package com.mintWaterMelon.uvmanager.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI uvmangerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UV Manger API")
                        .description("자외선 지수 확인 및 알림 설정을 위한 API 문서")
                        .version("v1.0.0"));
    }
}