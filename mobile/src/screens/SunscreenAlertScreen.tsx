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
    checkSunscreenAlert,
    createSunscreenAlert,
    getSunscreenAlerts,
    SunscreenAlertCheckResponse,
    SunscreenAlertResponse,
} from "../api/sunscreenAlertApi";
import ScreenContainer from "../components/ScreenContainer";

const DEFAULT_AREA_NO = "1100000000";

export default function SunscreenAlertScreen() {
    const [areaNo, setAreaNo] = useState(DEFAULT_AREA_NO);
    const [alertTime, setAlertTime] = useState("08:00");
    const [uvThreshold, setUvThreshold] = useState("6");
    const [enabled, setEnabled] = useState(true);

    const [alerts, setAlerts] = useState<SunscreenAlertResponse[]>([]);
    const [checkResult, setCheckResult] =
        useState<SunscreenAlertCheckResponse | null>(null);

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useEffect(() => {
        loadAlerts();
    }, []);

    async function loadAlerts() {
        try {
            setLoading(true);
            setErrorMessage(null);

            const data = await getSunscreenAlerts();
            setAlerts(data);
        } catch (error) {
            console.error(error);
            setErrorMessage("선크림 알림 설정을 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    async function handleCreateAlert() {
        try {
            setLoading(true);
            setMessage(null);
            setErrorMessage(null);

            const thresholdNumber = Number(uvThreshold);

            if (Number.isNaN(thresholdNumber)) {
                setErrorMessage("UV 기준값은 숫자로 입력해야 합니다.");
                return;
            }

            await createSunscreenAlert({
                areaNo,
                alertTime,
                uvThreshold: thresholdNumber,
                enabled,
            });

            setMessage("선크림 알림 설정이 저장되었습니다.");
            setCheckResult(null);
            await loadAlerts();
        } catch (error) {
            console.error(error);
            setErrorMessage("선크림 알림 설정 저장에 실패했습니다.");
        } finally {
            setLoading(false);
        }
    }

    async function handleCheckAlert(id: number) {
        try {
            setLoading(true);
            setMessage(null);
            setErrorMessage(null);

            const result = await checkSunscreenAlert(id);
            setCheckResult(result);
        } catch (error) {
            console.error(error);
            setErrorMessage("알림 판단 결과를 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <Text style={styles.title}>선크림 알림</Text>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>알림 설정 만들기</Text>

                    <Text style={styles.label}>지역 코드</Text>
                    <TextInput
                        style={styles.input}
                        value={areaNo}
                        onChangeText={setAreaNo}
                        placeholder="예: 1100000000"
                        keyboardType="number-pad"
                    />

                    <Text style={styles.label}>알림 시간</Text>
                    <TextInput
                        style={styles.input}
                        value={alertTime}
                        onChangeText={setAlertTime}
                        placeholder="예: 08:00"
                    />

                    <Text style={styles.label}>알림 기준 UV 지수</Text>
                    <TextInput
                        style={styles.input}
                        value={uvThreshold}
                        onChangeText={setUvThreshold}
                        placeholder="예: 6"
                        keyboardType="number-pad"
                    />

                    <View style={styles.switchRow}>
                        <View>
                            <Text style={styles.label}>알림 사용 여부</Text>
                            <Text style={styles.subText}>{enabled ? "ON" : "OFF"}</Text>
                        </View>

                        <Switch value={enabled} onValueChange={setEnabled} />
                    </View>

                    <Pressable style={styles.primaryButton} onPress={handleCreateAlert}>
                        <Text style={styles.primaryButtonText}>알림 설정 저장</Text>
                    </Pressable>
                </View>

                {loading && (
                    <View style={styles.statusBox}>
                        <ActivityIndicator size="small" />
                        <Text style={styles.statusText}>처리 중...</Text>
                    </View>
                )}

                {message && <Text style={styles.successText}>{message}</Text>}
                {errorMessage && <Text style={styles.errorText}>{errorMessage}</Text>}

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>저장된 알림 설정</Text>

                    {alerts.length === 0 ? (
                        <Text style={styles.message}>저장된 알림 설정이 없습니다.</Text>
                    ) : (
                        alerts.map((alert) => (
                            <View key={alert.id} style={styles.alertItem}>
                                <View>
                                    <Text style={styles.alertTitle}>{alert.locationName}</Text>
                                    <Text style={styles.subText}>지역 코드: {alert.areaNo}</Text>
                                    <Text style={styles.subText}>
                                        알림 시간: {formatTime(alert.alertTime)}
                                    </Text>
                                    <Text style={styles.subText}>
                                        기준 UV 지수: {alert.uvThreshold} 이상
                                    </Text>
                                    <Text style={styles.subText}>
                                        상태: {alert.enabled ? "ON" : "OFF"}
                                    </Text>
                                </View>

                                <Pressable
                                    style={styles.secondaryButton}
                                    onPress={() => handleCheckAlert(alert.id)}
                                >
                                    <Text style={styles.secondaryButtonText}>알림 판단</Text>
                                </Pressable>
                            </View>
                        ))
                    )}
                </View>

                {checkResult && (
                    <View style={styles.card}>
                        <Text style={styles.sectionTitle}>알림 판단 결과</Text>

                        <Text style={styles.value}>{checkResult.locationName}</Text>
                        <Text style={styles.subText}>
                            현재 UV 지수: {checkResult.currentUvValue}
                        </Text>
                        <Text style={styles.subText}>
                            현재 UV 단계: {checkResult.currentUvLevel}
                        </Text>
                        <Text style={styles.subText}>
                            알림 기준: {checkResult.uvThreshold} 이상
                        </Text>
                        <Text
                            style={[
                                styles.notifyResult,
                                checkResult.shouldNotify
                                    ? styles.notifyOn
                                    : styles.notifyOff,
                            ]}
                        >
                            {checkResult.shouldNotify ? "알림 필요" : "알림 불필요"}
                        </Text>

                        <Text style={styles.message}>{checkResult.message}</Text>
                    </View>
                )}
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
        paddingBottom: 48,
    },
    title: {
        fontSize: 28,
        fontWeight: "800",
        marginBottom: 8,
    },
    card: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 16,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: "800",
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
        fontWeight: "700",
    },
    subText: {
        marginTop: 4,
        fontSize: 12,
        color: "#777777",
    },
    message: {
        marginTop: 8,
        fontSize: 14,
        color: "#444444",
        lineHeight: 20,
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
    switchRow: {
        marginTop: 8,
        marginBottom: 16,
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    primaryButton: {
        marginTop: 8,
        backgroundColor: "#2563EB",
        paddingVertical: 14,
        borderRadius: 12,
        alignItems: "center",
    },
    primaryButtonText: {
        color: "#FFFFFF",
        fontSize: 15,
        fontWeight: "800",
    },
    secondaryButton: {
        marginTop: 12,
        backgroundColor: "#111827",
        paddingVertical: 10,
        paddingHorizontal: 14,
        borderRadius: 10,
        alignSelf: "flex-start",
    },
    secondaryButtonText: {
        color: "#FFFFFF",
        fontWeight: "700",
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
        fontWeight: "700",
    },
    errorText: {
        color: "#DC2626",
        fontSize: 14,
        fontWeight: "700",
    },
    alertItem: {
        paddingVertical: 14,
        borderTopWidth: 1,
        borderTopColor: "#EEEEEE",
    },
    alertTitle: {
        fontSize: 16,
        fontWeight: "800",
    },
    notifyResult: {
        marginTop: 12,
        fontSize: 20,
        fontWeight: "900",
    },
    notifyOn: {
        color: "#DC2626",
    },
    notifyOff: {
        color: "#059669",
    },
});