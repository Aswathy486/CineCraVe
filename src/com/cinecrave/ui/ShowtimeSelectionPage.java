package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.cinecrave.model.Movie;
import com.cinecrave.model.Show;
import com.cinecrave.model.Customer;
import com.cinecrave.service.BookingService;

public class ShowtimeSelectionPage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private final Movie selectedMovie;
    private final JFrame parentFrame;

    public ShowtimeSelectionPage(JFrame parentFrame, Customer customer, BookingService service, Movie movie) {
        this.parentFrame = parentFrame;
        this.loggedInCustomer = customer;
        this.bookingService = service;
        this.selectedMovie = movie;

        setTitle("Showtimes for: " + movie.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        
        JLabel header = new JLabel("Available Showtimes for " + movie.getTitle(), SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        
        JPanel showtimeListPanel = createShowtimeListPanel();
        JScrollPane scrollPane = new JScrollPane(showtimeListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backBtn = new JButton("← Back to Movies");
        backBtn.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true); 
        });
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private JPanel createShowtimeListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Fetch the data
        List<Show> showtimes = bookingService.getShowtimesByMovieId(selectedMovie.getMovieId());

        if (showtimes.isEmpty()) {
            panel.add(new JLabel("No showtimes currently scheduled for this movie."));
            return panel;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d");
        
        for (Show show : showtimes) {
            JButton showtimeBtn = new JButton(
                "<html><b>" + show.getStartTime().format(timeFormatter) + "</b> (" + 
                show.getStartTime().format(dateFormatter) + 
                ") @ $" + String.format("%.2f", show.getBasePrice()) + "</html>"
            );
            showtimeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            showtimeBtn.setPreferredSize(new Dimension(450, 40));
            showtimeBtn.setMaximumSize(new Dimension(450, 40));
            showtimeBtn.setBackground(new Color(240, 240, 255)); 
            
            showtimeBtn.addActionListener(e -> {
                
                JOptionPane.showMessageDialog(this, 
                    "Selected Show: " + show.getStartTime().format(timeFormatter) + 
                    ".\nNext: Seat Selection."
                );
            });
            
            panel.add(showtimeBtn);
            panel.add(Box.createVerticalStrut(8));
        }

        return panel;
    }
}
