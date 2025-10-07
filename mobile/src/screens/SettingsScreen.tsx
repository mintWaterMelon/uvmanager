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
import { useRouter } from "expo-router";

import { AreaResponse, searchAreas } from "../api/areaApi";
import { getSettings, updateSettings } from "../api/settingApi";
import ScreenContainer from "../components/ScreenContainer";

export default function SettingsScreen() {
    const router = useRouter();

    const [defaultAreaNo, setDefaultAreaNo] = useState("1100000000");
    const [defaultLocationName, setDefaultLocationName] = useState("서울특별시");
    const [defaultUvThreshold, setDefaultUvThreshold] = useState("6");
    const [sunscreenAlertEnabled, setSunscreenAlertEnabled] = useState(true);
    const [defaultAlertTime, setDefaultAlertTime] = useState("08:00");

    const [keyword, setKeyword] = useState("");
    const [areas, setAreas] = useState<AreaResponse[]>([]);

    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);

    const [message, setMessage] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useEffect(() => {
        loadSettings();
    }, []);

    async function loadSettings() {
        try {
            setLoading(true);
            setErrorMessage(null);

            const settings = await getSettings();

            setDefaultAreaNo(settings.defaultAreaNo);
            setDefaultLocationName(settings.defaultLocationName);
            setDefaultUvThreshold(String(settings.defaultUvThreshold));
            setSunscreenAlertEnabled(settings.sunscreenAlertEnabled);
            setDefaultAlertTime(formatTime(settings.defaultAlertTime));
            setKeyword(settings.defaultLocationName);
        } catch (error) {
            console.error(error);
            setErrorMessage("설정 정보를 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    async function handleSearchArea() {
        try {
            setLoading(true);
            setMessage(null);
            setErrorMessage(null);

            const result = await searchAreas(keyword);
            setAreas(result);
        } catch (error) {
            console.error(error);
            setErrorMessage("지역 정보를 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    function handleSelectArea(area: AreaResponse) {
        setDefaultAreaNo(area.areaNo);
        setDefaultLocationName(area.displayName);
        setKeyword(area.displayName);
        setAreas([]);
    }

    async function handleSaveSettings() {
        try {
            setSaving(true);
            setMessage(null);
            setErrorMessage(null);

            const thresholdNumber = Number(defaultUvThreshold);

            if (Number.isNaN(thresholdNumber)) {
                setErrorMessage("UV 기준값은 숫자로 입력해야 합니다.");
                return;
            }

            if (thresholdNumber < 0 || thresholdNumber > 11) {
                setErrorMessage("UV 기준값은 0 이상 11 이하로 입력해야 합니다.");
                return;
            }

            if (!defaultAlertTime.match(/^([01]\d|2[0-3]):[0-5]\d$/)) {
                setErrorMessage("알림 시간은 HH:mm 형식으로 입력해야 합니다. 예: 08:00");
                return;
            }

            const updatedSettings = await updateSettings({
                defaultAreaNo,
                defaultUvThreshold: thresholdNumber,
                sunscreenAlertEnabled,
                defaultAlertTime,
            });

            setDefaultAreaNo(updatedSettings.defaultAreaNo);
            setDefaultLocationName(updatedSettings.defaultLocationName);
            setDefaultUvThreshold(String(updatedSettings.defaultUvThreshold));
            setSunscreenAlertEnabled(updatedSettings.sunscreenAlertEnabled);
            setDefaultAlertTime(formatTime(updatedSettings.defaultAlertTime));
            setKeyword(updatedSettings.defaultLocationName);

            setMessage("설정이 저장되었습니다.");
        } catch (error) {
            console.error(error);
            setErrorMessage("설정 저장에 실패했습니다.");
        } finally {
            setSaving(false);
        }
    }

    if (loading && !defaultAreaNo) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <ActivityIndicator size="large" />
                    <Text style={styles.loadingText}>설정을 불러오는 중입니다...</Text>
                </View>
            </ScreenContainer>
        );
    }

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <Text style={styles.title}>설정</Text>

                <Pressable
                    style={styles.menuCard}
                    onPress={() => router.push("/location-settings")}
                >
                    <Text style={styles.menuTitle}>위치 설정</Text>
                    <Text style={styles.menuDescription}>
                        현재 기본 위치를 검색하고 변경합니다.
                    </Text>
                </Pressable>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>알림 기본값</Text>

                    <Text style={styles.label}>기본 UV 기준값</Text>
                    <TextInput
                        style={styles.input}
                        value={defaultUvThreshold}
                        onChangeText={setDefaultUvThreshold}
                        placeholder="예: 6"
                        keyboardType="number-pad"
                    />

                    <Text style={styles.label}>기본 알림 시간</Text>
                    <TextInput
                        style={styles.input}
                        value={defaultAlertTime}
                        onChangeText={setDefaultAlertTime}
                        placeholder="예: 08:00"
                    />

                    <View style={styles.switchRow}>
                        <View>
                            <Text style={styles.label}>선크림 알림 사용</Text>
                            <Text style={styles.subText}>
                                {sunscreenAlertEnabled ? "사용" : "사용 안 함"}
                            </Text>
                        </View>

                        <Switch
                            value={sunscreenAlertEnabled}
                            onValueChange={setSunscreenAlertEnabled}
                        />
                    </View>
                </View>

                <Pressable
                    style={styles.menuCard}
                    onPress={() => router.push("/push-settings")}
                >
                    <Text style={styles.menuTitle}>푸시 설정</Text>
                    <Text style={styles.menuDescription}>
                        자외선 알림, 미세먼지 알림, 알림 시간을 설정합니다.
                    </Text>
                </Pressable>

                <Pressable style={styles.menuCard}>
                    <Text style={styles.menuTitle}>사용방법</Text>
                    <Text style={styles.menuDescription}>
                        앱 사용 방법과 자외선 정보 확인 방법을 안내합니다.
                    </Text>
                </Pressable>

                <Pressable style={styles.menuCard}>
                    <Text style={styles.menuTitle}>라이센스</Text>
                    <Text style={styles.menuDescription}>
                        공공데이터 출처와 제3자 저작권 표시를 확인합니다.
                    </Text>
                </Pressable>

                {loading && (
                    <View style={styles.statusBox}>
                        <ActivityIndicator size="small" />
                        <Text style={styles.statusText}>처리 중...</Text>
                    </View>
                )}

                {message && <Text style={styles.successText}>{message}</Text>}
                {errorMessage && <Text style={styles.errorText}>{errorMessage}</Text>}

                <Pressable
                    style={[styles.saveButton, saving && styles.disabledButton]}
                    onPress={handleSaveSettings}
                    disabled={saving}
                >
                    <Text style={styles.saveButtonText}>
                        {saving ? "저장 중..." : "설정 저장"}
                    </Text>
                </Pressable>
            </ScrollView>
        </ScreenContainer>
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
        marginBottom: 12,
    },
    label: {
        fontSize: 14,
        color: "#666666",
        marginBottom: 8,
        marginTop: 8,
    },
    value: {
        fontSize: 20,
        fontWeight: "800",
        color: "#111827",
    },
    subText: {
        marginTop: 4,
        fontSize: 12,
        color: "#777777",
    },
    input: {
        borderWidth: 1,
        borderColor: "#DDDDDD",
        borderRadius: 12,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 14,
        backgroundColor: "#FFFFFF",
    },
    searchRow: {
        flexDirection: "row",
        gap: 8,
    },
    searchButton: {
        backgroundColor: "#2563EB",
        borderRadius: 12,
        paddingHorizontal: 16,
        justifyContent: "center",
        alignItems: "center",
    },
    searchButtonText: {
        color: "#FFFFFF",
        fontWeight: "800",
    },
    resultBox: {
        marginTop: 12,
        borderTopWidth: 1,
        borderTopColor: "#EEEEEE",
    },
    resultItem: {
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: "#EEEEEE",
    },
    resultName: {
        fontSize: 16,
        fontWeight: "800",
        color: "#111827",
    },
    resultCode: {
        marginTop: 4,
        fontSize: 12,
        color: "#777777",
    },
    selectedBox: {
        marginTop: 16,
        paddingTop: 12,
        borderTopWidth: 1,
        borderTopColor: "#EEEEEE",
    },
    switchRow: {
        marginTop: 8,
        marginBottom: 4,
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    menuCard: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 18,
    },
    menuTitle: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
    },
    menuDescription: {
        marginTop: 6,
        fontSize: 13,
        lineHeight: 19,
        color: "#6B7280",
    },
    statusBox: {
        backgroundColor: "#FFFFFF",
        padding: 12,
        borderRadius: 12,
        flexDirection: "row",
        alignItems: "center",
        gap: 8,
    },
    statusText: {
        fontSize: 14,
        color: "#555555",
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