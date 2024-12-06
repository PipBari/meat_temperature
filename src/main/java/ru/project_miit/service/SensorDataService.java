package ru.project_miit.service;

import ru.project_miit.dao.entity.SensorData;
import ru.project_miit.rest.dto.SensorDataDTO;

import java.util.List;

import java.util.List;
import java.util.Optional;

public interface SensorDataService {
    List<SensorDataDTO> getAllSensorData();
    SensorDataDTO saveSensorData(SensorDataDTO dto);
    void deleteSensorData(Long id);
    List<SensorDataDTO> findSensorDataByTemperature(double minTemperature);
    Optional<SensorDataDTO> getLatestSensorData();
    void modifyLatestTemperature(double delta);
}
