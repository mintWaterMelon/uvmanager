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

                    <Pressable style={styles.backButton} onPress={() => router.back()}>
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
                        <Text style={styles.sourceText}>
                            제공기관: 기상청
                        </Text>
                        <Text style={styles.sourceText}>
                            활용 목적: 지역별 자외선지수 조회
                        </Text>
                    </View>

                    <View style={styles.sourceBox}>
                        <Text style={styles.sourceTitle}>기상청 단기예보 조회서비스</Text>
                        <Text style={styles.sourceText}>
                            제공기관: 기상청
                        </Text>
                        <Text style={styles.sourceText}>
                            활용 목적: 날씨, 기온, 하늘상태, 강수형태 조회
                        </Text>
                    </View>

                    <View style={styles.sourceBox}>
                        <Text style={styles.sourceTitle}>기상청 대기정체지수 API</Text>
                        <Text style={styles.sourceText}>
                            제공기관: 기상청
                        </Text>
                        <Text style={styles.sourceText}>
                            활용 목적: 지역별 대기정체지수 조회
                        </Text>
                    </View>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>공공저작물 이용 안내</Text>

                    <Text style={styles.paragraph}>
                        공공데이터 및 공공저작물은 제공기관의 이용 조건에 따라 사용해야 합니다.
                        앱 배포 시에는 데이터 출처를 명확히 표시하고, 제공기관의 이용 약관을
                        확인해야 합니다.
                    </Text>

                    <Text style={styles.noticeText}>
                        공공누리 제1유형 또는 공공데이터포털 이용 조건에 해당하는 경우,
                        출처 표시가 필요할 수 있습니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>제3자 권리</Text>

                    <Text style={styles.paragraph}>
                        앱에서 사용하는 아이콘, 이미지, 폰트, 라이브러리 등에는 별도의
                        저작권 또는 라이센스 조건이 있을 수 있습니다.
                    </Text>

                    <Text style={styles.paragraph}>
                        추후 앱에 외부 이미지, 아이콘, 지도, 폰트 등을 추가하는 경우 해당
                        라이센스를 이 화면 또는 별도 문서에 표시해야 합니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>오픈소스 라이브러리</Text>

                    <Text style={styles.paragraph}>
                        이 앱은 React Native, Expo 및 관련 오픈소스 라이브러리를 사용할 수
                        있습니다.
                    </Text>

                    <Text style={styles.paragraph}>
                        정식 배포 전 package.json의 의존성을 기준으로 오픈소스 라이센스
                        목록을 정리하는 것을 권장합니다.
                    </Text>
                </View>

                <View style={styles.card}>
                    <Text style={styles.sectionTitle}>면책 안내</Text>

                    <Text style={styles.paragraph}>
                        이 앱에서 제공하는 자외선, 날씨, 대기정체 정보는 사용자의 편의를 위한
                        참고 정보입니다.
                    </Text>

                    <Text style={styles.paragraph}>
                        실제 외출, 건강 관리, 안전 판단이 필요한 경우 기상청 공식 정보와
                        관련 기관의 안내를 함께 확인하세요.
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
    noticeText: {
        marginTop: 12,
        padding: 12,
        borderRadius: 12,
        backgroundColor: "#EFF6FF",
        color: "#1D4ED8",
        fontSize: 13,
        lineHeight: 20,
        fontWeight: "700",
    },
});