import { useEffect, useState } from "react";
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    View,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";

import { AreaResponse, searchAreas } from "../api/areaApi";
import { getSettings, updateSettings } from "../api/settingApi";
import ScreenContainer from "../components/ScreenContainer";
import { getApiErrorMessage, logApiError } from "../api/apiErrorMessage";

export default function LocationSettingScreen() {
    const router = useRouter();
    const { from } = useLocalSearchParams<{ from?: string }>();

    const [defaultAreaNo, setDefaultAreaNo] = useState("1100000000");
    const [defaultLocationName, setDefaultLocationName] = useState("서울특별시");
    const [defaultUvThreshold, setDefaultUvThreshold] = useState(6);
    const [sunscreenAlertEnabled, setSunscreenAlertEnabled] = useState(true);
    const [defaultAlertTime, setDefaultAlertTime] = useState("08:00");

    const [keyword, setKeyword] = useState("");
    const [areas, setAreas] = useState<AreaResponse[]>([]);

    const [loading, setLoading] = useState(true);
    const [searching, setSearching] = useState(false);
    const [savingAreaNo, setSavingAreaNo] = useState<string | null>(null);

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
            setDefaultUvThreshold(settings.defaultUvThreshold);
            setSunscreenAlertEnabled(settings.sunscreenAlertEnabled);
            setDefaultAlertTime(formatTime(settings.defaultAlertTime));
            setKeyword(settings.defaultLocationName);
        } catch (error) {
            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            setLoading(false);
        }
    }

    async function handleSearchArea() {
        try {
            setSearching(true);
            setMessage(null);
            setErrorMessage(null);

            const result = await searchAreas(keyword);
            setAreas(result);
        } catch (error) {
            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            setSearching(false);
        }
    }

    async function handleSelectArea(area: AreaResponse) {
        try {
            setSavingAreaNo(area.areaNo);
            setMessage(null);
            setErrorMessage(null);

            const updated = await updateSettings({
                defaultAreaNo: area.areaNo,
                defaultUvThreshold,
                sunscreenAlertEnabled,
                defaultAlertTime,
            });

            setDefaultAreaNo(updated.defaultAreaNo);
            setDefaultLocationName(updated.defaultLocationName);
            setKeyword(updated.defaultLocationName);
            setAreas([]);

            setMessage("기본 위치가 저장되었습니다.");
        } catch (error) {
            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            setSavingAreaNo(null);
        }
    }

    function handleBack() {
        if (from === "settings") {
            router.replace("/settings");
            return;
        }

        router.replace("/");
    }

    if (loading) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <ActivityIndicator size="large" />
                    <Text style={styles.loadingText}>위치 설정을 불러오는 중입니다...</Text>
                </View>
            </ScreenContainer>
        );
    }

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <View style={styles.headerRow}>
                    <Text style={styles.title}>위치 설정</Text>

                    <Pressable style={styles.backButton} onPress={handleBack}>
                        <Text style={styles.backButtonText}>뒤로</Text>
                    </Pressable>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>현재 기본 위치</Text>
                    <Text style={styles.locationName}>{defaultLocationName}</Text>
                    <Text style={styles.subText}>지역 코드: {defaultAreaNo}</Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>위치 검색</Text>
                    <Text style={styles.description}>
                        지역명이나 행정구역 코드를 검색해서 기본 위치를 선택하세요.
                    </Text>

                    <View style={styles.searchRow}>
                        <TextInput
                            style={styles.input}
                            value={keyword}
                            onChangeText={setKeyword}
                            placeholder="예: 서울, 종로구, 청운효자동"
                            autoCapitalize="none"
                        />

                        <Pressable style={styles.searchButton} onPress={handleSearchArea}>
                            <Text style={styles.searchButtonText}>검색</Text>
                        </Pressable>
                    </View>

                    {searching && (
                        <View style={styles.statusBox}>
                            <ActivityIndicator size="small" />
                            <Text style={styles.statusText}>검색 중...</Text>
                        </View>
                    )}

                    {areas.length > 0 && (
                        <View style={styles.resultBox}>
                            {areas.map((area) => (
                                <Pressable
                                    key={area.areaNo}
                                    style={styles.resultItem}
                                    onPress={() => handleSelectArea(area)}
                                    disabled={savingAreaNo !== null}
                                >
                                    <View>
                                        <Text style={styles.resultName}>{area.displayName}</Text>
                                        <Text style={styles.resultCode}>{area.areaNo}</Text>
                                        <Text style={styles.resultCode}>
                                            격자 좌표: X {area.gridX}, Y {area.gridY}
                                        </Text>
                                    </View>

                                    {savingAreaNo === area.areaNo ? (
                                        <ActivityIndicator size="small" />
                                    ) : (
                                        <Text style={styles.selectText}>선택</Text>
                                    )}
                                </Pressable>
                            ))}
                        </View>
                    )}
                </View>

                {message && <Text style={styles.successText}>{message}</Text>}
                {errorMessage && <Text style={styles.errorText}>{errorMessage}</Text>}
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
    headerRow: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    title: {
        fontSize: 28,
        fontWeight: "900",
        color: "#111827",
    },
    backButton: {
        backgroundColor: "#111827",
        paddingVertical: 8,
        paddingHorizontal: 12,
        borderRadius: 10,
    },
    backButtonText: {
        color: "#FFFFFF",
        fontWeight: "800",
        fontSize: 12,
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
        marginBottom: 10,
    },
    locationName: {
        fontSize: 22,
        fontWeight: "900",
        color: "#111827",
    },
    description: {
        fontSize: 13,
        lineHeight: 19,
        color: "#6B7280",
        marginBottom: 12,
    },
    subText: {
        marginTop: 4,
        fontSize: 12,
        color: "#777777",
    },
    searchRow: {
        flexDirection: "row",
        gap: 8,
    },
    input: {
        flex: 1,
        borderWidth: 1,
        borderColor: "#DDDDDD",
        borderRadius: 12,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 14,
        backgroundColor: "#FFFFFF",
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
    statusBox: {
        marginTop: 12,
        backgroundColor: "#F9FAFB",
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
    resultBox: {
        marginTop: 12,
        borderTopWidth: 1,
        borderTopColor: "#EEEEEE",
    },
    resultItem: {
        paddingVertical: 14,
        borderBottomWidth: 1,
        borderBottomColor: "#EEEEEE",
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
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
    selectText: {
        color: "#2563EB",
        fontWeight: "900",
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
});