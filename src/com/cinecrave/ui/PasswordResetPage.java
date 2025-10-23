package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.model.Customer;
import com.cinecrave.service.BookingService;

public class PasswordResetPage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private JPasswordField currentPassField, newPassField, confirmNewPassField;
    private final JFrame parentFrame;

    public PasswordResetPage(JFrame parentFrame, Customer customer, BookingService service) {
        this.parentFrame = parentFrame;
        this.loggedInCustomer = customer;
        this.bookingService = service;

        setTitle("Reset Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 380);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Change Password", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(new JLabel("Current Password:"));
        currentPassField = new JPasswordField();
        formPanel.add(currentPassField);

        formPanel.add(new JLabel("New Password:"));
        newPassField = new JPasswordField();
        formPanel.add(newPassField);

        formPanel.add(new JLabel("Confirm New Password:"));
        confirmNewPassField = new JPasswordField();
        formPanel.add(confirmNewPassField);

        JButton saveBtn = new JButton("Reset Password");
        saveBtn.setBackground(new Color(138, 43, 226));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmNewPassField.getPassword());
            
            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, "New password must be at least 6 characters.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Password reset successful! (DB logic and validation required)", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            parentFrame.setVisible(true);
        });
        
        formPanel.add(new JLabel("")); 
        formPanel.add(saveBtn);
        
        add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
