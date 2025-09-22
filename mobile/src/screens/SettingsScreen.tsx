import { StyleSheet, Text, View } from "react-native";

export default function SettingsScreen() {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>설정</Text>

            <View style={styles.card}>
                <Text style={styles.label}>기본 지역</Text>
                <Text style={styles.value}>서울특별시</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>기본 UV 기준값</Text>
                <Text style={styles.value}>6</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>기본 알림 시간</Text>
                <Text style={styles.value}>08:00</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>선크림 알림</Text>
                <Text style={styles.value}>사용</Text>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        gap: 16,
        backgroundColor: "#F7F8FA",
    },
    title: {
        fontSize: 28,
        fontWeight: "700",
        marginBottom: 8,
    },
    card: {
        backgroundColor: "#FFFFFF",
        padding: 16,
        borderRadius: 16,
    },
    label: {
        fontSize: 14,
        color: "#666666",
        marginBottom: 8,
    },
    value: {
        fontSize: 20,
        fontWeight: "600",
    },
});