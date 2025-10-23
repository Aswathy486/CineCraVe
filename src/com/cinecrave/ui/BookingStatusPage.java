package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.model.Customer;
import com.cinecrave.service.BookingService;

public class BookingStatusPage extends JFrame {

    private final Customer loggedInCustomer;
    private final JFrame parentFrame;

    public BookingStatusPage(JFrame parentFrame, Customer customer) {
        this.parentFrame = parentFrame;
        this.loggedInCustomer = customer;

        setTitle("My Bookings | " + customer.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Booking History", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

        JTextArea bookingListArea = new JTextArea();
        bookingListArea.setEditable(false);
        bookingListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        bookingListArea.setText(
            "--- Booking Data for " + customer.getName() + " ---\n\n" +
            "Booking ID: #CRAVE1001 | Show: Galactic Odyssey\n" +
            "Seats: B5, B6 | Total: ₹500.00 | Status: CONFIRMED\n" +
            "---------------------------------------------------\n" +
            "Booking ID: #CRAVE1002 | Show: Mountain Mystery\n" +
            "Seats: E1, E2 | Total: ₹350.00 | Status: CANCELLED\n"
        );
        add(new JScrollPane(bookingListArea), BorderLayout.CENTER);

        JButton backBtn = new JButton("← Back to Home");
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
