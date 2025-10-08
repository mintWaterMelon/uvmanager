import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { useRouter } from "expo-router";

import ScreenContainer from "../components/ScreenContainer";

export default function SettingsScreen() {
    const router = useRouter();

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <Text style={styles.title}>설정</Text>

                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>앱 설정</Text>

                    <SettingMenuCard
                        icon="📍"
                        title="위치 설정"
                        description="현재 기본 위치를 검색하고 변경합니다."
                        onPress={() => router.push("/location-settings")}
                    />

                    <SettingMenuCard
                        icon="🔔"
                        title="푸시 설정"
                        description="자외선 알림, 미세먼지 알림, 알림 시간을 설정합니다."
                        onPress={() => router.push("/push-settings")}
                    />
                </View>

                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>도움말</Text>

                    <SettingMenuCard
                        icon="📘"
                        title="사용방법"
                        description="앱 사용 방법과 자외선 정보 확인 방법을 안내합니다."
                        onPress={() => router.push("/how-to-use")}
                    />

                    <SettingMenuCard
                        icon="⚖️"
                        title="라이센스"
                        description="공공데이터 출처와 제3자 저작권 표시를 확인합니다."
                        onPress={() => router.push("/license")}
                    />
                </View>

                <View style={styles.infoCard}>
                    <Text style={styles.infoTitle}>UV Alert</Text>
                    <Text style={styles.infoText}>
                        기상청 데이터를 기반으로 날씨, 자외선 지수, 대기정체지수를 확인하는 앱입니다.
                    </Text>
                    <Text style={styles.versionText}>Version 1.0.0</Text>
                </View>
            </ScrollView>
        </ScreenContainer>
    );
}

function SettingMenuCard({
    icon,
    title,
    description,
    onPress,
}: {
    icon: string;
    title: string;
    description: string;
    onPress: () => void;
}) {
    return (
        <Pressable style={styles.menuCard} onPress={onPress}>
            <View style={styles.iconBox}>
                <Text style={styles.iconText}>{icon}</Text>
            </View>

            <View style={styles.menuTextBox}>
                <Text style={styles.menuTitle}>{title}</Text>
                <Text style={styles.menuDescription}>{description}</Text>
            </View>

            <Text style={styles.arrowText}>›</Text>
        </Pressable>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#F7F8FA",
    },
    content: {
        padding: 20,
        gap: 20,
        paddingBottom: 56,
    },
    title: {
        fontSize: 28,
        fontWeight: "900",
        color: "#111827",
        marginBottom: 4,
    },
    section: {
        gap: 10,
    },
    sectionTitle: {
        fontSize: 15,
        fontWeight: "900",
        color: "#6B7280",
        marginLeft: 4,
        marginBottom: 2,
    },
    menuCard: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 18,
        flexDirection: "row",
        alignItems: "center",
    },
    iconBox: {
        width: 44,
        height: 44,
        borderRadius: 14,
        backgroundColor: "#F3F4F6",
        justifyContent: "center",
        alignItems: "center",
        marginRight: 12,
    },
    iconText: {
        fontSize: 22,
    },
    menuTextBox: {
        flex: 1,
    },
    menuTitle: {
        fontSize: 17,
        fontWeight: "900",
        color: "#111827",
    },
    menuDescription: {
        marginTop: 4,
        fontSize: 13,
        lineHeight: 19,
        color: "#6B7280",
    },
    arrowText: {
        fontSize: 30,
        color: "#9CA3AF",
        marginLeft: 8,
    },
    infoCard: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 18,
    },
    infoTitle: {
        fontSize: 18,
        fontWeight: "900",
        color: "#111827",
    },
    infoText: {
        marginTop: 8,
        fontSize: 13,
        lineHeight: 20,
        color: "#6B7280",
    },
    versionText: {
        marginTop: 12,
        fontSize: 12,
        color: "#9CA3AF",
        fontWeight: "700",
    },
});