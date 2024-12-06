package ru.project_miit.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project_miit.component.SensorDataSimulator;
import ru.project_miit.config.SensorWebSocketHandler;
import ru.project_miit.rest.dto.SensorDataDTO;
import ru.project_miit.service.SensorDataService;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/sensors")
public class SensorDataController {

    private double minThreshold = -5.0;
    private double maxThreshold = 15.0;

    private final SensorDataService sensorDataService;
    private final SensorWebSocketHandler webSocketHandler;
    private final SensorDataSimulator sensorDataSimulator;

    public SensorDataController(SensorDataService sensorDataService,
                                SensorWebSocketHandler webSocketHandler,
                                SensorDataSimulator sensorDataSimulator) {
        this.sensorDataService = sensorDataService;
        this.webSocketHandler = webSocketHandler;
        this.sensorDataSimulator = sensorDataSimulator;
    }

    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getAllSensorData() {
        List<SensorDataDTO> data = sensorDataService.getAllSensorData();
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<SensorDataDTO> createSensorData(@RequestBody SensorDataDTO sensorDataDTO) {
        SensorDataDTO savedData = sensorDataService.saveSensorData(sensorDataDTO);
        updateWebSocket(savedData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
    }

    @PostMapping("/increase")
    public ResponseEntity<Void> increaseTemperature() {
        processTemperatureChange(5.0);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decrease")
    public ResponseEntity<Void> decreaseTemperature() {
        processTemperatureChange(-5.0);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/latest")
    public ResponseEntity<SensorDataDTO> getLatestData() {
        return sensorDataService.getLatestSensorData()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/thresholds")
    public ResponseEntity<double[]> getThresholds() {
        return ResponseEntity.ok(new double[]{minThreshold, maxThreshold});
    }

    @PostMapping("/threshold")
    public ResponseEntity<Void> updateThresholds(@RequestBody double[] thresholds) {
        if (thresholds.length == 2) {
            minThreshold = thresholds[0];
            maxThreshold = thresholds[1];
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private void processTemperatureChange(double delta) {
        sensorDataService.modifyLatestTemperature(delta);

        sensorDataService.getLatestSensorData().ifPresent(this::updateWebSocket);
    }

    private void updateWebSocket(SensorDataDTO data) {
        String message = String.format(Locale.US, "%.2f,%s",
                data.getTemperature(),
                data.isFailure() ? "Критическая ситуация" : "Норма"
        );
        webSocketHandler.sendUpdate(message);
    }
}
