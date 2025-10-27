import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { useRouter } from "expo-router";

import ScreenContainer from "../components/ScreenContainer";

export default function LicenseScreen() {
    const router = useRouter();

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <View style={styles.headerRow}>
                    <Text style={styles.title}>라이센스</Text>

                    <Pressable style={styles.backButton} onPress={() => router.replace("/settings")}>
                        <Text style={styles.backButtonText}>뒤로</Text>
                    </Pressable>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>공공데이터 출처</Text>

                    <Text style={styles.paragraph}>
                        이 앱은 기상청 및 공공데이터포털에서 제공하는 기상 데이터를 활용합니다.
                    </Text>

                    <View style={styles.sourceBox}>
                        <Text style={styles.sourceTitle}>기상청 자외선지수 API</Text>
                        <Text style={styles.sourceText}>제공기관: 기상청</Text>
                        <Text style={styles.sourceText}>활용 목적: 지역별 자외선지수 조회</Text>
                    </View>

                    <View style={styles.sourceBox}>
                        <Text style={styles.sourceTitle}>기상청 단기예보 조회서비스</Text>
                        <Text style={styles.sourceText}>제공기관: 기상청</Text>
                        <Text style={styles.sourceText}>
                            활용 목적: 날씨, 기온, 하늘상태, 강수형태 조회
                        </Text>
                    </View>
                </View>
            </ScrollView>
        </ScreenContainer>
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
    paragraph: {
        fontSize: 14,
        lineHeight: 22,
        color: "#444444",
        marginTop: 4,
    },
    sourceBox: {
        marginTop: 12,
        padding: 12,
        borderRadius: 12,
        backgroundColor: "#F9FAFB",
    },
    sourceTitle: {
        fontSize: 15,
        fontWeight: "900",
        color: "#111827",
        marginBottom: 6,
    },
    sourceText: {
        fontSize: 13,
        lineHeight: 20,
        color: "#555555",
    },
});
