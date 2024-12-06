package ru.project_miit.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.project_miit.dao.entity.SensorData;
import ru.project_miit.dao.rep.SensorDataRepository;
import ru.project_miit.rest.dto.SensorDataDTO;
import ru.project_miit.service.SensorDataService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final ModelMapper modelMapper;

    public SensorDataServiceImpl(SensorDataRepository sensorDataRepository, ModelMapper modelMapper) {
        this.sensorDataRepository = sensorDataRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SensorDataDTO> getAllSensorData() {
        return sensorDataRepository.findAll()
                .stream()
                .map(sensorData -> modelMapper.map(sensorData, SensorDataDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SensorDataDTO saveSensorData(SensorDataDTO dto) {
        SensorData sensorData = modelMapper.map(dto, SensorData.class);
        SensorData savedData = sensorDataRepository.save(sensorData);
        return modelMapper.map(savedData, SensorDataDTO.class);
    }

    @Override
    public void deleteSensorData(Long id) {
        sensorDataRepository.deleteById(id);
    }

    @Override
    public List<SensorDataDTO> findSensorDataByTemperature(double minTemperature) {
        return sensorDataRepository.findAll()
                .stream()
                .filter(data -> data.getTemperature() > minTemperature)
                .map(sensorData -> modelMapper.map(sensorData, SensorDataDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SensorDataDTO> getLatestSensorData() {
        return sensorDataRepository.findTopByOrderByTimestampDesc()
                .map(data -> modelMapper.map(data, SensorDataDTO.class));
    }

    @Override
    public void modifyLatestTemperature(double delta) {
        sensorDataRepository.findTopByOrderByTimestampDesc().ifPresentOrElse(data -> {
            SensorData newData = new SensorData();
            double updatedTemperature = Math.round(data.getTemperature() + delta);
            newData.setTemperature(updatedTemperature);

            newData.setFailure(updatedTemperature < -5 || updatedTemperature > 15);

            newData.setTimestamp(java.time.LocalDateTime.now());
            sensorDataRepository.save(newData);
        }, () -> {
            SensorData initialData = new SensorData();
            initialData.setTemperature(Math.round(delta));
            initialData.setFailure(delta < -5 || delta > 15);
            initialData.setTimestamp(java.time.LocalDateTime.now());
            sensorDataRepository.save(initialData);
        });
    }
}



