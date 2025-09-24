import { ReactNode } from "react";
import { StyleSheet, ViewStyle } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

type ScreenContainerProps = {
    children: ReactNode;
    style?: ViewStyle;
};

export default function ScreenContainer({
    children,
    style,
}: ScreenContainerProps) {
    return (
        <SafeAreaView style={[styles.container, style]} edges={["top"]}>
            {children}
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#F7F8FA",
    },
});