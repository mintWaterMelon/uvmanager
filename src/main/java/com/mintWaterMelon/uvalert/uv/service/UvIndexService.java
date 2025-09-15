package com.mintWaterMelon.uvalert.uv.service;

import com.mintWaterMelon.uvalert.uv.client.KmaUvIndexClient;
import com.mintWaterMelon.uvalert.uv.dto.UvIndexResponse;
import org.springframework.stereotype.Service;

@Service
public class UvIndexService {

    private final KmaUvIndexClient kmaUvIndexClient;

    public UvIndexService(KmaUvIndexClient kmaUvIndexClient) {
        this.kmaUvIndexClient = kmaUvIndexClient;
    }

    public UvIndexResponse getUvIndex(String areaNo, String time) {
        return kmaUvIndexClient.getUvIndex(areaNo, time);
    }
}