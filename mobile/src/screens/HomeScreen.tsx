import { useCallback, useState } from "react";
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

export default function HomeScreen() {
    //페이지 이동 제어 router 객체 생성
    const router = useRouter();

    //대쉬보드 데이터
    const [dashboard, setDashboard] = useState<HomeDashboardResponse | null>(null);
    const [dateType, setDateType] = useState<HomeDateType>("TODAY");

    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    //Home 화면 들어오면 대시보드 다시 로드
    useFocusEffect(
        useCallback(() => {
            loadDashboard();
        }, [dateType])
    );

    async function loadDashboard() {
        try {
            setLoading(true);       //로딩 화면
            setErrorMessage(null);  //에러 메세지 초기화

            const settings = await getSettings();

            const data = await getHomeDashboard(
                settings.defaultAreaNo,
                dateType
            );

            setDashboard(data);
        } catch (error) {
            logApiError(error);
            setErrorMessage(getApiErrorMessage(error));
        } finally {
            setLoading(false);
        }
    }

    function handleChangeDateType(nextDateType: HomeDateType) {
        setDateType(nextDateType);
    }

    if (loading && dashboard === null) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <ActivityIndicator size="large" />
                    <Text style={styles.loadingText}>홈 데이터를 불러오는 중입니다...</Text>
                </View>
            </ScreenContainer>
        );
    }

    if (errorMessage || dashboard === null) {
        return (
            <ScreenContainer>
                <View style={styles.centerContainer}>
                    <Text style={styles.errorTitle}>오류 발생</Text>
                    <Text style={styles.errorMessage}>
                        {errorMessage ?? "알 수 없는 오류가 발생했습니다."}
                    </Text>

                    <Pressable style={styles.retryButton} onPress={loadDashboard}>
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
    const softCardBackgroundColor = getSoftCardBackgroundColor(theme);

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
                        refreshing={loading}
                        onRefresh={loadDashboard}
                        tintColor="#3182F6"
                        colors={["#3182F6"]}
                    />
                }
            >

                {/* 날짜 및 시간 */}
                <View>
                    {/* 앞부분(날짜)만 가져와서 출력 */}
                    <Text style={[styles.headerDate, { color: subTextColor }]}>
                        {formatDateTime(dashboard.currentTime).split(" 오")[0]}
                    </Text>

                    {/* 뒷부분(오전/오후 시간)만 가져와서 출력 */}
                    <Text style={[styles.headerTime, { color: textColor }]}>
                        {"오" + formatDateTime(dashboard.currentTime).split(" 오")[1]}
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

                <View style={[styles.selectorCard, { backgroundColor: cardBackgroundColor }]}>
                    <Text style={[styles.selectorTitle, { color: textColor }]}>날짜 선택</Text>

                    <View style={styles.dateButtonRow}>
                        <DateButton
                            label="오늘"
                            emoji="📍"
                            active={dateType === "TODAY"}
                            onPress={() => handleChangeDateType("TODAY")}
                        />
                        <DateButton
                            label="내일"
                            emoji="➡️"
                            active={dateType === "TOMORROW"}
                            onPress={() => handleChangeDateType("TOMORROW")}
                        />
                        <DateButton
                            label="모레"
                            emoji="⏭️"
                            active={dateType === "DAY_AFTER_TOMORROW"}
                            onPress={() => handleChangeDateType("DAY_AFTER_TOMORROW")}
                        />
                    </View>

                    <View style={[styles.tableCard, { backgroundColor: cardBackgroundColor }]}>
                        <Text style={[styles.sectionTitle, { color: textColor }]}>시간대별 정보</Text>
                        <Text style={[styles.tableHint, { color: subTextColor }]}>
                            현재 시간대는 굵은 테두리로 표시됩니다.
                        </Text>

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

                    <View style={[styles.backgroundCard, { backgroundColor: softCardBackgroundColor }]}>
                        <Text style={[styles.value, { color: textColor }]}>
                            {dashboard.background.theme}
                        </Text>
                        <Text style={[styles.subText, { color: subTextColor }]}>
                            {dashboard.background.description}
                        </Text>
                    </View>
                </View>
            </ScrollView>
        </ScreenContainer >
    );
}

