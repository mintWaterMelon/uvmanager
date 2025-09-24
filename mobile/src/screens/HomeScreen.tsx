import { useCallback, useState } from "react";
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    View,
} from "react-native";
import { useFocusEffect } from "expo-router";

import { getHome, HomeResponse, UvForecast } from "../api/homeApi";
import { getSettings } from "../api/settingApi";
import ScreenContainer from "../components/ScreenContainer";

export default function HomeScreen() {
    const [home, setHome] = useState<HomeResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useFocusEffect(
        useCallback(() => {
            loadHomeWithSettings();
        }, [])
    );

    async function loadHomeWithSettings() {
        try {
            setLoading(true);
            setErrorMessage(null);

            const settings = await getSettings();
            const data = await getHome(settings.defaultAreaNo);

            setHome(data);
        } catch (error) {
            console.error(error);
            setErrorMessage("홈 화면 데이터를 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <ActivityIndicator size="large" />
                    <Text style={styles.loadingText}>자외선 정보를 불러오는 중입니다...</Text>
                </View>
            </ScreenContainer>
        );
    }

    if (errorMessage || home === null) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <Text style={styles.errorTitle}>오류 발생</Text>
                    <Text style={styles.errorMessage}>{errorMessage}</Text>

                    <Pressable style={styles.retryButton} onPress={loadHomeWithSettings}>
                        <Text style={styles.retryButtonText}>다시 시도</Text>
                    </Pressable>
                </View>
            </ScreenContainer>
        );
    }

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <View style={styles.headerRow}>
                    <Text style={styles.title}>UV Alert</Text>

                    <Pressable style={styles.refreshButton} onPress={loadHomeWithSettings}>
                        <Text style={styles.refreshButtonText}>새로고침</Text>
                    </Pressable>
                </View>

                <View style={styles.card}>
                    <Text style={styles.label}>현재 시간</Text>
                    <Text style={styles.value}>{formatDateTime(home.currentTime)}</Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.label}>현재 위치</Text>
                    <Text style={styles.value}>{home.location.name}</Text>
                    <Text style={styles.subText}>지역 코드: {home.location.areaNo}</Text>
                </View>

                <View style={styles.uvCard}>
                    <Text style={styles.label}>현재 UV 지수</Text>

                    {home.currentUv ? (
                        <>
                            <Text style={styles.uvValue}>{home.currentUv.value}</Text>
                            <Text style={styles.uvLevel}>{home.currentUv.level}</Text>
                            <Text style={styles.message}>{home.currentUv.message}</Text>
                            <Text style={styles.subText}>
                                예보 시간: {formatDateTime(home.currentUv.forecastTime)}
                            </Text>
                        </>
                    ) : (
                        <Text style={styles.message}>현재 UV 정보를 찾을 수 없습니다.</Text>
                    )}
                </View>

                <View style={styles.card}>
                    <Text style={styles.label}>기준 발표 시각</Text>
                    <Text style={styles.value}>{formatBaseTime(home.baseTime)}</Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>시간대별 UV 지수</Text>

                    {home.forecasts.length === 0 ? (
                        <Text style={styles.message}>시간대별 UV 정보가 없습니다.</Text>
                    ) : (
                        home.forecasts.map((forecast) => (
                            <ForecastRow
                                key={`${forecast.hourAfter}-${forecast.forecastTime}`}
                                forecast={forecast}
                            />
                        ))
                    )}
                </View>
            </ScrollView>
        </ScreenContainer>
    );
}

function ForecastRow({ forecast }: { forecast: UvForecast }) {
    return (
        <View style={styles.forecastRow}>
            <View>
                <Text style={styles.forecastTime}>
                    {formatTimeOnly(forecast.forecastTime)}
                </Text>
                <Text style={styles.subText}>{forecast.hourAfter}시간 후</Text>
            </View>

            <View style={styles.forecastRight}>
                <Text style={styles.forecastValue}>UV {forecast.value}</Text>
                <Text style={styles.forecastLevel}>{forecast.level}</Text>
            </View>
        </View>
    );
}

function formatDateTime(value: string) {
    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });
}

function formatTimeOnly(value: string) {
    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleTimeString("ko-KR", {
        hour: "2-digit",
        minute: "2-digit",
    });
}

function formatBaseTime(baseTime: string) {
    if (baseTime.length !== 10) {
        return baseTime;
    }

    const year = baseTime.slice(0, 4);
    const month = baseTime.slice(4, 6);
    const day = baseTime.slice(6, 8);
    const hour = baseTime.slice(8, 10);

    return `${year}-${month}-${day} ${hour}:00`;
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
    errorTitle: {
        fontSize: 22,
        fontWeight: "700",
        marginBottom: 8,
    },
    errorMessage: {
        fontSize: 15,
        color: "#555555",
    },
    retryButton: {
        marginTop: 16,
        backgroundColor: "#2563EB",
        paddingVertical: 12,
        paddingHorizontal: 20,
        borderRadius: 12,
    },
    retryButtonText: {
        color: "#FFFFFF",
        fontWeight: "700",
    },
    headerRow: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    title: {
        fontSize: 28,
        fontWeight: "800",
        marginBottom: 8,
    },
    refreshButton: {
        backgroundColor: "#111827",
        paddingVertical: 8,
        paddingHorizontal: 12,
        borderRadius: 10,
    },
    refreshButtonText: {
        color: "#FFFFFF",
        fontSize: 12,
        fontWeight: "700",
    },
    card: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 16,
    },
    uvCard: {
        backgroundColor: "#FFFFFF",
        padding: 20,
        borderRadius: 20,
    },
    label: {
        fontSize: 14,
        color: "#666666",
        marginBottom: 8,
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
    uvValue: {
        fontSize: 56,
        fontWeight: "900",
    },
    uvLevel: {
        fontSize: 18,
        fontWeight: "700",
        marginBottom: 8,
    },
    message: {
        fontSize: 14,
        color: "#444444",
        lineHeight: 20,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: "700",
        marginBottom: 12,
    },
    forecastRow: {
        paddingVertical: 12,
        borderTopWidth: 1,
        borderTopColor: "#EEEEEE",
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    forecastTime: {
        fontSize: 16,
        fontWeight: "700",
    },
    forecastRight: {
        alignItems: "flex-end",
    },
    forecastValue: {
        fontSize: 16,
        fontWeight: "800",
    },
    forecastLevel: {
        marginTop: 2,
        fontSize: 12,
        color: "#666666",
    },
});