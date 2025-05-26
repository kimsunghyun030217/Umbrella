//	락 해제/잠금 요청  (	하드웨어 락 제어 API 호출 )

package com.example.umbrella.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HardwareService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8081/hardware";

    public void unlock(int tableNumber) {
        try {
            restTemplate.postForEntity(BASE_URL + "/unlock/" + tableNumber, null, String.class);
        } catch (Exception e) {
            System.out.println("하드웨어 unlock 실패: " + e.getMessage());
        }
    }

    public void lock(int tableNumber) {
        try {
            restTemplate.postForEntity(BASE_URL + "/lock/" + tableNumber, null, String.class);
        } catch (Exception e) {
            System.out.println("하드웨어 lock 실패: " + e.getMessage());
        }
    }
}
