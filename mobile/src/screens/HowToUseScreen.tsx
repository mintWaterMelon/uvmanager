import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { useRouter } from "expo-router";

import ScreenContainer from "../components/ScreenContainer";

export default function HowToUseScreen() {
    const router = useRouter();

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <View style={styles.headerRow}>
                    <Text style={styles.title}>사용방법</Text>

                    <Pressable style={styles.backButton} onPress={() => router.back()}>
                        <Text style={styles.backButtonText}>뒤로</Text>
                    </Pressable>
                </View>

                <View style={styles.card}>
                    <Text style={styles.stepNumber}>1</Text>
                    <Text style={styles.sectionTitle}>홈 화면 확인하기</Text>
                    <Text style={styles.paragraph}>
                        홈 화면에서는 현재 시간, 설정된 위치, 날씨 및 온도, 자외선 지수,
                        대기정체지수를 시간대별로 확인할 수 있습니다.
                    </Text>
                    <Text style={styles.paragraph}>
                        현재 시간에 해당하는 열은 굵은 테두리로 표시됩니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.stepNumber}>2</Text>
                    <Text style={styles.sectionTitle}>낮/밤 모드 바꾸기</Text>
                    <Text style={styles.paragraph}>
                        낮 모드는 06시, 09시, 12시, 15시, 18시 정보를 보여줍니다.
                    </Text>
                    <Text style={styles.paragraph}>
                        밤 모드는 18시, 21시, 00시, 03시, 06시 정보를 보여줍니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.stepNumber}>3</Text>
                    <Text style={styles.sectionTitle}>오늘/내일/모레 선택하기</Text>
                    <Text style={styles.paragraph}>
                        홈 화면의 오늘, 내일, 모레 버튼을 눌러 날짜별 예보 정보를 확인할 수
                        있습니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.stepNumber}>4</Text>
                    <Text style={styles.sectionTitle}>위치 설정하기</Text>
                    <Text style={styles.paragraph}>
                        설정 화면에서 위치 설정으로 이동하면 지역명 또는 행정구역 코드를
                        검색해 기본 위치를 변경할 수 있습니다.
                    </Text>
                    <Text style={styles.paragraph}>
                        변경한 위치는 홈 화면의 날씨와 자외선 정보에 반영됩니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.stepNumber}>5</Text>
                    <Text style={styles.sectionTitle}>푸시 설정하기</Text>
                    <Text style={styles.paragraph}>
                        푸시 설정 화면에서 자외선 알림, 미세먼지 알림, 알림 시간을 설정할 수
                        있습니다.
                    </Text>
                    <Text style={styles.paragraph}>
                        현재 단계에서는 설정 저장 기능을 중심으로 구현되어 있으며, 실제 푸시
                        발송 기능은 이후 단계에서 추가할 수 있습니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.stepNumber}>6</Text>
                    <Text style={styles.sectionTitle}>자외선 정보 보기</Text>
                    <Text style={styles.paragraph}>
                        자외선 탭에서는 SPF, PA, 자외선지수 단계, 올바른 선크림 사용법을
                        확인할 수 있습니다.
                    </Text>
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
    stepNumber: {
        width: 28,
        height: 28,
        borderRadius: 14,
        backgroundColor: "#2563EB",
        color: "#FFFFFF",
        textAlign: "center",
        lineHeight: 28,
        fontWeight: "900",
        marginBottom: 10,
        overflow: "hidden",
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
        marginBottom: 8,
    },
    paragraph: {
        fontSize: 14,
        lineHeight: 22,
        color: "#444444",
        marginTop: 4,
    },
});