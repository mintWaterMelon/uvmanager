import { ScrollView, StyleSheet, Text, View } from "react-native";

import ScreenContainer from "../components/ScreenContainer";

export default function UvInfoScreen() {
    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <Text style={styles.title}>자외선 정보</Text>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>자외선차단지수란?</Text>
                    <Text style={styles.paragraph}>
                        자외선차단지수는 선크림에 표시되는 SPF와 PA를 의미합니다.
                        SPF는 UVB 차단 정도를, PA는 UVA 차단 정도를 나타냅니다.
                        숫자가 높거나 + 기호가 많을수록 차단 효과가 커집니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>SPF와 PA의 의미</Text>
                    <Text style={styles.paragraph}>
                        SPF는 피부 화상이나 붉어짐을 유발하는 UVB를 차단하는 지표입니다.
                        PA는 피부 노화와 색소 침착에 영향을 주는 UVA를 차단하는 지표입니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>자외선지수 단계</Text>
                    <Text style={styles.paragraph}>
                        자외선지수는 0~2 낮음, 3~5 보통, 6~7 높음, 8~10 매우 높음,
                        11 이상 위험 단계로 구분됩니다. 자외선지수가 3 이상이면
                        야외활동 시 자외선 차단제를 포함한 보호 조치를 권장합니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>올바른 선크림 사용법</Text>
                    <Text style={styles.paragraph}>
                        외출 15~30분 전에 충분한 양을 바르고, 얼굴뿐 아니라 목, 팔,
                        다리 등 노출되는 부위에 꼼꼼히 바르세요. 땀이나 마찰로 지워질 수
                        있으므로 2~3시간 간격으로 덧바르는 것이 좋습니다.
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
        paddingBottom: 48,
    },
    title: {
        fontSize: 28,
        fontWeight: "800",
        marginBottom: 8,
    },
    card: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 16,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: "800",
        marginBottom: 10,
    },
    paragraph: {
        fontSize: 14,
        lineHeight: 22,
        color: "#444444",
    },
});