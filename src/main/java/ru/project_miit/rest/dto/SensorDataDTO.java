package ru.project_miit.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataDTO {
    private Long id;
    private double temperature;
    private boolean failure;
    private String timestamp;

    public SensorDataDTO(double temperature, boolean failure) {
        this.temperature = temperature;
        this.failure = failure;
    }
}

