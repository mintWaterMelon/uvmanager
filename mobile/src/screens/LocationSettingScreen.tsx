import { useEffect, useRef, useState } from "react";
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

const SEARCH_DEBOUNCE_MS = 400;

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

    const searchDebounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    const settingsAbortControllerRef = useRef<AbortController | null>(null);
    const searchAbortControllerRef = useRef<AbortController | null>(null);
    const saveAbortControllerRef = useRef<AbortController | null>(null);
    const latestSearchIdRef = useRef(0);

    useEffect(() => {
        loadSettings();

        return () => {
            clearSearchDebounceTimer();
            settingsAbortControllerRef.current?.abort();
            searchAbortControllerRef.current?.abort();
            saveAbortControllerRef.current?.abort();
        };
    }, []);

    function clearSearchDebounceTimer() {
        if (searchDebounceTimerRef.current) {
            clearTimeout(searchDebounceTimerRef.current);
            searchDebounceTimerRef.current = null;
        }
    }

    async function loadSettings() {
        settingsAbortControllerRef.current?.abort();

        const abortController = new AbortController();
        settingsAbortControllerRef.current = abortController;

        try {
            setLoading(true);
            setErrorMessage(null);

            const settings = await getSettings({
                signal: abortController.signal,
            });

            setDefaultAreaNo(settings.defaultAreaNo);
            setDefaultLocationName(settings.defaultLocationName);
            setDefaultUvThreshold(settings.defaultUvThreshold);
            setSunscreenAlertEnabled(settings.sunscreenAlertEnabled);
            setDefaultAlertTime(formatTime(settings.defaultAlertTime));
            setKeyword(settings.defaultLocationName);
        } catch (error) {
            if (isAbortError(error)) {
                return;
            }

            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            if (settingsAbortControllerRef.current === abortController) {
                setLoading(false);
                settingsAbortControllerRef.current = null;
            }
        }
    }

    function handleChangeKeyword(nextKeyword: string) {
        setKeyword(nextKeyword);
        setMessage(null);
        setErrorMessage(null);

        clearSearchDebounceTimer();

        const trimmedKeyword = nextKeyword.trim();

        if (trimmedKeyword.length === 0) {
            searchAbortControllerRef.current?.abort();
            setAreas([]);
            setSearching(false);
            return;
        }

        searchDebounceTimerRef.current = setTimeout(() => {
            searchAreasByKeyword(trimmedKeyword);
        }, SEARCH_DEBOUNCE_MS);
    }

    async function handleSearchArea() {
        clearSearchDebounceTimer();
        await searchAreasByKeyword(keyword);
    }

    async function searchAreasByKeyword(searchKeyword: string) {
        const trimmedKeyword = searchKeyword.trim();

        if (trimmedKeyword.length === 0) {
            searchAbortControllerRef.current?.abort();
            setAreas([]);
            setSearching(false);
            return;
        }

        const searchId = latestSearchIdRef.current + 1;
        latestSearchIdRef.current = searchId;

        searchAbortControllerRef.current?.abort();

        const abortController = new AbortController();
        searchAbortControllerRef.current = abortController;

        try {
            setSearching(true);
            setMessage(null);
            setErrorMessage(null);

            const result = await searchAreas(trimmedKeyword, {
                signal: abortController.signal,
            });

            if (
                abortController.signal.aborted ||
                latestSearchIdRef.current !== searchId
            ) {
                return;
            }

            setAreas(result);
        } catch (error) {
            if (isAbortError(error)) {
                return;
            }

            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            if (searchAbortControllerRef.current === abortController) {
                setSearching(false);
                searchAbortControllerRef.current = null;
            }
        }
    }

    async function handleSelectArea(area: AreaResponse) {
        if (savingAreaNo !== null) {
            return;
        }

        clearSearchDebounceTimer();
        searchAbortControllerRef.current?.abort();
        saveAbortControllerRef.current?.abort();

        const abortController = new AbortController();
        saveAbortControllerRef.current = abortController;

        try {
            setSavingAreaNo(area.areaNo);
            setMessage(null);
            setErrorMessage(null);

            const updated = await updateSettings({
                defaultAreaNo: area.areaNo,
                defaultUvThreshold,
                sunscreenAlertEnabled,
                defaultAlertTime,
            }, {
                signal: abortController.signal,
            });

            setDefaultAreaNo(updated.defaultAreaNo);
            setDefaultLocationName(updated.defaultLocationName);
            setKeyword(updated.defaultLocationName);
            setAreas([]);

            setMessage("기본 위치가 저장되었습니다.");
        } catch (error) {
            if (isAbortError(error)) {
                return;
            }

            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            if (saveAbortControllerRef.current === abortController) {
                setSavingAreaNo(null);
                saveAbortControllerRef.current = null;
            }
        }
    }

    function handleBack() {
        if (savingAreaNo !== null) {
            return;
        }

        if (from === "settings") {
            router.replace("/settings");
            return;
        }

        router.replace("/");
    }

    const isSaving = savingAreaNo !== null;

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

                    <Pressable
                        style={[styles.backButton, isSaving && styles.disabledButton]}
                        onPress={handleBack}
                        disabled={isSaving}
                    >
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
                            style={[styles.input, isSaving && styles.disabledInput]}
                            value={keyword}
                            onChangeText={handleChangeKeyword}
                            placeholder="예: 서울, 종로구, 청운효자동"
                            autoCapitalize="none"
                            editable={!isSaving}
                        />

                        <Pressable
                            style={[
                                styles.searchButton,
                                (searching || isSaving || keyword.trim().length === 0) && styles.disabledButton,
                            ]}
                            onPress={handleSearchArea}
                            disabled={searching || isSaving || keyword.trim().length === 0}
                        >
                            <Text style={styles.searchButtonText}>
                                {searching ? "검색 중" : "검색"}
                            </Text>
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
                                    style={[
                                        styles.resultItem,
                                        isSaving && savingAreaNo !== area.areaNo && styles.disabledResultItem,
                                    ]}
                                    onPress={() => handleSelectArea(area)}
                                    disabled={isSaving}
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

function isAbortError(error: unknown) {
    return error instanceof Error && error.name === "AbortError";
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
    disabledButton: {
        opacity: 0.5,
    },
    disabledInput: {
        opacity: 0.6,
        backgroundColor: "#F3F4F6",
    },
    disabledResultItem: {
        opacity: 0.45,
    },
});
