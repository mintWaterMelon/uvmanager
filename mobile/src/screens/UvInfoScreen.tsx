import { ReactNode, useState } from "react";
import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";

import ScreenContainer from "../components/ScreenContainer";

const uvLevelColors = {
    low: "#16A34A",
    moderate: "#CA8A04",
    high: "#F97316",
    veryHigh: "#EA580C",
    extreme: "#DC2626",
};

const spfLevelColors = {
    spf15: "#F97316",
    spf30: "#EF4444",
    spf50: "#DC2626",
};

const paLevelColors = {
    pa1: "#F97316",
    pa2: "#EF4444",
    pa3: "#DC2626",
    pa4: "#B91C1C",
};

export default function UvInfoScreen() {
    const [spfExpanded, setSpfExpanded] = useState(false);
    const [paExpanded, setPaExpanded] = useState(false);

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <View style={styles.headerCard}>
                    <Text style={styles.headerIcon}>☀️</Text>
                    <Text style={styles.title}>자외선 정보</Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>자외선의 종류</Text>

                    <UvTypeRow
                        type="UV-A"
                        color="#F97316"
                        description="피부 깊숙이 도달하기 쉬워 피부 노화와 색소 침착에 영향을 줄 수 있습니다."
                    />
                    <UvTypeRow
                        type="UV-B"
                        color="#EF4444"
                        description="피부 표면에 영향을 주며 화상, 붉어짐과 관련이 큽니다."
                    />
                    <UvTypeRow
                        type="UV-C"
                        color="#DC2626"
                        description="에너지가 가장 강하지만 대부분 오존층과 대기에서 흡수되어 지표면에는 거의 도달하지 않습니다."
                    />
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>SPF와 PA는 무엇인가요?</Text>

                    <ProtectionAccordion
                        badge="SPF"
                        title="UV-B 차단 지표"
                        description="피부 화상이나 붉어짐을 유발하는 UV-B 차단 정도를 나타냅니다."
                        expanded={spfExpanded}
                        onPress={() => setSpfExpanded((prev) => !prev)}
                        cardStyle={styles.spfCard}
                        badgeStyle={styles.spfBadge}
                        arrowBackgroundColor="#FEE2E2"
                    >
                        <ProtectionLevelRow
                            title="SPF 15"
                            description="UVB 차단율 약 93%"
                            color={spfLevelColors.spf15}
                        />
                        <ProtectionLevelRow
                            title="SPF 30"
                            description="UVB 차단율 약 97%"
                            color={spfLevelColors.spf30}
                        />
                        <ProtectionLevelRow
                            title="SPF 50"
                            description="UVB 차단율 약 98%"
                            color={spfLevelColors.spf50}
                        />
                    </ProtectionAccordion>

                    <ProtectionAccordion
                        badge="PA"
                        title="UV-A 차단 지표"
                        description="피부 노화와 색소 침착에 영향을 주는 UV-A 차단 정도를 나타냅니다."
                        expanded={paExpanded}
                        onPress={() => setPaExpanded((prev) => !prev)}
                        cardStyle={styles.paCard}
                        badgeStyle={styles.paBadge}
                        arrowBackgroundColor="#FFEDD5"
                    >
                        <ProtectionLevelRow
                            title="PA+"
                            description="차단 효과 있음 · 기존 피부 대비 약 2~4배 보호"
                            color={paLevelColors.pa1}
                        />
                        <ProtectionLevelRow
                            title="PA++"
                            description="차단 효과가 꽤 높음 · 약 4~8배 보호"
                            color={paLevelColors.pa2}
                        />
                        <ProtectionLevelRow
                            title="PA+++"
                            description="차단 효과가 매우 높음 · 약 8~16배 보호"
                            color={paLevelColors.pa3}
                        />
                        <ProtectionLevelRow
                            title="PA++++"
                            description="차단 효과가 극히 높음 · 16배 이상 보호"
                            color={paLevelColors.pa4}
                        />
                    </ProtectionAccordion>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>자외선지수 단계</Text>

                    <UvLevelRow
                        level="0~2"
                        label="낮음"
                        color={uvLevelColors.low}
                        description="일상적인 외출은 부담이 적은 편입니다."
                    />
                    <UvLevelRow
                        level="3~5"
                        label="보통"
                        color={uvLevelColors.moderate}
                        description="장시간 야외활동 시 선크림 사용을 권장합니다."
                    />
                    <UvLevelRow
                        level="6~7"
                        label="높음"
                        color={uvLevelColors.high}
                        description="선크림, 모자, 선글라스를 함께 사용하는 것이 좋습니다."
                    />
                    <UvLevelRow
                        level="8~10"
                        label="매우 높음"
                        color={uvLevelColors.veryHigh}
                        description="외출 전 충분히 바르고 2~3시간마다 덧바르세요."
                    />
                    <UvLevelRow
                        level="11+"
                        label="위험"
                        color={uvLevelColors.extreme}
                        description="가능하면 한낮 외출을 피하는 것이 좋습니다."
                    />
                </View>

                <View style={[styles.card, styles.tipCard]}>
                    <View style={styles.tipTitleRow}>
                        <Text style={styles.tipBadge}>Tip</Text>
                        <Text style={styles.sectionTitleNoMargin}>올바른 선크림 사용법</Text>
                    </View>

                    <ChecklistItem text="외출 15~30분 전에 충분한 양을 바르세요." />
                    <ChecklistItem text="얼굴뿐 아니라 목, 팔, 다리 등 노출 부위에 바르세요." />
                    <ChecklistItem>
                        땀이나 마찰로 지워질 수 있으니 <Text style={styles.highlightText}>2~3시간</Text>마다 덧바르세요.
                    </ChecklistItem>
                    <ChecklistItem text="흐린 날에도 자외선은 존재하므로 매일 사용하는 것이 좋습니다." />
                </View>
            </ScrollView>
        </ScreenContainer>
    );
}

