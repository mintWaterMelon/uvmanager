import { useState } from "react";
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    View,
} from "react-native";

import { AreaResponse, searchAreas } from "../api/areaApi";

export default function SettingsScreen() {
    const [keyword, setKeyword] = useState("");
    const [areas, setAreas] = useState<AreaResponse[]>([]);
    const [selectedArea, setSelectedArea] = useState<AreaResponse | null>(null);
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    async function handleSearch() {
        try {
            setLoading(true);
            setErrorMessage(null);

            const result = await searchAreas(keyword);
            setAreas(result);
        } catch (error) {
            console.error(error);
            setErrorMessage("지역 정보를 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    }

    function handleSelectArea(area: AreaResponse) {
        setSelectedArea(area);
        setKeyword(area.displayName);
        setAreas([]);
    }

    return (
        <ScrollView style={styles.container} contentContainerStyle={styles.content}>
            <Text style={styles.title}>설정</Text>

            <View style={styles.card}>
                <Text style={styles.label}>기본 지역 검색</Text>

                <View style={styles.searchRow}>
                    <TextInput
                        style={styles.input}
                        value={keyword}
                        onChangeText={setKeyword}
                        placeholder="예: 서울, 종로구, 청운효자동"
                        autoCapitalize="none"
                    />

                    <Pressable style={styles.searchButton} onPress={handleSearch}>
                        <Text style={styles.searchButtonText}>검색</Text>
                    </Pressable>
                </View>

                {loading && (
                    <View style={styles.loadingRow}>
                        <ActivityIndicator size="small" />
                        <Text style={styles.loadingText}>검색 중...</Text>
                    </View>
                )}

                {errorMessage && <Text style={styles.errorText}>{errorMessage}</Text>}

                {areas.length > 0 && (
                    <View style={styles.resultBox}>
                        {areas.map((area) => (
                            <Pressable
                                key={area.areaNo}
                                style={styles.resultItem}
                                onPress={() => handleSelectArea(area)}
                            >
                                <Text style={styles.resultName}>{area.displayName}</Text>
                                <Text style={styles.resultCode}>{area.areaNo}</Text>
                            </Pressable>
                        ))}
                    </View>
                )}
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>선택된 기본 지역</Text>

                {selectedArea ? (
                    <>
                        <Text style={styles.value}>{selectedArea.displayName}</Text>
                        <Text style={styles.subText}>지역 코드: {selectedArea.areaNo}</Text>
                    </>
                ) : (
                    <Text style={styles.message}>아직 선택된 지역이 없습니다.</Text>
                )}
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
        </ScrollView>
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
        paddingBottom: 32,
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
    label: {
        fontSize: 14,
        color: "#666666",
        marginBottom: 8,
    },
    value: {
        fontSize: 20,
        fontWeight: "700",
    },
    subText: {
        marginTop: 4,
        fontSize: 12,
        color: "#777777",
    },
    message: {
        fontSize: 14,
        color: "#444444",
    },
    searchRow: {
        flexDirection: "row",
        gap: 8,
    },
    input: {
        flex: 1,
        borderWidth: 1,
        borderColor: "#DDDDDD",
        borderRadius: 12,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 14,
        backgroundColor: "#FFFFFF",
    },
    searchButton: {
        backgroundColor: "#2563EB",
        borderRadius: 12,
        paddingHorizontal: 16,
        justifyContent: "center",
        alignItems: "center",
    },
    searchButtonText: {
        color: "#FFFFFF",
        fontWeight: "700",
    },
    loadingRow: {
        marginTop: 12,
        flexDirection: "row",
        alignItems: "center",
        gap: 8,
    },
    loadingText: {
        color: "#555555",
        fontSize: 14,
    },
    errorText: {
        marginTop: 12,
        color: "#DC2626",
        fontSize: 14,
    },
    resultBox: {
        marginTop: 12,
        borderTopWidth: 1,
        borderTopColor: "#EEEEEE",
    },
    resultItem: {
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: "#EEEEEE",
    },
    resultName: {
        fontSize: 16,
        fontWeight: "700",
    },
    resultCode: {
        marginTop: 4,
        fontSize: 12,
        color: "#777777",
    },
});