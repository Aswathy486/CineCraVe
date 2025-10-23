package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.service.BookingService; 

public class SignupPage extends JFrame {

    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;
    private final BookingService bookingService = new BookingService();

    public SignupPage() {
        setTitle("CineCraVe - Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Create Your CineCraVe Account");
        title.setFont(new Font("Poppins", Font.BOLD, 18));
        title.setBounds(130, 30, 400, 30);
        add(title);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(100, 100, 100, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(220, 100, 250, 30);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 150, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(220, 150, 250, 30);
        add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(100, 200, 100, 25);
        add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(220, 200, 250, 30);
        add(phoneField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 250, 100, 25);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(220, 250, 250, 30);
        add(passwordField);

        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(138, 43, 226));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBounds(220, 310, 150, 35);
        add(createBtn);

        JLabel backLabel = new JLabel("Back to Login");
        backLabel.setForeground(new Color(75, 0, 130));
        backLabel.setBounds(250, 360, 150, 25);
        add(backLabel);

        // --- Event Listener with Logic ---
        createBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());
            
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                boolean success = bookingService.signUpCustomer(name, email, phone, password);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Account created successfully! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new LoginPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed. Email may already be in use.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginPage();
            }
        });

        setVisible(true);
    }
}