function UvTypeRow({
    type,
    description,
    color,
}: {
    type: string;
    description: string;
    color: string;
}) {
    return (
        <View style={styles.uvTypeRow}>
            <Text style={[styles.uvTypeTitle, { color }]}>{type}</Text>
            <Text style={styles.uvTypeDescription}>{description}</Text>
        </View>
    );
}

function ProtectionAccordion({
    badge,
    title,
    description,
    expanded,
    onPress,
    cardStyle,
    badgeStyle,
    arrowBackgroundColor,
    children,
}: {
    badge: string;
    title: string;
    description: string;
    expanded: boolean;
    onPress: () => void;
    cardStyle: object;
    badgeStyle: object;
    arrowBackgroundColor: string,
    children: ReactNode;
}) {
    return (
        <View style={[styles.protectionCard, cardStyle]}>
            <Pressable style={styles.protectionHeader} onPress={onPress}>
                <View style={[styles.protectionBadge, badgeStyle]}>
                    <Text style={styles.protectionBadgeText}>{badge}</Text>
                </View>

                <View style={styles.protectionTextBox}>
                    <Text style={styles.infoTitle}>{title}</Text>
                    <Text style={styles.protectionDescription}>{description}</Text>
                </View>

                <Text
                    style={[
                        styles.arrowIcon,
                        { backgroundColor: arrowBackgroundColor },
                    ]}
                >
                    {expanded ? "▲" : "▼"}
                </Text>
            </Pressable>

            {expanded && <View style={styles.protectionLevelBox}>{children}</View>}
        </View>
    );
}

function ProtectionLevelRow({
    title,
    description,
    color,
}: {
    title: string;
    description: string;
    color: string;
}) {
    return (
        <View style={styles.protectionLevelRow}>
            <Text style={[styles.protectionLevelTitle, { color }]}>{title}</Text>
            <Text style={styles.protectionLevelDescription}>{description}</Text>
        </View>
    );
}

function UvLevelRow({
    level,
    label,
    color,
    description,
}: {
    level: string;
    label: string;
    color: string;
    description: string;
}) {
    return (
        <View style={styles.levelRow}>
            <View style={styles.levelBadge}>
                <Text style={[styles.levelBadgeText, { color }]}>{level}</Text>
            </View>

            <View style={styles.levelTextBox}>
                <Text style={[styles.levelLabel, { color }]}>{label}</Text>
                <Text style={styles.levelDescription}>{description}</Text>
            </View>
        </View>
    );
}