function DateButton({
    label,
    emoji,
    active,
    onPress,
}: {
    label: string;
    emoji: string;
    active: boolean;
    onPress: () => void;
}) {
    return (
        <Pressable
            style={[styles.dateButton, active && styles.dateButtonActive]}
            onPress={onPress}
        >
            <Text style={styles.dateButtonEmoji}>{emoji}</Text>
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
                            <Text style={styles.headerText}>{slot.time}</Text>
                            <Text style={styles.dateText}>{formatShortDate(slot.date)}</Text>
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
                    />
                );
            })}
        </View>
    );
}

function DashboardTableCell({
    cell,
    current,
}: {
    cell: HomeTableCell;
    current: boolean;
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

    return (
        <View style={[styles.dataCell, current && styles.currentColumnCell]}>
            <Text style={styles.cellMainText}>{formatCellMainText(cell)}</Text>
            <Text style={styles.cellSubText}>{cell.subText}</Text>

            {shouldShowLevel && (
                <Text style={styles.cellLevelText}>{formatLevelText(cell.level)}</Text>
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

function formatShortDate(value: string) {
    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleDateString("ko-KR", {
        month: "2-digit",
        day: "2-digit",
    });
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
        return "rgba(31, 41, 55, 0.92)";
    }

    return "rgba(255, 255, 255, 0.9)";
}

function getSoftCardBackgroundColor(theme: string) {
    if (theme === "NIGHT") {
        return "rgba(17, 24, 39, 0.88)";
    }

    return "rgba(255, 255, 255, 0.82)";
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
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
        gap: 8,
    },
    dateButton: {
        flex: 1,
        paddingVertical: 12,
        borderRadius: 14,
        backgroundColor: "#F3F4F6",
        alignItems: "center",
        gap: 4,
    },
    dateButtonActive: {
        backgroundColor: "#2563EB",
    },
    dateButtonEmoji: {
        fontSize: 18,
    },
    dateButtonText: {
        color: "#374151",
        fontWeight: "900",
        fontSize: 13,
    },
    dateButtonTextActive: {
        color: "#FFFFFF",
    },
    selectorCard: {
        padding: 14,
        borderRadius: 18,
        gap: 14,
    },
    selectorTitle: {
        fontSize: 15,
        fontWeight: "900",
        color: "#111827",
    },
    selectorDescription: {
        marginTop: 4,
        fontSize: 12,
        lineHeight: 18,
        color: "#6B7280",
    },
    tableCard: {
        padding: 12,
        borderRadius: 18,
    },
    sectionTitle: {
        fontSize: 20,
        fontWeight: "900",
        color: "#111827",
    },
    tableHint: {
        marginTop: 4,
        marginBottom: 12,
        fontSize: 12,
        color: "#6B7280",
    },
    table: {
        borderRadius: 14,
        overflow: "hidden",
        backgroundColor: "rgba(255, 255, 255, 0.72)",
    },
    tableRow: {
        flexDirection: "row",
    },
    headerCell: {
        backgroundColor: "rgba(255, 255, 255, 0.65)",
    },
    rowHeaderCell: {
        width: 62,
        minHeight: 52,
        padding: 4,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "rgba(255, 255, 255, 0.65)",
    },
    timeHeaderCell: {
        width: 64,
        minHeight: 52,
        padding: 4,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "rgba(255, 255, 255, 0.65)",
    },
    dataCell: {
        width: 64,
        minHeight: 58,
        padding: 4,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "rgba(255, 255, 255, 0.58)",
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
        fontSize: 12,
        fontWeight: "900",
        color: "#111827",
    },
    dateText: {
        marginTop: 1,
        fontSize: 9,
        color: "#6B7280",
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
        fontSize: 11,
        fontWeight: "900",
        color: "#111827",
        textAlign: "center",
        lineHeight: 15,
    },
    cellMainText: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
        textAlign: "center",
    },
    cellSubText: {
        marginTop: 2,
        fontSize: 10,
        color: "#6B7280",
        textAlign: "center",
    },
    cellLevelText: {
        marginTop: 1,
        fontSize: 9,
        color: "#9CA3AF",
        textAlign: "center",
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
    backgroundCard: {
        padding: 16,
        borderRadius: 18,
    },
    value: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
    },
});