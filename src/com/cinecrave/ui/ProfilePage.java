package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.model.Customer;
import com.cinecrave.service.BookingService;

public class ProfilePage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private JTextField nameField, emailField, phoneField;
    private final JFrame parentFrame;

    public ProfilePage(JFrame parentFrame, Customer customer, BookingService service) {
        this.parentFrame = parentFrame;
        this.loggedInCustomer = customer;
        this.bookingService = service;

        setTitle("Profile Settings | " + customer.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Manage Account", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(new JLabel("User ID:"));
        formPanel.add(new JLabel(String.valueOf(customer.getUserId())));

        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField(customer.getName());
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email (Read-Only):"));
        emailField = new JTextField(customer.getEmail());
        emailField.setEditable(false);
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(customer.getPhone());
        formPanel.add(phoneField);

        JButton resetPassBtn = new JButton("Reset Password");
        resetPassBtn.addActionListener(e -> {
            this.setVisible(false);
            new PasswordResetPage(this, loggedInCustomer, bookingService);
        });
        formPanel.add(resetPassBtn);
        
        JButton saveBtn = new JButton("Save Profile Updates");
        saveBtn.setBackground(new Color(255, 165, 0));
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Profile updated for " + nameField.getText() + "! (DB logic required)");
        });
        formPanel.add(saveBtn);

        add(formPanel, BorderLayout.CENTER);

        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
