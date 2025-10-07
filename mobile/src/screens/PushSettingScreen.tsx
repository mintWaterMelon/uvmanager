import { useEffect, useState } from "react";
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    StyleSheet,
    Switch,
    Text,
    TextInput,
    View,
} from "react-native";

import {
    getPushSettings,
    updatePushSettings,
} from "../api/pushSettingApi";
import ScreenContainer from "../components/ScreenContainer";

export default function PushSettingScreen() {
    const [uvAlertEnabled, setUvAlertEnabled] = useState(true);
    const [uvAlertThreshold, setUvAlertThreshold] = useState("8");
    const [dustAlertEnabled, setDustAlertEnabled] = useState(false);
    const [alertTime, setAlertTime] = useState("08:00");

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [message, setMessage] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useEffect(() => {
        loadPushSettings();
    }, []);

    async function loadPushSettings() {
        try {
            setLoading(true);
            setErrorMessage(null);

            const settings = await getPushSettings();

            setUvAlertEnabled(settings.uvAlertEnabled);
            setUvAlertThreshold(String(settings.uvAlertThreshold));
            setDustAlertEnabled(settings.dustAlertEnabled);
            setAlertTime(formatTime(settings.alertTime));
        } catch (error) {
            console.error(error);
            setErrorMessage("푸시 설정을 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    async function handleSave() {
        try {
            setSaving(true);
            setMessage(null);
            setErrorMessage(null);

            const threshold = Number(uvAlertThreshold);

            if (Number.isNaN(threshold)) {
                setErrorMessage("자외선 알림 기준값은 숫자로 입력해야 합니다.");
                return;
            }

            if (threshold < 0 || threshold > 11) {
                setErrorMessage("자외선 알림 기준값은 0 이상 11 이하로 입력해야 합니다.");
                return;
            }

            if (!alertTime.match(/^([01]\d|2[0-3]):[0-5]\d$/)) {
                setErrorMessage("알림 시간은 HH:mm 형식으로 입력해야 합니다. 예: 08:00");
                return;
            }

            const updated = await updatePushSettings({
                uvAlertEnabled,
                uvAlertThreshold: threshold,
                dustAlertEnabled,
                alertTime,
            });

            setUvAlertEnabled(updated.uvAlertEnabled);
            setUvAlertThreshold(String(updated.uvAlertThreshold));
            setDustAlertEnabled(updated.dustAlertEnabled);
            setAlertTime(formatTime(updated.alertTime));

            setMessage("푸시 설정이 저장되었습니다.");
        } catch (error) {
            console.error(error);
            setErrorMessage("푸시 설정 저장에 실패했습니다.");
        } finally {
            setSaving(false);
        }
    }

    if (loading) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <ActivityIndicator size="large" />
                    <Text style={styles.loadingText}>푸시 설정을 불러오는 중입니다...</Text>
                </View>
            </ScreenContainer>
        );
    }

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <Text style={styles.title}>푸시 설정</Text>

                <View style={styles.card}>
                    <View style={styles.switchRow}>
                        <View style={styles.switchTextBox}>
                            <Text style={styles.sectionTitle}>자외선 알림</Text>
                            <Text style={styles.description}>
                                설정한 기준값 이상이면 자외선 주의 알림을 받을 수 있습니다.
                            </Text>
                        </View>

                        <Switch
                            value={uvAlertEnabled}
                            onValueChange={setUvAlertEnabled}
                        />
                    </View>

                    <Text style={styles.label}>자외선 알림 기준값</Text>
                    <View style={styles.thresholdRow}>
                        <ThresholdButton
                            label="8 이상"
                            active={uvAlertThreshold === "8"}
                            onPress={() => setUvAlertThreshold("8")}
                        />
                        <ThresholdButton
                            label="11 이상"
                            active={uvAlertThreshold === "11"}
                            onPress={() => setUvAlertThreshold("11")}
                        />
                    </View>

                    <TextInput
                        style={styles.input}
                        value={uvAlertThreshold}
                        onChangeText={setUvAlertThreshold}
                        placeholder="예: 8"
                        keyboardType="number-pad"
                    />
                </View>

                <View style={styles.card}>
                    <View style={styles.switchRow}>
                        <View style={styles.switchTextBox}>
                            <Text style={styles.sectionTitle}>미세먼지 알림</Text>
                            <Text style={styles.description}>
                                미세먼지 또는 대기 상태가 좋지 않을 때 알림을 받을 수 있습니다.
                            </Text>
                        </View>

                        <Switch
                            value={dustAlertEnabled}
                            onValueChange={setDustAlertEnabled}
                        />
                    </View>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>알림 시간</Text>
                    <Text style={styles.description}>
                        매일 설정한 시간에 날씨와 자외선 상태를 확인합니다.
                    </Text>

                    <TextInput
                        style={styles.input}
                        value={alertTime}
                        onChangeText={setAlertTime}
                        placeholder="예: 08:00"
                    />
                </View>

                {message && <Text style={styles.successText}>{message}</Text>}
                {errorMessage && <Text style={styles.errorText}>{errorMessage}</Text>}

                <Pressable
                    style={[styles.saveButton, saving && styles.disabledButton]}
                    onPress={handleSave}
                    disabled={saving}
                >
                    <Text style={styles.saveButtonText}>
                        {saving ? "저장 중..." : "푸시 설정 저장"}
                    </Text>
                </Pressable>
            </ScrollView>
        </ScreenContainer>
    );
}

