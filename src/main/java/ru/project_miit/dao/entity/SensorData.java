package ru.project_miit.dao.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private boolean failure;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isFailure() {
        return failure;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

