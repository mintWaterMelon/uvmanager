import { useCallback, useEffect, useRef, useState } from "react";
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    RefreshControl,
    StyleSheet,
    Text,
    View,
} from "react-native";
import FontAwesome from '@expo/vector-icons/FontAwesome';
import { useFocusEffect, useRouter } from "expo-router";

import {
    getHomeDashboard,
    HomeDashboardResponse,
    HomeDateType,
    HomeTableCell,
    HomeTableRow,
} from "../api/homeApi";
import { getSettings } from "../api/settingApi";
import ScreenContainer from "../components/ScreenContainer";
import { getApiErrorMessage, logApiError } from "../api/apiErrorMessage";

type DashboardCacheItem = {
    data: HomeDashboardResponse;
    savedAt: number;
};

const DASHBOARD_CACHE_TTL_MS = 5 * 60 * 1000;

export default function HomeScreen() {
    //페이지 이동 제어 router 객체 생성
    const router = useRouter();

    //대쉬보드 데이터
    const [dashboard, setDashboard] = useState<HomeDashboardResponse | null>(null);
    const [dateType, setDateType] = useState<HomeDateType>("TODAY");
    const [currentDateTime, setCurrentDateTime] = useState(() => new Date());

    const [isInitialLoading, setIsInitialLoading] = useState(true);
    const [isRefreshingDashboard, setIsRefreshingDashboard] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const dashboardRef = useRef<HomeDashboardResponse | null>(null);
    const dashboardCacheRef = useRef<Record<string, DashboardCacheItem>>({});
    const dateTypeRef = useRef<HomeDateType>("TODAY");
    const areaNoRef = useRef<string | null>(null);
    const abortControllerRef = useRef<AbortController | null>(null);
    const latestRequestIdRef = useRef(0);

    const updateDashboard = useCallback((nextDashboard: HomeDashboardResponse) => {
        dashboardRef.current = nextDashboard;
        setDashboard(nextDashboard);
    }, []);

    useEffect(() => {
        const timerId = setInterval(() => {
            setCurrentDateTime(new Date());
        }, 30_000);

        return () => {
            clearInterval(timerId);
        };
    }, []);

    const loadDashboard = useCallback(async (
        targetDateType: HomeDateType = dateTypeRef.current,
        options?: {
            forceRefresh?: boolean;
            forceSettingsRefresh?: boolean;
        }
    ) => {
        const requestId = latestRequestIdRef.current + 1;
        latestRequestIdRef.current = requestId;

        abortControllerRef.current?.abort();

        const abortController = new AbortController();
        abortControllerRef.current = abortController;

        const hasDashboard = dashboardRef.current !== null;

        if (hasDashboard) {
            setIsRefreshingDashboard(true);
        } else {
            setIsInitialLoading(true);
        }

        try {
            setErrorMessage(null);  //에러 메세지 초기화

            let areaNo = areaNoRef.current;

            if (!areaNo || options?.forceSettingsRefresh) {
                const settings = await getSettings({
                    signal: abortController.signal,
                });

                if (abortController.signal.aborted || latestRequestIdRef.current !== requestId) {
                    return;
                }

                const nextAreaNo = settings.defaultAreaNo;

                if (areaNoRef.current && areaNoRef.current !== nextAreaNo) {
                    dashboardCacheRef.current = {};
                }

                areaNo = nextAreaNo;
                areaNoRef.current = nextAreaNo;
            }

            const cacheKey = `${areaNo}:${targetDateType}`;
            const cachedItem = dashboardCacheRef.current[cacheKey];
            const now = Date.now();
            const isCacheValid =
                cachedItem !== undefined &&
                now - cachedItem.savedAt < DASHBOARD_CACHE_TTL_MS;

            if (!options?.forceRefresh && isCacheValid) {
                updateDashboard(cachedItem.data);
                return;
            }

            if (cachedItem && !isCacheValid) {
                delete dashboardCacheRef.current[cacheKey];
            }

            const data = await getHomeDashboard(
                areaNo,
                targetDateType,
                {
                    signal: abortController.signal,
                }
            );

            if (abortController.signal.aborted || latestRequestIdRef.current !== requestId) {
                return;
            }

            dashboardCacheRef.current[cacheKey] = {
                data,
                savedAt: Date.now(),
            };
            updateDashboard(data);
        } catch (error) {
            if (isAbortError(error)) {
                return;
            }

            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            if (abortControllerRef.current === abortController) {
                setIsInitialLoading(false);
                setIsRefreshingDashboard(false);
                abortControllerRef.current = null;
            }
        }
    }, [updateDashboard]);

    //Home 화면에 들어오면 최신 설정을 확인하고 대시보드 로드
    useFocusEffect(
        useCallback(() => {
            loadDashboard(dateTypeRef.current, { forceSettingsRefresh: true });

            return () => {
                abortControllerRef.current?.abort();
            };
        }, [loadDashboard])
    );

    function handleChangeDateType(nextDateType: HomeDateType) {
        if (nextDateType === dateTypeRef.current) {
            return;
        }

        dateTypeRef.current = nextDateType;
        setDateType(nextDateType);
        loadDashboard(nextDateType);
    }

    function handleRefresh() {
        loadDashboard(dateTypeRef.current, {
            forceRefresh: true,
            forceSettingsRefresh: true,
        });
    }

    if (isInitialLoading && dashboard === null) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <ActivityIndicator size="large" />
                    <Text style={styles.loadingText}>홈 데이터를 불러오는 중입니다...</Text>
                </View>
            </ScreenContainer>
        );
    }

    if (dashboard === null) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <Text style={styles.errorTitle}>오류 발생</Text>
                    <Text style={styles.errorMessage}>
                        {errorMessage ?? "알 수 없는 오류가 발생했습니다."}
                    </Text>

                    <Pressable style={styles.retryButton} onPress={handleRefresh}>
                        <Text style={styles.retryButtonText}>다시 시도</Text>
                    </Pressable>
                </View>
            </ScreenContainer>
        );
    }

    const theme = dashboard.background.theme;
    const textColor = getThemeTextColor(theme);
    const subTextColor = getThemeSubTextColor(theme);
    const cardBackgroundColor = getCardBackgroundColor(theme);
    const formattedCurrentDateTime = formatDateTime(currentDateTime.toISOString());
    const [headerDateText, headerTimeSuffix] = formattedCurrentDateTime.split(" 오");
    const headerTimeText = headerTimeSuffix
        ? `오${headerTimeSuffix}`
        : formattedCurrentDateTime;

    return (
        <ScreenContainer style={{ backgroundColor: dashboard.background.color }}>
            <ScrollView
                style={[
                    styles.container,
                    { backgroundColor: dashboard.background.color },
                ]}
                contentContainerStyle={styles.content}
                refreshControl={        //당겨서 새로고침
                    <RefreshControl
                        refreshing={isRefreshingDashboard}
                        onRefresh={handleRefresh}
                        tintColor="#3182F6"
                        colors={["#3182F6"]}
                    />
                }
            >

                {/* 날짜 및 시간 */}
                <View>
                    {/* 앞부분(날짜)만 가져와서 출력 */}
                    <Text style={[styles.headerDate, { color: subTextColor }]}>
                        {headerDateText}
                    </Text>

                    {/* 뒷부분(오전/오후 시간)만 가져와서 출력 */}
                    <Text style={[styles.headerTime, { color: textColor }]}>
                        {headerTimeText}
                    </Text>
                </View>

                {/* 위치, 누르면 변경 화면 이동 */}
                <Pressable
                    onPress={() => router.push("/location-settings")}
                    style={styles.locationCard}
                >
                    {/* 지도 이모티콘 */}
                    <FontAwesome name="map-marker" size={20} color="#000080" style={{ marginRight: 6 }} />
                    {/* 위치 */}
                    <Text style={styles.locationName}>
                        {dashboard.location.name}
                    </Text>
                </Pressable>

                <View style={[styles.dateSelectorCard, { backgroundColor: cardBackgroundColor }]}>
                    <View style={styles.dateButtonRow}>
                        <DateButton
                            label="오늘"
                            active={dateType === "TODAY"}
                            onPress={() => handleChangeDateType("TODAY")}
                        />
                        <DateButton
                            label="내일"
                            active={dateType === "TOMORROW"}
                            onPress={() => handleChangeDateType("TOMORROW")}
                        />
                        <DateButton
                            label="모레"
                            active={dateType === "DAY_AFTER_TOMORROW"}
                            onPress={() => handleChangeDateType("DAY_AFTER_TOMORROW")}
                        />
                    </View>

                    {isRefreshingDashboard && (
                        <View style={styles.updatingRow}>
                            <ActivityIndicator size="small" />
                            <Text style={styles.updatingText}>최신 정보를 불러오는 중입니다</Text>
                        </View>
                    )}
                </View>

                {errorMessage && (
                    <Text style={styles.inlineErrorText}>{errorMessage}</Text>
                )}

                <View style={[styles.tableCard, { backgroundColor: cardBackgroundColor }]}>
                    <DashboardTable dashboard={dashboard} />
                </View>

                <View style={[styles.adviceCard, { backgroundColor: cardBackgroundColor }]}>
                    <Text style={styles.adviceBadge}>
                        {convertSeverityText(dashboard.advice.severity)}
                    </Text>
                    <Text style={[styles.adviceTitle, { color: textColor }]}>
                        {dashboard.advice.title}
                    </Text>
                    <Text style={[styles.adviceMessage, { color: subTextColor }]}>
                        {dashboard.advice.message}
                    </Text>
                </View>
            </ScrollView>
        </ScreenContainer >
    );
}

