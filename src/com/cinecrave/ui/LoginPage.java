package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.service.BookingService; 
import com.cinecrave.model.User; 
import com.cinecrave.model.Customer;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    
    private final BookingService bookingService = new BookingService();

    public LoginPage() {
        setTitle("CineCraVe - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // --- Left Panel (Logo) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(255, 252, 245));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("🍿 CineCraVe", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 36));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel tagline = new JLabel("Your Flick Fix", SwingConstants.CENTER);
        tagline.setFont(new Font("Poppins", Font.PLAIN, 18));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logo);
        leftPanel.add(tagline);
        leftPanel.add(Box.createVerticalGlue());

        // --- Right Panel (Login Form) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel emailLabel = new JLabel("Email / Phone:");
        emailLabel.setBounds(100, 80, 150, 25);
        rightPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 110, 250, 30);
        rightPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 160, 150, 25);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 190, 250, 30);
        rightPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(138, 43, 226));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBounds(100, 240, 250, 35);
        rightPanel.add(loginBtn);

        JButton googleBtn = new JButton("Sign in with Google");
        googleBtn.setBounds(100, 290, 250, 35);
        rightPanel.add(googleBtn);

        JLabel createAccLabel = new JLabel("Create account");
        createAccLabel.setBounds(100, 340, 120, 25);
        createAccLabel.setForeground(new Color(75, 0, 130));
        rightPanel.add(createAccLabel);

        JLabel forgotLabel = new JLabel("Forgot Password?");
        forgotLabel.setBounds(230, 340, 150, 25);
        forgotLabel.setForeground(new Color(75, 0, 130));
        rightPanel.add(forgotLabel);

        // --- Event Listener with Logic ---
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            try {
                User authenticatedUser = bookingService.authenticateUser(email, password);

                if (authenticatedUser != null) {
                    dispose();
                    
                    if (authenticatedUser instanceof Customer) {
                        new HomePage((Customer) authenticatedUser, bookingService); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password. Please try again.", 
                                                  "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "System Error: Could not connect to database.", 
                                              "System Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        createAccLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new SignupPage();
            }
        });

        add(leftPanel);
        add(rightPanel);
        setVisible(true);
    }
}