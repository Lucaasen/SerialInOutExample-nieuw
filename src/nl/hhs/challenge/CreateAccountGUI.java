package nl.hhs.challenge;

import javax.swing.*;
import java.awt.*;

public class CreateAccountGUI {

    private JFrame frame;

    public CreateAccountGUI() {
        frame = new JFrame("Account aanmaken");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Account aanmaken");
        titleLabel.setBounds(380, 100, 200, 20);
        frame.add(titleLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(320, 160, 250, 30);
        frame.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(320, 210, 250, 30);
        frame.add(passwordField);

        JButton createAccountButton = new JButton("Account aanmaken");
        createAccountButton.setBounds(320, 300, 250, 30);
        createAccountButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Gebruikersnaam en wachtwoord mogen niet leeg zijn.", "Fout", JOptionPane.ERROR_MESSAGE);
            } else {
                GUI1.registerUser(username, password); // Registreer de gebruiker
                JOptionPane.showMessageDialog(frame, "Account succesvol aangemaakt!");
                frame.dispose();
                new GUI1();
            }
        });
        frame.add(createAccountButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CreateAccountGUI();
    }
}
