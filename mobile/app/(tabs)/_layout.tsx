import { Tabs } from "expo-router";
import { Image, ImageSourcePropType } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const homeIcon = require("../assets/tab-icons/icon-home.png");
const uvIcon = require("../assets/tab-icons/icon-lightbulb.png");
const settingsIcon = require("../assets/tab-icons/icon-settings.png");

function TabIcon({
    source,
    color,
    size,
}: {
    source: ImageSourcePropType;
    color: string;
    size: number;
}) {
    return (
        <Image
            source={source}
            style={{
                width: size,
                height: size,
                tintColor: color,
                resizeMode: "contain",
            }}
        />
    );
}

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
                    title: "홈",
                    tabBarIcon: ({ color, size }) => (
                        <TabIcon source={homeIcon} color={color} size={size} />
                    ),
                }}
            />

            <Tabs.Screen
                name="uv-info"
                options={{
                    title: "자외선",
                    tabBarIcon: ({ color, size }) => (
                        <TabIcon source={uvIcon} color={color} size={size} />
                    ),
                }}
            />

            <Tabs.Screen
                name="settings"
                options={{
                    title: "설정",
                    tabBarIcon: ({ color, size }) => (
                        <TabIcon source={settingsIcon} color={color} size={size} />
                    ),
                }}
            />
        </Tabs>
    );
}
