package com.mintWaterMelon.uvalert.uv.service;

import com.mintWaterMelon.uvalert.uv.client.KmaUvIndexClient;
import com.mintWaterMelon.uvalert.uv.dto.UvIndexResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UvIndexService {

    private static final DateTimeFormatter API_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHH");

    private final KmaUvIndexClient kmaUvIndexClient;

    public UvIndexService(KmaUvIndexClient kmaUvIndexClient) {
        this.kmaUvIndexClient = kmaUvIndexClient;
    }

    public UvIndexResponse getUvIndex(String areaNo, String time) {
        return kmaUvIndexClient.getUvIndex(areaNo, time);
    }

    public UvIndexResponse getCurrentUvIndex(String areaNo) {
        String currentTime = LocalDateTime.now().format(API_TIME_FORMATTER);

        return kmaUvIndexClient.getUvIndex(areaNo, currentTime);
    }
}