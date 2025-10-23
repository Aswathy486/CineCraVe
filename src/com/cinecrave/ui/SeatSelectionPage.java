package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.cinecrave.model.Customer;
import com.cinecrave.model.Show;
import com.cinecrave.service.BookingService;

public class SeatSelectionPage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private final Show selectedShow;
    
    private final Map<String, JButton> seatButtons = new HashMap<>();
    private final List<String> selectedSeats = new ArrayList<>();
    private JLabel totalLabel;
    
    private static final int ROWS = 10;
    private static final int COLS = 15;

    public SeatSelectionPage(Customer customer, BookingService service, Show show) {
        this.loggedInCustomer = customer;
        this.bookingService = service;
        this.selectedShow = show;

        setTitle("Select Seats for " + show.getShowId());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        
        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 20));
        screenLabel.setForeground(Color.WHITE);
        screenLabel.setBackground(Color.BLACK);
        screenLabel.setOpaque(true);
        screenLabel.setPreferredSize(new Dimension(1000, 40));
        add(screenLabel, BorderLayout.NORTH);

        
        JPanel seatGridPanel = createSeatGridPanel();
        add(new JScrollPane(seatGridPanel), BorderLayout.CENTER);

        
        JPanel actionBar = createActionBar();
        add(actionBar, BorderLayout.SOUTH);

        simulateSeatMap(); 
        
        setVisible(true);
    }
    
    private void simulateSeatMap() {
        
        String[] booked = {"C5", "C6", "D1"};
        for (String seatId : booked) {
            JButton btn = seatButtons.get(seatId);
            if (btn != null) {
                btn.setBackground(Color.RED);
                btn.setEnabled(false);
                btn.setToolTipText(seatId + " - BOOKED");
            }
        }
    }
    
    private JPanel createSeatGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(ROWS + 1, COLS + 1, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       
        gridPanel.add(new JLabel("")); 
        for (int c = 1; c <= COLS; c++) {
            JLabel header = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 12));
            gridPanel.add(header);
        }

        
        for (int r = 0; r < ROWS; r++) {
            char rowChar = (char) ('A' + r);
            
            
            JLabel rowHeader = new JLabel(String.valueOf(rowChar), SwingConstants.CENTER);
            rowHeader.setFont(new Font("Arial", Font.BOLD, 12));
            gridPanel.add(rowHeader); 

            
            for (int c = 1; c <= COLS; c++) {
                String seatId = String.valueOf(rowChar) + c;
                JButton seatBtn = new JButton(String.valueOf(c));
                
                
                if (r < 2) { 
                    seatBtn.setBackground(new Color(150, 200, 255));
                    seatBtn.setToolTipText(seatId + " (PREMIUM) - ₹250.00");
                } else {
                    seatBtn.setBackground(Color.LIGHT_GRAY);
                    seatBtn.setToolTipText(seatId + " (REGULAR) - ₹150.00");
                }

                seatBtn.setPreferredSize(new Dimension(60, 40));
                seatBtn.addActionListener(new SeatActionListener(seatId));
                
                seatButtons.put(seatId, seatBtn);
                gridPanel.add(seatBtn);
            }
        }
        return gridPanel;
    }
    
    private JPanel createActionBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        
        totalLabel = new JLabel("Total: ₹0.00 (0 Seats Selected)");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(totalLabel, BorderLayout.WEST);

        
        JButton bookBtn = new JButton("Proceed to Payment");
        bookBtn.setBackground(new Color(75, 0, 130));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            dispose();
            new PaymentConfirmationPage(
                loggedInCustomer,
                bookingService,
                selectedSeats.size() + "-" + selectedShow.getShowId() 
            );
        });
        
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.add(bookBtn);
        panel.add(buttonWrapper, BorderLayout.EAST);
        
        return panel;
    }
    
    private class SeatActionListener implements ActionListener {
        private final String seatId;

        public SeatActionListener(String seatId) {
            this.seatId = seatId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            
            
            boolean isPremium = seatId.charAt(0) < 'C';
            
            if (selectedSeats.contains(seatId)) {
                
                selectedSeats.remove(seatId);
                btn.setBackground(isPremium ? new Color(150, 200, 255) : Color.LIGHT_GRAY);
                btn.setBorder(BorderFactory.createEmptyBorder());
            } else {
               
                selectedSeats.add(seatId);
                btn.setBackground(Color.GREEN);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
            
            updateTotal();
        }
    }
    
    private void updateTotal() {
        double currentTotal = 0.0;
        double baseShowPrice = selectedShow.getBasePrice();
        
        for (String seatId : selectedSeats) {
            
            double seatModifier = (seatId.charAt(0) < 'C') ? 250.00 : 150.00; 
            currentTotal += baseShowPrice + seatModifier; 
        }
        
        totalLabel.setText(String.format("Total: ₹%.2f (%d Seats Selected)", currentTotal, selectedSeats.size()));
    }
}
