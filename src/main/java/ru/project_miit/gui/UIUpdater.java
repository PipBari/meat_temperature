package ru.project_miit.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import ru.project_miit.rest.dto.SensorDataDTO;

public class UIUpdater {
    private final JLabel temperatureLabel;
    private final JLabel statusLabel;

    public UIUpdater(JLabel temperatureLabel, JLabel statusLabel) {
        this.temperatureLabel = temperatureLabel;
        this.statusLabel = statusLabel;
    }

    public void updateUI(SensorDataDTO data) {
        SwingUtilities.invokeLater(() -> {
            temperatureLabel.setText(String.format(Locale.US, "Температура: %.2f°C", data.getTemperature()));
            if (data.isFailure()) {
                statusLabel.setText("Статус: Критическая ситуация!");
                statusLabel.setForeground(Color.RED);
            } else {
                statusLabel.setText("Статус: Норма");
                statusLabel.setForeground(Color.GREEN);
            }
        });
    }

    public void updateStatus(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            statusLabel.setForeground(color);
        });
    }
}
