package ru.project_miit.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JLabel temperatureLabel;
    private JLabel statusLabel;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JTextField minTemperatureField;
    private JTextField maxTemperatureField;
    private JButton applyThresholdButton;

    public MainFrame() {
        setTitle("Мониторинг температуры");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Мониторинг температуры", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        temperatureLabel = new JLabel("Температура: Загрузка...", SwingConstants.CENTER);
        temperatureLabel.setFont(new Font("Arial", Font.BOLD, 16));

        statusLabel = new JLabel("Статус: Загрузка...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        contentPanel.add(temperatureLabel);
        contentPanel.add(statusLabel);

        JPanel thresholdPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        thresholdPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel minLabel = new JLabel("Мин. критическая температура:");
        minTemperatureField = new JTextField();
        JLabel maxLabel = new JLabel("Макс. критическая температура:");
        maxTemperatureField = new JTextField();
        applyThresholdButton = new JButton("Применить");

        thresholdPanel.add(minLabel);
        thresholdPanel.add(minTemperatureField);
        thresholdPanel.add(maxLabel);
        thresholdPanel.add(maxTemperatureField);
        thresholdPanel.add(applyThresholdButton);

        contentPanel.add(thresholdPanel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        increaseButton = new JButton("Повысить температуру");
        decreaseButton = new JButton("Понизить температуру");

        buttonPanel.add(increaseButton);
        buttonPanel.add(decreaseButton);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JLabel getTemperatureLabel() {
        return temperatureLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JButton getIncreaseButton() {
        return increaseButton;
    }

    public JButton getDecreaseButton() {
        return decreaseButton;
    }

    public JTextField getMinTemperatureField() {
        return minTemperatureField;
    }

    public JTextField getMaxTemperatureField() {
        return maxTemperatureField;
    }

    public JButton getApplyThresholdButton() {
        return applyThresholdButton;
    }
}
