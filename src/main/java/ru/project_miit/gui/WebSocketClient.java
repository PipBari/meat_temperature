package ru.project_miit.gui;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import ru.project_miit.rest.dto.SensorDataDTO;

import javax.swing.*;
import java.awt.*;

@ClientEndpoint
public class WebSocketClient {
    private final UIUpdater uiUpdater;

    public WebSocketClient(UIUpdater uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length == 2) {
                double temperature = Double.parseDouble(parts[0].trim());
                String status = parts[1].trim();

                SwingUtilities.invokeLater(() -> {
                    uiUpdater.updateStatus("Температура обновлена", Color.BLUE);
                    uiUpdater.updateUI(new SensorDataDTO(temperature, "Критическая ситуация".equals(status)));
                });
            } else {
                throw new IllegalArgumentException("Неверный формат сообщения");
            }
        } catch (Exception e) {
            uiUpdater.updateStatus("Ошибка обработки сообщения: " + e.getMessage(), Color.ORANGE);
        }
    }
}