function isAbortError(error: unknown) {
    return error instanceof Error && error.name === "AbortError";
}

function DateButton({
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
            style={[styles.dateButton, active && styles.dateButtonActive]}
            onPress={onPress}
            disabled={active}
        >
            <Text
                style={[
                    styles.dateButtonText,
                    active && styles.dateButtonTextActive,
                ]}
            >
                {label}
            </Text>
        </Pressable>
    );
}

function DashboardTable({
    dashboard,
}: {
    dashboard: HomeDashboardResponse;
}) {
    return (
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={styles.table}>
                <View style={styles.tableRow}>
                    <View style={[styles.rowHeaderCell, styles.headerCell]}>
                        <Text style={styles.headerText}>구분</Text>
                    </View>

                    {dashboard.table.timeSlots.map((slot) => (
                        <View
                            key={`${slot.date}-${slot.time}`}
                            style={[
                                styles.timeHeaderCell,
                                styles.headerCell,
                                slot.current && styles.currentColumnHeader,
                            ]}
                        >
                            <Text style={styles.headerText}>{formatHourLabel(slot.time)}</Text>
                            {slot.current && <Text style={styles.currentBadge}>현재</Text>}
                        </View>
                    ))}
                </View>

                {dashboard.table.rows.map((row) => (
                    <DashboardTableRow
                        key={row.type}
                        row={row}
                        currentTimes={dashboard.table.timeSlots}
                    />
                ))}
            </View>
        </ScrollView>
    );
}