function ThresholdButton({
    label,
    active,
    onPress,
}: {
    label: string;
    active: boolean;
    onPress: () => void;
}) {
    return (
        <Pressable
            style={[styles.thresholdButton, active && styles.thresholdButtonActive]}
            onPress={onPress}
        >
            <Text
                style={[
                    styles.thresholdButtonText,
                    active && styles.thresholdButtonTextActive,
                ]}
            >
                {label}
            </Text>
        </Pressable>
    );
}

function formatTime(value: string) {
    if (value.length >= 5) {
        return value.slice(0, 5);
    }

    return value;
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#F7F8FA",
    },
    content: {
        padding: 20,
        gap: 16,
        paddingBottom: 56,
    },
    centerContainer: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        padding: 20,
        backgroundColor: "#F7F8FA",
    },
    loadingText: {
        marginTop: 12,
        fontSize: 14,
        color: "#555555",
    },
    title: {
        fontSize: 28,
        fontWeight: "900",
        color: "#111827",
        marginBottom: 8,
    },
    card: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 18,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
    },
    description: {
        marginTop: 6,
        fontSize: 13,
        lineHeight: 19,
        color: "#6B7280",
    },
    label: {
        marginTop: 16,
        marginBottom: 8,
        fontSize: 14,
        fontWeight: "700",
        color: "#374151",
    },
    switchRow: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    switchTextBox: {
        flex: 1,
        paddingRight: 12,
    },
    thresholdRow: {
        flexDirection: "row",
        gap: 8,
        marginBottom: 10,
    },
    thresholdButton: {
        flex: 1,
        paddingVertical: 11,
        borderRadius: 12,
        backgroundColor: "#F3F4F6",
        alignItems: "center",
    },
    thresholdButtonActive: {
        backgroundColor: "#2563EB",
    },
    thresholdButtonText: {
        color: "#374151",
        fontWeight: "800",
    },
    thresholdButtonTextActive: {
        color: "#FFFFFF",
    },
    input: {
        borderWidth: 1,
        borderColor: "#DDDDDD",
        borderRadius: 12,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 15,
        backgroundColor: "#FFFFFF",
    },
    successText: {
        color: "#059669",
        fontSize: 14,
        fontWeight: "800",
    },
    errorText: {
        color: "#DC2626",
        fontSize: 14,
        fontWeight: "800",
    },
    saveButton: {
        backgroundColor: "#2563EB",
        paddingVertical: 16,
        borderRadius: 14,
        alignItems: "center",
    },
    disabledButton: {
        opacity: 0.6,
    },
    saveButtonText: {
        color: "#FFFFFF",
        fontSize: 16,
        fontWeight: "900",
    },
});