package nl.hhs.challenge;

import javax.swing.*;
import java.awt.*;

public class GUI1 {

    private JFrame frame1;
    private JButton inlog;

    // Statische variabelen voor gebruikersgegevens
    private static String registeredUsername = "";
    private static String registeredPassword = "";

    // Methode om een gebruiker te registreren
    public static void registerUser(String username, String password) {
        registeredUsername = username;
        registeredPassword = password;
    }

    // Methode om inloggegevens te valideren
    public static boolean validateLogin(String username, String password) {
        return username.equals(registeredUsername) && password.equals(registeredPassword);
    }

    public GUI1() {
        frame1 = new JFrame();
        frame1.setSize(900, 600);
        frame1.setResizable(false);
        frame1.setTitle("Login");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setLayout(null);

        JLabel titleLabel = new JLabel("Inloggen");
        titleLabel.setBounds(410, 100, 100, 20);
        frame1.add(titleLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(320, 160, 250, 30);
        frame1.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(320, 210, 250, 30);
        frame1.add(passwordField);

        JLabel Hydro = new JLabel("Hydro-Home");
        Hydro.setFont(new Font("Serif", Font.BOLD, 100));
        Hydro.setForeground(new Color(200, 200, 200, 50));
        Hydro.setOpaque(false);
        Hydro.setBounds(20, 400, 700, 100);
        frame1.add(Hydro);

        inlog = new JButton("Login");
        inlog.setBounds(320, 300, 250, 30);
        inlog.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                JOptionPane.showMessageDialog(frame1, "Inloggen succesvol!");

                SerialInOutExample.logSucc = true; // Zet de boolean op true
                SerialInOutExample.serialInstance.gui.setVisible(true); // GUI tonen.
                frame1.dispose(); // Login GUI sluiten.
            } else {
                JOptionPane.showMessageDialog(frame1, "Onjuiste gebruikersnaam of wachtwoord.", "Inlogfout", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame1.add(inlog);

        JButton createAccountButton = new JButton("Account aanmaken");
        createAccountButton.setBounds(320, 350, 250, 30);
        createAccountButton.addActionListener(e -> {
            frame1.dispose();
            new CreateAccountGUI();
        });
        frame1.add(createAccountButton);

        frame1.setVisible(true);
    }

    public static void main(String[] args) {
        registerUser("testuser", "testpass"); // Optioneel: een testaccount
        new GUI1();
        SerialInOutExample.serialInstance = new SerialInOutExample("SerialInOutExample");
        SerialInOutExample.serialInstance.setVisible(true);
    }
}
