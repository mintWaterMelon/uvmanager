import { StyleSheet, Text, View } from "react-native";

export default function SunscreenAlertScreen() {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>선크림 알림</Text>

            <View style={styles.card}>
                <Text style={styles.label}>알림 시간</Text>
                <Text style={styles.value}>08:00</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>알림 기준 UV 지수</Text>
                <Text style={styles.value}>6 이상</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>알림 상태</Text>
                <Text style={styles.value}>ON</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>알림 판단 결과</Text>
                <Text style={styles.message}>API 연결 후 표시됩니다.</Text>
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
    message: {
        fontSize: 14,
        color: "#444444",
    },
});