function ChecklistItem({
    text,
    children,
}: {
    text?: string;
    children?: ReactNode;
}) {
    return (
        <View style={styles.checkRow}>
            <Text style={styles.checkIcon}>✓</Text>
            <Text style={styles.checkText}>{children ?? text}</Text>
        </View>
    );
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
    headerCard: {
        backgroundColor: "#FFF7ED",
        padding: 20,
        borderRadius: 22,
        flexDirection: "row",
        justifyContent: "flex-start",
        alignItems: "center",
        gap: 12,
    },
    headerIcon: {
        fontSize: 38,
    },
    title: {
        fontSize: 28,
        fontWeight: "900",
        color: "#111827",
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
        marginBottom: 14,
    },
    sectionTitleNoMargin: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
    },
    uvTypeRow: {
        marginTop: 5,
        paddingVertical: 10,
        paddingHorizontal: 12,
        borderRadius: 14,
        backgroundColor: "#F9FAFB",
    },
    uvTypeTitle: {
        fontSize: 16,
        fontWeight: "900",
        marginBottom: 4,
    },
    uvTypeDescription: {
        fontSize: 13,
        lineHeight: 19,
        color: "#4B5563",
    },
    protectionCard: {
        padding: 14,
        borderRadius: 16,
        marginBottom: 12,
    },
    spfCard: {
        backgroundColor: "#FEE2E2",
    },
    paCard: {
        backgroundColor: "#FFEDD5",
        marginTop: 2,
    },
    protectionHeader: {
        flexDirection: "row",
        alignItems: "center",
        gap: 12,
    },
    protectionBadge: {
        width: 48,
        height: 48,
        borderRadius: 16,
        justifyContent: "center",
        alignItems: "center",
    },
    spfBadge: {
        backgroundColor: "#DC2626",
    },
    paBadge: {
        backgroundColor: "#F97316",
    },
    protectionBadgeText: {
        color: "#FFFFFF",
        fontWeight: "900",
        fontSize: 14,
    },
    protectionTextBox: {
        flex: 1,
    },
    infoTitle: {
        fontSize: 15,
        fontWeight: "900",
        color: "#111827",
        marginBottom: 4,
    },
    protectionDescription: {
        fontSize: 13,
        lineHeight: 18,
        color: "#6B7280",
    },
    arrowIcon: {
        width: 28,
        height: 28,
        borderRadius: 14,
        color: "#111827",
        fontSize: 14,
        lineHeight: 28,
        fontWeight: "900",
        textAlign: "center",
        overflow: "hidden",
        marginTop: 6,
    },
    protectionLevelBox: {
        marginTop: 12,
        paddingTop: 4,
        borderTopWidth: 1,
        borderTopColor: "rgba(255, 255, 255, 0.9)",
    },
    protectionLevelRow: {
        paddingVertical: 8,
        borderBottomWidth: 1,
        borderBottomColor: "rgba(255, 255, 255, 0.85)",
    },
    protectionLevelTitle: {
        fontSize: 15,
        fontWeight: "900",
    },
    protectionLevelDescription: {
        marginTop: 3,
        fontSize: 13,
        lineHeight: 18,
        color: "#6B7280",
    },
    levelRow: {
        flexDirection: "row",
        alignItems: "center",
        gap: 12,
        paddingVertical: 10,
    },
    levelBadge: {
        width: 52,
        height: 38,
        borderRadius: 12,
        backgroundColor: "#F3F4F6",
        justifyContent: "center",
        alignItems: "center",
    },
    levelBadgeText: {
        fontSize: 13,
        fontWeight: "900",
    },
    levelTextBox: {
        flex: 1,
    },
    levelLabel: {
        fontSize: 15,
        fontWeight: "900",
    },
    levelDescription: {
        marginTop: 3,
        fontSize: 13,
        lineHeight: 18,
        color: "#6B7280",
    },
    tipCard: {
        backgroundColor: "#EFF6FF",
        borderWidth: 1,
        borderColor: "#BFDBFE",
    },
    tipTitleRow: {
        flexDirection: "row",
        alignItems: "center",
        gap: 8,
        marginBottom: 14,
    },
    tipBadge: {
        paddingVertical: 4,
        paddingHorizontal: 10,
        borderRadius: 999,
        backgroundColor: "#DBEAFE",
        color: "#1D4ED8",
        fontSize: 13,
        fontWeight: "900",
        overflow: "hidden",
    },
    checkRow: {
        flexDirection: "row",
        gap: 10,
        paddingVertical: 8,
    },
    checkIcon: {
        width: 22,
        height: 22,
        borderRadius: 11,
        backgroundColor: "#DCFCE7",
        color: "#059669",
        textAlign: "center",
        lineHeight: 22,
        fontWeight: "900",
        overflow: "hidden",
    },
    checkText: {
        flex: 1,
        fontSize: 14,
        lineHeight: 22,
        color: "#444444",
    },
    highlightText: {
        color: "#DC2626",
        fontWeight: "900",
    },
});
