import { ScrollView, StyleSheet, Text, View } from "react-native";

import ScreenContainer from "../components/ScreenContainer";

export default function UvInfoScreen() {
    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <View style={styles.headerCard}>
                    <Text style={styles.headerEmoji}>🧴</Text>
                    <Text style={styles.title}>자외선 정보</Text>
                    <Text style={styles.headerDescription}>
                        자외선지수와 선크림 표시를 이해하고, 상황에 맞게 피부를 보호하세요.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>SPF와 PA는 무엇인가요?</Text>

                    <View style={styles.infoRow}>
                        <View style={styles.badge}>
                            <Text style={styles.badgeText}>SPF</Text>
                        </View>
                        <View style={styles.infoTextBox}>
                            <Text style={styles.infoTitle}>UVB 차단 지표</Text>
                            <Text style={styles.paragraph}>
                                SPF는 피부 화상이나 붉어짐을 유발하는 UVB를 차단하는 정도를
                                나타냅니다. 숫자가 높을수록 차단 효과가 커집니다.
                            </Text>
                        </View>
                    </View>

                    <View style={styles.infoRow}>
                        <View style={styles.badge}>
                            <Text style={styles.badgeText}>PA</Text>
                        </View>
                        <View style={styles.infoTextBox}>
                            <Text style={styles.infoTitle}>UVA 차단 지표</Text>
                            <Text style={styles.paragraph}>
                                PA는 피부 노화와 색소 침착에 영향을 주는 UVA를 차단하는 정도를
                                나타냅니다. + 기호가 많을수록 차단력이 높습니다.
                            </Text>
                        </View>
                    </View>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>자외선지수 단계</Text>

                    <UvLevelRow level="0~2" label="낮음" description="일상적인 외출은 부담이 적은 편입니다." />
                    <UvLevelRow level="3~5" label="보통" description="장시간 야외활동 시 선크림 사용을 권장합니다." />
                    <UvLevelRow level="6~7" label="높음" description="선크림, 모자, 선글라스를 함께 사용하는 것이 좋습니다." />
                    <UvLevelRow level="8~10" label="매우 높음" description="외출 전 충분히 바르고 2~3시간마다 덧바르세요." />
                    <UvLevelRow level="11+" label="위험" description="가능하면 한낮 외출을 피하는 것이 좋습니다." />
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>올바른 선크림 사용법</Text>

                    <ChecklistItem text="외출 15~30분 전에 충분한 양을 바르세요." />
                    <ChecklistItem text="얼굴뿐 아니라 목, 팔, 다리 등 노출 부위에 바르세요." />
                    <ChecklistItem text="땀이나 마찰로 지워질 수 있으니 2~3시간마다 덧바르세요." />
                    <ChecklistItem text="흐린 날에도 자외선은 존재하므로 매일 사용하는 것이 좋습니다." />
                </View>

                <View style={styles.tipCard}>
                    <Text style={styles.tipTitle}>Tip</Text>
                    <Text style={styles.tipText}>
                        자외선지수가 3 이상이면 야외활동 시 자외선 차단제를 포함한 보호 조치를
                        준비하는 것이 좋습니다.
                    </Text>
                </View>
            </ScrollView>
        </ScreenContainer>
    );
}

function UvLevelRow({
    level,
    label,
    description,
}: {
    level: string;
    label: string;
    description: string;
}) {
    return (
        <View style={styles.levelRow}>
            <View style={styles.levelBadge}>
                <Text style={styles.levelBadgeText}>{level}</Text>
            </View>

            <View style={styles.levelTextBox}>
                <Text style={styles.levelLabel}>{label}</Text>
                <Text style={styles.levelDescription}>{description}</Text>
            </View>
        </View>
    );
}

function ChecklistItem({ text }: { text: string }) {
    return (
        <View style={styles.checkRow}>
            <Text style={styles.checkIcon}>✓</Text>
            <Text style={styles.checkText}>{text}</Text>
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
    },
    headerEmoji: {
        fontSize: 36,
        marginBottom: 8,
    },
    title: {
        fontSize: 28,
        fontWeight: "900",
        color: "#111827",
    },
    headerDescription: {
        marginTop: 8,
        fontSize: 14,
        lineHeight: 22,
        color: "#6B7280",
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
    infoRow: {
        flexDirection: "row",
        gap: 12,
        marginBottom: 14,
    },
    badge: {
        width: 48,
        height: 48,
        borderRadius: 16,
        backgroundColor: "#2563EB",
        justifyContent: "center",
        alignItems: "center",
    },
    badgeText: {
        color: "#FFFFFF",
        fontWeight: "900",
        fontSize: 14,
    },
    infoTextBox: {
        flex: 1,
    },
    infoTitle: {
        fontSize: 15,
        fontWeight: "900",
        color: "#111827",
        marginBottom: 4,
    },
    paragraph: {
        fontSize: 14,
        lineHeight: 21,
        color: "#444444",
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
        color: "#111827",
    },
    levelTextBox: {
        flex: 1,
    },
    levelLabel: {
        fontSize: 15,
        fontWeight: "900",
        color: "#111827",
    },
    levelDescription: {
        marginTop: 3,
        fontSize: 13,
        lineHeight: 18,
        color: "#6B7280",
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
    tipCard: {
        backgroundColor: "#EFF6FF",
        padding: 16,
        borderRadius: 18,
    },
    tipTitle: {
        fontSize: 16,
        fontWeight: "900",
        color: "#1D4ED8",
        marginBottom: 6,
    },
    tipText: {
        fontSize: 14,
        lineHeight: 22,
        color: "#1E40AF",
    },
});