function DashboardTableRow({
    row,
    currentTimes,
}: {
    row: HomeTableRow;
    currentTimes: { date: string; time: string; current: boolean }[];
}) {
    return (
        <View style={styles.tableRow}>
            <View style={styles.rowHeaderCell}>
                <Text style={styles.rowHeaderText}>{convertRowLabel(row.label)}</Text>
            </View>

            {row.cells.map((cell) => {
                const current = currentTimes.some(
                    (slot) =>
                        slot.date === cell.date &&
                        slot.time === cell.time &&
                        slot.current
                );

                return (
                    <DashboardTableCell
                        key={`${row.type}-${cell.date}-${cell.time}`}
                        cell={cell}
                        current={current}
                        rowLabel={row.label}
                    />
                );
            })}
        </View>
    );
}

function DashboardTableCell({
    cell,
    current,
    rowLabel,
}: {
    cell: HomeTableCell;
    current: boolean;
    rowLabel: string;
}) {
    const shouldShowLevel =
        cell.level !== "UNKNOWN" &&
        cell.level !== "맑음" &&
        cell.level !== "구름많음" &&
        cell.level !== "흐림" &&
        cell.level !== "비" &&
        cell.level !== "비/눈" &&
        cell.level !== "눈" &&
        cell.level !== "소나기" &&
        cell.level !== "강수";

    const levelText = formatLevelText(cell.level);
    const shouldShowSubText =
        cell.subText.trim().length > 0 &&
        cell.subText.trim() !== levelText;
    const isPrecipitationCell = cell.mainText.includes("%");
    const isUvCell = rowLabel === "자외선 지수";
    const uvTextColor = isUvCell ? getUvLevelColor(cell.level) : null;
    const mainTextStyle = [
        styles.cellMainText,
        isPrecipitationCell && styles.precipitationMainText,
        uvTextColor && { color: uvTextColor },
    ];

    return (
        <View style={[styles.dataCell, current && styles.currentColumnCell]}>
            <Text style={mainTextStyle}>{formatCellMainText(cell)}</Text>

            {shouldShowSubText && (
                <Text style={styles.cellSubText}>{cell.subText}</Text>
            )}

            {shouldShowLevel && levelText.length > 0 && (
                <Text style={[styles.cellLevelText, uvTextColor && { color: uvTextColor }]}>
                    {levelText}
                </Text>
            )}
        </View>
    );
}

