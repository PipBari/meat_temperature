package ru.project_miit.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.project_miit.rest.dto.SensorDataDTO;

import java.util.Random;

@Component
public class SensorDataSimulator {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${simulator.api.url}")
    private String apiUrl;

    private double minThreshold = -5.0;
    private double maxThreshold = 15.0;

    @Scheduled(fixedRate = 30000)
    public void generateTemperatureData() {
        try {
            double[] thresholds = restTemplate.getForObject(apiUrl + "/thresholds", double[].class);
            if (thresholds != null && thresholds.length == 2) {
                minThreshold = thresholds[0];
                maxThreshold = thresholds[1];
            }

            SensorDataDTO data = generateRandomData();
            restTemplate.postForObject(apiUrl, data, SensorDataDTO.class);
            System.out.println("Созданы данные: " + data);
        } catch (Exception e) {
            System.err.println("Ошибка при отправке данных: " + e.getMessage());
        }
    }

    private SensorDataDTO generateRandomData() {
        SensorDataDTO data = new SensorDataDTO();
        int randomTemperature = -10 + new Random().nextInt(31);
        data.setTemperature(randomTemperature);
        data.setFailure(randomTemperature < minThreshold || randomTemperature > maxThreshold);
        return data;
    }
}

