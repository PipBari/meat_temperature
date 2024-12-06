package ru.project_miit.gui;

import com.formdev.flatlaf.FlatLightLaf;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import org.springframework.web.client.RestTemplate;
import ru.project_miit.rest.dto.SensorDataDTO;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class ReactiveSwingApp {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "http://localhost:8080/api/sensors";
    private MainFrame mainFrame;
    private UIUpdater uiUpdater;

    public ReactiveSwingApp() {
        FlatLightLaf.setup();
        createAndShowGUI();
        connectWebSocket();
        fetchLatestData();
        fetchThresholds();
    }

    private void createAndShowGUI() {
        mainFrame = new MainFrame();
        uiUpdater = new UIUpdater(mainFrame.getTemperatureLabel(), mainFrame.getStatusLabel());

        mainFrame.getIncreaseButton().addActionListener(e -> {
            restTemplate.postForObject(apiUrl + "/increase", null, Void.class);
            fetchLatestData();
        });

        mainFrame.getDecreaseButton().addActionListener(e -> {
            restTemplate.postForObject(apiUrl + "/decrease", null, Void.class);
            fetchLatestData();
        });

        mainFrame.getApplyThresholdButton().addActionListener(e -> {
            try {
                double min = Double.parseDouble(mainFrame.getMinTemperatureField().getText());
                double max = Double.parseDouble(mainFrame.getMaxTemperatureField().getText());
                restTemplate.postForObject(apiUrl + "/threshold", new double[]{min, max}, Void.class);
                uiUpdater.updateStatus("Пороги критических температур обновлены", Color.BLUE);
            } catch (NumberFormatException ex) {
                uiUpdater.updateStatus("Ошибка: некорректные значения порогов", Color.RED);
            }
        });
    }

    private void connectWebSocket() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(new WebSocketClient(uiUpdater), new URI("ws://localhost:8080/ws/sensors"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchLatestData() {
        try {
            SensorDataDTO data = restTemplate.getForObject(apiUrl + "/latest", SensorDataDTO.class);
            if (data != null) {
                uiUpdater.updateUI(data);
            }
        } catch (Exception e) {
            uiUpdater.updateStatus("Ошибка получения данных: " + e.getMessage(), Color.ORANGE);
        }
    }

    private void fetchThresholds() {
        try {
            double[] thresholds = restTemplate.getForObject(apiUrl + "/thresholds", double[].class);
            if (thresholds != null && thresholds.length == 2) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.getMinTemperatureField().setText(String.format("%.2f", thresholds[0]));
                    mainFrame.getMaxTemperatureField().setText(String.format("%.2f", thresholds[1]));
                });
            }
        } catch (Exception e) {
            uiUpdater.updateStatus("Ошибка загрузки порогов: " + e.getMessage(), Color.ORANGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReactiveSwingApp::new);
    }
}