function formatCellMainText(cell: HomeTableCell) {
    if (cell.mainText === "맑음") {
        return "☀️";
    }

    if (cell.mainText === "구름많음") {
        return "⛅";
    }

    if (cell.mainText === "흐림") {
        return "☁️";
    }

    if (cell.mainText === "비") {
        return "🌧️";
    }

    if (cell.mainText === "비/눈") {
        return "🌨️";
    }

    if (cell.mainText === "눈") {
        return "❄️";
    }

    if (cell.mainText === "소나기") {
        return "🌦️";
    }

    if (cell.mainText === "강수") {
        return "🌧️";
    }

    return cell.mainText;
}

function formatLevelText(level: string) {
    switch (level) {
        case "LOW":
            return "낮음";
        case "MODERATE":
            return "보통";
        case "HIGH":
            return "높음";
        case "VERY_HIGH":
            return "매우 높음";
        case "EXTREME":
            return "위험";
        case "SUNNY":
            return "맑음";
        default:
            return "";
    }
}

function convertRowLabel(label: string) {
    if (label === "날씨 및 온도") {
        return "🌤️\n날씨\n온도";
    }

    if (label === "자외선 지수") {
        return "☀️\n자외선";
    }

    if (label === "강수확률") {
        return "🌫️\n강수\n확률";
    }

    return label;
}

