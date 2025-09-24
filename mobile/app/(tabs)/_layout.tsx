import { Tabs } from "expo-router";
import { useSafeAreaInsets } from "react-native-safe-area-context";

export default function TabLayout() {
    const insets = useSafeAreaInsets();

    return (
        <Tabs
            screenOptions={{
                headerShown: false,
                tabBarActiveTintColor: "#2563EB",
                tabBarInactiveTintColor: "#888888",
                tabBarStyle: {
                    height: 56 + insets.bottom,
                    paddingTop: 8,
                    paddingBottom: Math.max(insets.bottom, 8),
                    backgroundColor: "#FFFFFF",
                    borderTopWidth: 1,
                    borderTopColor: "#EEEEEE",
                },
                tabBarLabelStyle: {
                    fontSize: 12,
                    fontWeight: "600",
                },
            }}
        >
            <Tabs.Screen
                name="index"
                options={{
                    title: "Home",
                }}
            />

            <Tabs.Screen
                name="sunscreen"
                options={{
                    title: "Sunscreen",
                }}
            />

            <Tabs.Screen
                name="settings"
                options={{
                    title: "Settings",
                }}
            />
        </Tabs>
    );
}