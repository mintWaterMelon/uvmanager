package com.mintWaterMelon.uvmanager.home.service;

import com.mintWaterMelon.uvmanager.home.dto.HomeAdviceCondition;
import com.mintWaterMelon.uvmanager.home.dto.HomeAdviceResponse;
import com.mintWaterMelon.uvmanager.home.dto.HomeAdviceSeverity;
import com.mintWaterMelon.uvmanager.home.dto.HomeMode;
import org.springframework.stereotype.Service;

@Service
public class HomeAdviceService {

    public HomeAdviceResponse createAdvice(HomeAdviceCondition condition) {
        if (condition.mode() == HomeMode.NIGHT) {
            return createNightAdvice(condition);
        }

        if (condition.maxUv() >= 11) {
            return new HomeAdviceResponse(
                    "자외선이 매우 위험해요",
                    "가능하면 한낮 외출을 피하고, 외출이 필요하다면 긴 옷, 모자, 선글라스, 자외선 차단제를 모두 사용하세요.",
                    HomeAdviceSeverity.DANGER
            );
        }

        if (condition.maxUv() >= 8) {
            return new HomeAdviceResponse(
                    "자외선이 매우 강해요",
                    "외출 전 선크림을 충분히 바르고, 2~3시간마다 덧바르세요. 모자와 선글라스도 함께 사용하는 것이 좋습니다.",
                    HomeAdviceSeverity.WARNING
            );
        }

        if (condition.maxPrecipitationProbability() >= 60) {
            return new HomeAdviceResponse(
                    "비가 올 가능성이 높아요",
                    "외출 전 우산을 준비하고, 자외선 정보와 함께 강수확률도 확인하세요.",
                    HomeAdviceSeverity.INFO
            );
        }

        if (isRainy(condition.representativeWeather())) {
            return new HomeAdviceResponse(
                    "비가 와도 자외선 차단은 필요해요",
                    "흐리거나 비가 오는 날에도 자외선은 존재합니다. 장시간 외출한다면 기본적인 자외선 차단을 해주세요.",
                    HomeAdviceSeverity.INFO
            );
        }

        if (isSnowy(condition.representativeWeather())) {
            return new HomeAdviceResponse(
                    "눈 오는 날에는 반사 자외선도 주의하세요",
                    "눈이 오는 날에는 지면 반사로 자외선 노출이 생길 수 있습니다. 외출 시간이 길다면 선크림과 선글라스를 준비하세요.",
                    HomeAdviceSeverity.INFO
            );
        }

        if (condition.maxUv() >= 6) {
            return new HomeAdviceResponse(
                    "자외선이 높은 편이에요",
                    "야외활동 시간이 길다면 선크림을 바르고, 햇빛이 강한 시간대에는 그늘을 이용하는 것이 좋습니다.",
                    HomeAdviceSeverity.WARNING
            );
        }

        if (condition.maxUv() >= 3) {
            return new HomeAdviceResponse(
                    "자외선 차단을 준비하세요",
                    "자외선지수가 보통 수준입니다. 짧은 외출은 괜찮지만, 장시간 야외활동 시에는 선크림을 바르는 것이 좋습니다.",
                    HomeAdviceSeverity.INFO
            );
        }

        if (isHot(condition.representativeTemperature())) {
            return new HomeAdviceResponse(
                    "더운 날씨에는 피부 보호를 함께 챙기세요",
                    "기온이 높은 편입니다. 수분을 충분히 섭취하고, 외출 시 자외선 차단과 더위 대비를 함께 해주세요.",
                    HomeAdviceSeverity.INFO
            );
        }

        return new HomeAdviceResponse(
                "오늘은 자외선 부담이 낮은 편이에요",
                "일상적인 외출은 큰 부담이 적지만, 장시간 야외활동을 한다면 기본적인 자외선 차단을 권장합니다.",
                HomeAdviceSeverity.NORMAL
        );
    }

    private HomeAdviceResponse createNightAdvice(HomeAdviceCondition condition) {
        if (condition.maxPrecipitationProbability() >= 60) {
            return new HomeAdviceResponse(
                    "밤에는 빗길을 조심하세요",
                    "강수확률이 높은 밤에는 이동 시 시야와 노면 상태에 주의하세요.",
                    HomeAdviceSeverity.INFO
            );
        }

        if (isRainy(condition.representativeWeather())) {
            return new HomeAdviceResponse(
                    "밤에는 빗길을 조심하세요",
                    "자외선 걱정은 적지만 비가 오는 밤에는 이동 시 시야와 노면 상태에 주의하세요.",
                    HomeAdviceSeverity.INFO
            );
        }

        return new HomeAdviceResponse(
                "밤에는 자외선 걱정이 적어요",
                "밤 시간대에는 자외선 노출이 적습니다. 내일 낮 시간대의 자외선 정보를 미리 확인해보세요.",
                HomeAdviceSeverity.NORMAL
        );
    }

    private boolean isRainy(String weather) {
        return "비".equals(weather)
                || "비/눈".equals(weather)
                || "소나기".equals(weather)
                || "강수".equals(weather);
    }

    private boolean isSnowy(String weather) {
        return "눈".equals(weather);
    }

    private boolean isHot(Integer temperature) {
        return temperature != null && temperature >= 30;
    }
}