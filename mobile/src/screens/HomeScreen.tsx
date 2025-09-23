import { useEffect } from "react";
import { StyleSheet, Text, View } from "react-native";
import { getHome } from "../api/homeApi";

export default function HomeScreen() {
    useEffect(() => {
        getHome("1100000000")
            .then((data) => {
                console.log("Home API result:", data);
            })
            .catch((error) => {
                console.error("Home API error:", error);
            });
    }, []);
    return (
        <View style={styles.container}>
            <Text style={styles.title}>UV Alert</Text>

            <View style={styles.card}>
                <Text style={styles.label}>현재 시간</Text>
                <Text style={styles.value}>TBD</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>현재 위치</Text>
                <Text style={styles.value}>서울특별시</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>현재 UV 지수</Text>
                <Text style={styles.uvValue}>TBD</Text>
                <Text style={styles.message}>자외선 정보를 불러올 예정입니다.</Text>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>시간대별 UV 지수</Text>
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
    uvValue: {
        fontSize: 40,
        fontWeight: "800",
    },
    message: {
        fontSize: 14,
        color: "#444444",
    },
});