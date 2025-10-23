package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.cinecrave.model.Customer;
import com.cinecrave.model.Booking;
import com.cinecrave.service.BookingService;

public class BookingStatusPage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private final JFrame parentFrame;

    public BookingStatusPage(JFrame parentFrame, Customer customer, BookingService service) {
        this.parentFrame = parentFrame;
        this.loggedInCustomer = customer;
        this.bookingService = service;

        setTitle("My Bookings | " + customer.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Booking History", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

        JTextArea bookingListArea = createBookingDisplayArea();
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
    
    private JTextArea createBookingDisplayArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        StringBuilder sb = new StringBuilder();
        
        List<Booking> bookings = bookingService.getBookingHistory(loggedInCustomer.getUserId());
        
        if (bookings.isEmpty()) {
            sb.append("\n\n\n\t\t--- No Booking History Found ---");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a");
            
            sb.append(String.format("--- Booking History for %s (%d total) ---\n\n", 
                                    loggedInCustomer.getName(), bookings.size()));
            
            for (Booking b : bookings) {
                sb.append(String.format("  Booking ID: #CRAVE%d\n", b.getBookingId()));
                sb.append(String.format("  Show ID:    %d\n", b.getShowId()));
                sb.append(String.format("  Date/Time:  %s\n", b.getBookingTime().format(formatter)));
                sb.append(String.format("  Seats:      %s\n", b.getSeatNumbers() != null ? b.getSeatNumbers() : "N/A"));
                sb.append(String.format("  Total:      ₹%.2f\n", b.getTotalPrice()));
                sb.append(String.format("  Status:     %s\n", b.getStatus()));
                sb.append("---------------------------------------------------\n");
            }
        }
        area.setText(sb.toString());
        return area;
    }
}
