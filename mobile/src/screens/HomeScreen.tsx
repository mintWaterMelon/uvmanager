import { useCallback, useState } from "react";
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    View,
} from "react-native";
import { useFocusEffect, useRouter } from "expo-router";

import {
    getHomeDashboard,
    HomeDashboardResponse,
    HomeDateType,
    HomeMode,
    HomeTableCell,
    HomeTableRow,
} from "../api/homeApi";
import { getSettings } from "../api/settingApi";
import ScreenContainer from "../components/ScreenContainer";

export default function HomeScreen() {
    const router = useRouter();

    const [dashboard, setDashboard] = useState<HomeDashboardResponse | null>(null);
    const [dateType, setDateType] = useState<HomeDateType>("TODAY");
    const [mode, setMode] = useState<HomeMode>("DAY");

    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useFocusEffect(
        useCallback(() => {
            loadDashboard();
        }, [dateType, mode])
    );

    async function loadDashboard() {
        try {
            setLoading(true);
            setErrorMessage(null);

            const settings = await getSettings();

            const data = await getHomeDashboard(
                settings.defaultAreaNo,
                dateType,
                mode
            );

            setDashboard(data);
        } catch (error) {
            console.error(error);
            setErrorMessage("홈 화면 데이터를 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    function handleChangeDateType(nextDateType: HomeDateType) {
        setDateType(nextDateType);
    }

    function handleToggleMode() {
        setMode((prev) => (prev === "DAY" ? "NIGHT" : "DAY"));
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

    return (
        <ScreenContainer style={{ backgroundColor: dashboard.background.color }}>
            <ScrollView
                style={[
                    styles.container,
                    { backgroundColor: dashboard.background.color },
                ]}
                contentContainerStyle={styles.content}
            >
                <View style={styles.headerRow}>
                    <View>
                        <Text style={styles.title}>UV Alert</Text>
                        <Text style={styles.headerSubText}>
                            {formatDateTime(dashboard.currentTime)}
                        </Text>
                    </View>

                    <Pressable style={styles.refreshButton} onPress={loadDashboard}>
                        <Text style={styles.refreshButtonText}>새로고침</Text>
                    </Pressable>
                </View>

                <Pressable
                    style={styles.locationCard}
                    onPress={() => router.push("/location-settings")}
                >
                    <Text style={styles.label}>현재 위치</Text>
                    <Text style={styles.locationName}>{dashboard.location.name}</Text>
                    <Text style={styles.subText}>지역 코드: {dashboard.location.areaNo}</Text>
                    <Text style={styles.linkText}>눌러서 위치 변경하기</Text>
                </Pressable>

                <View style={styles.selectorCard}>
                    <Text style={styles.selectorTitle}>날짜 선택</Text>

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

                    <View style={styles.modeSelectorBox}>
                        <View>
                            <Text style={styles.selectorTitle}>시간대 선택</Text>
                            <Text style={styles.selectorDescription}>
                                {mode === "DAY"
                                    ? "06시부터 18시까지의 낮 정보를 보여줍니다."
                                    : "18시부터 다음날 06시까지의 밤 정보를 보여줍니다."}
                            </Text>
                        </View>

                        <Pressable
                            style={[
                                styles.modeToggleButton,
                                mode === "NIGHT" && styles.modeToggleButtonNight,
                            ]}
                            onPress={handleToggleMode}
                        >
                            <Text style={styles.modeToggleText}>
                                {mode === "DAY" ? "☀️ 낮" : "🌙 밤"}
                            </Text>
                        </Pressable>
                    </View>
                </View>

                <View style={styles.tableCard}>
                    <Text style={styles.sectionTitle}>시간대별 정보</Text>
                    <Text style={styles.tableHint}>
                        현재 시간대는 굵은 테두리로 표시됩니다.
                    </Text>

                    <DashboardTable dashboard={dashboard} />
                </View>

                <View style={styles.adviceCard}>
                    <Text style={styles.adviceBadge}>
                        {convertSeverityText(dashboard.advice.severity)}
                    </Text>
                    <Text style={styles.adviceTitle}>{dashboard.advice.title}</Text>
                    <Text style={styles.adviceMessage}>{dashboard.advice.message}</Text>
                </View>

                <View style={styles.backgroundCard}>
                    <Text style={styles.label}>배경 테마</Text>
                    <Text style={styles.value}>{dashboard.background.theme}</Text>
                    <Text style={styles.subText}>{dashboard.background.description}</Text>
                </View>
            </ScrollView>
        </ScreenContainer>
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

    if (label === "대기정체지수") {
        return "🌫️\n미세\n먼지";
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
    headerRow: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "flex-start",
    },
    title: {
        fontSize: 30,
        fontWeight: "900",
        color: "#111827",
    },
    headerSubText: {
        marginTop: 4,
        fontSize: 13,
        color: "#374151",
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
        fontWeight: "800",
    },
    locationCard: {
        backgroundColor: "rgba(255, 255, 255, 0.92)",
        padding: 16,
        borderRadius: 18,
    },
    label: {
        fontSize: 14,
        color: "#6B7280",
        marginBottom: 8,
    },
    locationName: {
        fontSize: 22,
        fontWeight: "900",
        color: "#111827",
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
        backgroundColor: "rgba(255, 255, 255, 0.92)",
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
    modeSelectorBox: {
        paddingTop: 12,
        borderTopWidth: 1,
        borderTopColor: "#E5E7EB",
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        gap: 12,
    },
    modeToggleButton: {
        minWidth: 84,
        backgroundColor: "#FBBF24",
        paddingVertical: 11,
        paddingHorizontal: 14,
        borderRadius: 999,
        alignItems: "center",
    },
    modeToggleButtonNight: {
        backgroundColor: "#111827",
    },
    modeToggleText: {
        color: "#FFFFFF",
        fontSize: 14,
        fontWeight: "900",
    },
    tableCard: {
        backgroundColor: "rgba(255, 255, 255, 0.82)",
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
        backgroundColor: "rgba(255, 255, 255, 0.95)",
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
        backgroundColor: "rgba(255, 255, 255, 0.88)",
        padding: 16,
        borderRadius: 18,
    },
    value: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
    },
});