function convertSeverityText(severity: string) {
    switch (severity) {
        case "DANGER":
            return "위험";
        case "WARNING":
            return "주의";
        case "INFO":
            return "정보";
        case "NORMAL":
            return "안내";
        default:
            return severity;
    }
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

function formatHourLabel(value: string) {
    const hour = Number(value.split(":")[0]);

    if (Number.isNaN(hour)) {
        return value;
    }

    if (hour === 0) {
        return "0 AM";
    }

    if (hour < 12) {
        return `${hour} AM`;
    }

    if (hour === 12) {
        return "12 PM";
    }

    return `${hour - 12} PM`;
}

function getUvLevelColor(level: string) {
    switch (level) {
        case "LOW":
        case "낮음":
            return "#6B7280";
        case "MODERATE":
        case "보통":
            return "#D97706";
        case "HIGH":
        case "높음":
            return "#EA580C";
        case "VERY_HIGH":
        case "매우 높음":
            return "#DC2626";
        case "EXTREME":
        case "위험":
            return "#991B1B";
        default:
            return null;
    }
}

function getThemeTextColor(theme: string) {
    if (theme === "NIGHT") {
        return "#F9FAFB";
    }

    return "#111827";
}

function getThemeSubTextColor(theme: string) {
    if (theme === "NIGHT") {
        return "#D1D5DB";
    }

    return "#374151";
}

function getCardBackgroundColor(theme: string) {
    if (theme === "NIGHT") {
        return "rgba(31, 41, 55, 0.62)";
    }

    return "rgba(255, 255, 255, 0.58)";
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    content: {
        padding: 20,
        gap: 12,
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
    errorTitle: {
        fontSize: 22,
        fontWeight: "800",
        marginBottom: 8,
    },
    errorMessage: {
        fontSize: 15,
        color: "#555555",
        textAlign: "center",
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
        fontWeight: "800",
    },
    updatingRow: {
        marginTop: 8,
        flexDirection: "row",
        justifyContent: "center",
        alignItems: "center",
        gap: 6,
    },
    updatingText: {
        fontSize: 12,
        fontWeight: "700",
        color: "#374151",
    },
    inlineErrorText: {
        borderRadius: 12,
        paddingVertical: 10,
        paddingHorizontal: 12,
        backgroundColor: "rgba(254, 226, 226, 0.92)",
        color: "#B91C1C",
        fontSize: 13,
        fontWeight: "700",
        textAlign: "center",
    },
    headerDate: {
        fontSize: 14,
        fontWeight: "700",
        lineHeight: 18,
        marginBottom: 2,
    },
    headerTime: {
        fontSize: 32,
        fontWeight: "900",
        lineHeight: 34,
    },
    locationCard: {
        flexDirection: "row",
        alignSelf: "flex-start",
        backgroundColor: "#F5F5F5",
        paddingVertical: 10,
        paddingHorizontal: 16,
        borderRadius: 50,
        // 부드러운 그림자 효과
        elevation: 3,
        shadowColor: "#000000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.08,
        shadowRadius: 4,
    },
    locationName: {
        fontSize: 16,
        fontWeight: "700",
        lineHeight: 24,
        color: "#000080",
    },
    subText: {
        marginTop: 4,
        fontSize: 12,
        color: "#6B7280",
    },
    linkText: {
        marginTop: 10,
        fontSize: 13,
        color: "#2563EB",
        fontWeight: "700",
    },
    dateButtonRow: {
        flexDirection: "row",
        justifyContent: "center",
        gap: 8,
    },
    dateButton: {
        width: 52,
        minHeight: 44,
        paddingVertical: 10,
        borderRadius: 16,
        backgroundColor: "transparent",
        justifyContent: "center",
        alignItems: "center",
    },
    dateButtonActive: {
        backgroundColor: "rgba(255, 255, 255, 0.96)",
    },
    dateButtonText: {
        color: "#374151",
        fontWeight: "900",
        fontSize: 13,
    },
    dateButtonTextActive: {
        color: "#111827",
    },
    dateSelectorCard: {
        alignSelf: "center",
        padding: 4,
        borderRadius: 18,
    },
    tableCard: {
        padding: 8,
        borderRadius: 18,
    },
    table: {
        borderRadius: 14,
        overflow: "hidden",
        backgroundColor: "rgba(255, 255, 255, 0.28)",
    },
    tableRow: {
        flexDirection: "row",
    },
    headerCell: {
        backgroundColor: "rgba(255, 255, 255, 0.65)",
    },
    rowHeaderCell: {
        width: 52,
        minHeight: 42,
        padding: 2,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "rgba(255, 255, 255, 0.42)",
    },
    timeHeaderCell: {
        width: 54,
        minHeight: 42,
        padding: 2,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "rgba(255, 255, 255, 0.42)",
    },
    dataCell: {
        width: 54,
        minHeight: 48,
        padding: 2,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "rgba(255, 255, 255, 0.34)",
    },
    currentColumnHeader: {
        backgroundColor: "rgba(219, 234, 254, 0.95)",
        borderWidth: 2,
        borderColor: "#2563EB",
        borderRadius: 10,
    },
    currentColumnCell: {
        backgroundColor: "rgba(239, 246, 255, 0.95)",
        borderWidth: 2,
        borderColor: "#2563EB",
        borderRadius: 10,
    },
    headerText: {
        fontSize: 11,
        fontWeight: "900",
        color: "#111827",
    },
    currentBadge: {
        marginTop: 3,
        paddingHorizontal: 5,
        paddingVertical: 1,
        borderRadius: 999,
        backgroundColor: "#2563EB",
        color: "#FFFFFF",
        fontSize: 9,
        fontWeight: "900",
        overflow: "hidden",
    },
    rowHeaderText: {
        fontSize: 10,
        fontWeight: "900",
        color: "#111827",
        textAlign: "center",
        lineHeight: 13,
    },
    cellMainText: {
        fontSize: 16,
        fontWeight: "900",
        color: "#111827",
        textAlign: "center",
    },
    cellSubText: {
        marginTop: 1,
        fontSize: 9,
        color: "#6B7280",
        textAlign: "center",
    },
    cellLevelText: {
        marginTop: 1,
        fontSize: 9,
        color: "#9CA3AF",
        textAlign: "center",
    },
    precipitationMainText: {
        fontSize: 14,
    },
    adviceCard: {
        padding: 18,
        borderRadius: 18,
    },
    adviceBadge: {
        alignSelf: "flex-start",
        backgroundColor: "#111827",
        color: "#FFFFFF",
        paddingVertical: 4,
        paddingHorizontal: 10,
        borderRadius: 999,
        fontSize: 12,
        fontWeight: "900",
        overflow: "hidden",
        marginBottom: 10,
    },
    adviceTitle: {
        fontSize: 20,
        fontWeight: "900",
        color: "#111827",
    },
    adviceMessage: {
        marginTop: 8,
        fontSize: 14,
        lineHeight: 22,
        color: "#374151",
    },
});