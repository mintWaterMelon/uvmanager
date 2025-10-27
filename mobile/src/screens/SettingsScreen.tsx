import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { useRouter } from "expo-router";
import { SvgXml } from "react-native-svg";

import ScreenContainer from "../components/ScreenContainer";

const locationIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="512" height="512"><g id="_01_align_center"><path d="M255.104,512.171l-14.871-12.747C219.732,482.258,40.725,327.661,40.725,214.577c0-118.398,95.981-214.379,214.379-214.379s214.379,95.981,214.379,214.379c0,113.085-179.007,267.682-199.423,284.932L255.104,512.171z M255.104,46.553c-92.753,0.105-167.918,75.27-168.023,168.023c0,71.042,110.132,184.53,168.023,236.473c57.892-51.964,168.023-165.517,168.023-236.473C423.022,121.823,347.858,46.659,255.104,46.553z"/><path d="M255.104,299.555c-46.932,0-84.978-38.046-84.978-84.978s38.046-84.978,84.978-84.978s84.978,38.046,84.978,84.978S302.037,299.555,255.104,299.555z M255.104,172.087c-23.466,0-42.489,19.023-42.489,42.489s19.023,42.489,42.489,42.489s42.489-19.023,42.489-42.489S278.571,172.087,255.104,172.087z"/></g></svg>`;

const bellIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="512" height="512"><g id="_01_align_center" data-name="01 align center"><path d="M23.259,16.2l-2.6-9.371A9.321,9.321,0,0,0,2.576,7.3L.565,16.35A3,3,0,0,0,3.493,20H7.1a5,5,0,0,0,9.8,0h3.47a3,3,0,0,0,2.89-3.8ZM12,22a3,3,0,0,1-2.816-2h5.632A3,3,0,0,1,12,22Zm9.165-4.395a.993.993,0,0,1-.8.395H3.493a1,1,0,0,1-.976-1.217l2.011-9.05a7.321,7.321,0,0,1,14.2-.372l2.6,9.371A.993.993,0,0,1,21.165,17.605Z"/></g></svg>`;

const interrogationIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="512" height="512"><g id="_01_align_center" data-name="01 align center"><path d="M12,24A12,12,0,1,1,24,12,12.013,12.013,0,0,1,12,24ZM12,2A10,10,0,1,0,22,12,10.011,10.011,0,0,0,12,2Z"/><path d="M13,15H11v-.743a3.954,3.954,0,0,1,1.964-3.5,2,2,0,0,0,1-2.125,2.024,2.024,0,0,0-1.6-1.595A2,2,0,0,0,10,9H8a4,4,0,1,1,5.93,3.505A1.982,1.982,0,0,0,13,14.257Z"/><rect x="11" y="17" width="2" height="2"/></g></svg>`;

const exclamationIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="512" height="512"><path d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm0,22A10,10,0,1,1,22,12,10.011,10.011,0,0,1,12,22Z"/><path d="M12,5a1,1,0,0,0-1,1v8a1,1,0,0,0,2,0V6A1,1,0,0,0,12,5Z"/><rect x="11" y="17" width="2" height="2" rx="1"/></svg>`;

export default function SettingsScreen() {
    const router = useRouter();

    return (
        <ScreenContainer>
            <ScrollView style={styles.container} contentContainerStyle={styles.content}>
                <Text style={styles.title}>설정</Text>

                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>앱 설정</Text>

                    <SettingMenuCard
                        iconXml={locationIconXml}
                        iconColor="#111827"
                        title="위치 설정"
                        description="현재 기본 위치를 검색하고 변경합니다."
                        onPress={() =>
                            router.push({
                                pathname: "/location-settings",
                                params: { from: "settings" },
                            })
                        }
                    />

                    <SettingMenuCard
                        iconXml={bellIconXml}
                        iconColor="#111827"
                        title="푸시 설정"
                        description="자외선 알림과 알림 시간을 설정합니다."
                        onPress={() => router.push("/push-settings")}
                    />
                </View>

                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>도움말</Text>

                    <SettingMenuCard
                        iconXml={interrogationIconXml}
                        iconColor="#F97316"
                        title="사용방법"
                        description="앱 사용 방법과 자외선 정보 확인 방법을 안내합니다."
                        onPress={() => router.push("/how-to-use")}
                    />

                    <SettingMenuCard
                        iconXml={exclamationIconXml}
                        iconColor="#DC2626"
                        title="라이센스"
                        description="공공데이터 출처를 확인합니다."
                        onPress={() => router.push("/license")}
                    />
                </View>

                <View style={styles.infoCard}>
                    <Text style={styles.infoTitle}>UV Alert</Text>
                    <Text style={styles.infoText}>
                        기상청 데이터를 기반으로 날씨와 자외선 지수를 확인하는 앱입니다.
                    </Text>
                    <Text style={styles.versionText}>Version 1.0.0</Text>
                </View>
            </ScrollView>
        </ScreenContainer>
    );
}

function SettingMenuCard({
    iconXml,
    iconColor,
    title,
    description,
    onPress,
}: {
    iconXml: string;
    iconColor: string;
    title: string;
    description: string;
    onPress: () => void;
}) {
    return (
        <Pressable style={styles.menuCard} onPress={onPress}>
            <View style={styles.iconBox}>
                <SvgXml xml={iconXml} width={23} height={23} color={iconColor} />
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
        width: 32,
        height: 32,
        justifyContent: "center",
        alignItems: "center",
        marginRight: 14,
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
