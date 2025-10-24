import { Tabs } from "expo-router";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { SvgXml } from "react-native-svg";

const homeIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Capa_1" x="0px" y="0px" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512;" xml:space="preserve" width="512" height="512">
<g>
	<path d="M256,319.841c-35.346,0-64,28.654-64,64v128h128v-128C320,348.495,291.346,319.841,256,319.841z"/>
	<g>
		<path d="M362.667,383.841v128H448c35.346,0,64-28.654,64-64V253.26c0.005-11.083-4.302-21.733-12.011-29.696l-181.29-195.99    c-31.988-34.61-85.976-36.735-120.586-4.747c-1.644,1.52-3.228,3.103-4.747,4.747L12.395,223.5    C4.453,231.496-0.003,242.31,0,253.58v194.261c0,35.346,28.654,64,64,64h85.333v-128c0.399-58.172,47.366-105.676,104.073-107.044    C312.01,275.383,362.22,323.696,362.667,383.841z"/>
		<path d="M256,319.841c-35.346,0-64,28.654-64,64v128h128v-128C320,348.495,291.346,319.841,256,319.841z"/>
	</g>
</g>
</svg>`;

const lightbulbIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="512" height="512"><path d="m17.994,2.286C16.086.581,13.522-.231,10.956.059,6.904.517,3.59,3.782,3.075,7.822c-.374,2.931.644,5.76,2.793,7.761,1.375,1.279,2.132,2.9,2.132,4.566v.161c0,2.035,1.655,3.69,3.69,3.69h.619c2.035,0,3.69-1.655,3.69-3.69v-.549c0-1.486.687-2.906,1.932-3.998,1.95-1.708,3.068-4.173,3.068-6.763,0-2.56-1.096-5.007-3.006-6.714Zm-5.685,19.714h-.619c-.932,0-1.69-.758-1.69-1.69v-.161c0-.05-.001-.1-.002-.149h4.002v.31c0,.932-.759,1.69-1.69,1.69Zm4.304-7.741c-1.177,1.032-1.998,2.34-2.376,3.741h-4.548c-.42-1.431-1.258-2.765-2.458-3.881-1.671-1.556-2.463-3.759-2.171-6.043.399-3.138,2.974-5.673,6.121-6.029.278-.031.554-.047.828-.047,1.725,0,3.353.617,4.652,1.778,1.486,1.328,2.339,3.231,2.339,5.222,0,2.013-.87,3.93-2.387,5.259Zm-5.613-3.259v-5c0-.552.447-1,1-1s1,.448,1,1v5c0,.552-.447,1-1,1s-1-.448-1-1Zm2.5,3.5c0,.828-.672,1.5-1.5,1.5s-1.5-.672-1.5-1.5.672-1.5,1.5-1.5,1.5.672,1.5,1.5Z"/></svg>`;

const settingsIconXml = `<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Capa_1" x="0px" y="0px" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512;" xml:space="preserve" width="512" height="512">
<g>
	<path d="M34.283,384c17.646,30.626,56.779,41.148,87.405,23.502c0.021-0.012,0.041-0.024,0.062-0.036l9.493-5.483   c17.92,15.332,38.518,27.222,60.757,35.072V448c0,35.346,28.654,64,64,64s64-28.654,64-64v-10.944   c22.242-7.863,42.841-19.767,60.757-35.115l9.536,5.504c30.633,17.673,69.794,7.167,87.467-23.467   c17.673-30.633,7.167-69.794-23.467-87.467l0,0l-9.472-5.461c4.264-23.201,4.264-46.985,0-70.187l9.472-5.461   c30.633-17.673,41.14-56.833,23.467-87.467c-17.673-30.633-56.833-41.14-87.467-23.467l-9.493,5.483   C362.862,94.638,342.25,82.77,320,74.944V64c0-35.346-28.654-64-64-64s-64,28.654-64,64v10.944   c-22.242,7.863-42.841,19.767-60.757,35.115l-9.536-5.525C91.073,86.86,51.913,97.367,34.24,128s-7.167,69.794,23.467,87.467l0,0   l9.472,5.461c-4.264,23.201-4.264,46.985,0,70.187l-9.472,5.461C27.158,314.296,16.686,353.38,34.283,384z M256,170.667   c47.128,0,85.333,38.205,85.333,85.333S303.128,341.333,256,341.333S170.667,303.128,170.667,256S208.872,170.667,256,170.667z"/>
</g>
</svg>`;

function TabSvgIcon({
    xml,
    color,
    size,
}: {
    xml: string;
    color: string;
    size: number;
}) {
    return (
        <SvgXml
            xml={xml}
            width={size}
            height={size}
            color={color}
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
                        <TabSvgIcon xml={homeIconXml} color={color} size={size} />
                    ),
                }}
            />

            <Tabs.Screen
                name="uv-info"
                options={{
                    title: "자외선",
                    tabBarIcon: ({ color, size }) => (
                        <TabSvgIcon xml={lightbulbIconXml} color={color} size={size} />
                    ),
                }}
            />

            <Tabs.Screen
                name="settings"
                options={{
                    title: "설정",
                    tabBarIcon: ({ color, size }) => (
                        <TabSvgIcon xml={settingsIconXml} color={color} size={size} />
                    ),
                }}
            />
        </Tabs>
    );
}
