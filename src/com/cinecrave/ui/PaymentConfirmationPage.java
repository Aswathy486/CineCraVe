package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.model.Customer;
import com.cinecrave.service.BookingService;

public class PaymentConfirmationPage extends JFrame {

    public PaymentConfirmationPage(Customer customer, BookingService service, String bookingId) {
        setTitle("Payment Successful | Booking ID: " + bookingId);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 255, 240));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        centerPanel.setBackground(new Color(240, 255, 240));

        JLabel iconLabel = new JLabel("✅", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("BOOKING CONFIRMED!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 22));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel idLabel = new JLabel("Your Booking ID: " + bookingId, SwingConstants.CENTER);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        idLabel.setForeground(new Color(75, 0, 130));

        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(messageLabel);
        centerPanel.add(idLabel);
        
        add(centerPanel, BorderLayout.CENTER);

        JButton homeBtn = new JButton("Return to Home");
        homeBtn.setBackground(new Color(138, 43, 226));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.addActionListener(e -> {
            dispose();
            new HomePage(customer, service);
        });
        JPanel southPanel = new JPanel();
        southPanel.add(homeBtn);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
