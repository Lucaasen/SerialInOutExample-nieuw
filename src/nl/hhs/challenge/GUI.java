package nl.hhs.challenge;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener, ChangeListener {

    private JLabel sliderLabel;
    private JFrame frame;
    private JPanel panel;
    private JSlider slider;
    private JButton backButton;

    private int waterPercentage = 0;

    public GUI() {
        frame = new JFrame();

        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        sliderLabel = new JLabel("Water percentage: 0%");

        // Zet in de database om water aan te passen: setWaterPercentage(0-100);

        final int min = 0;
        final int max = 100;
        final int init = 0;

        slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setEnabled(false);

        backButton = new JButton("Terug naar login");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUI1();
                frame.dispose();
            }
        });

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(90, 90, 50, 90));
        panel.setLayout(new GridLayout(0, 1));

        panel.add(sliderLabel);
        panel.add(slider);
        panel.add(backButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("GUI water");

        frame.setVisible(true);
    }

    public void setWaterPercentage(int percentage) {
        this.waterPercentage = percentage;
        slider.setValue(percentage);
        sliderLabel.setText("Water percentage: " + percentage + "%");
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }


    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setWaterPercentage(0);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (slider.getValue() != waterPercentage) {
            slider.setValue(waterPercentage);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Placeholder voor actiegebaseerde evenementen
    }
}
