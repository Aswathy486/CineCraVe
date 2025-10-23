package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.model.Movie;
import com.cinecrave.model.Customer;
import com.cinecrave.service.BookingService;

public class MovieDetailPage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private final Movie selectedMovie;
    private final JFrame parentFrame;

    public MovieDetailPage(JFrame parentFrame, Customer customer, BookingService service, Movie movie) {
        this.parentFrame = parentFrame;
        this.loggedInCustomer = customer;
        this.bookingService = service;
        this.selectedMovie = movie;

        setTitle(movie.getTitle() + " - Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10));

        
        JLabel header = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 30));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        header.setForeground(new Color(75, 0, 130)); 
        add(header, BorderLayout.NORTH);

       
        JPanel centerPanel = createDetailsPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        
        JPanel southPanel = createActionBar();
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private JPanel createDetailsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(250, 400));
        
        JLabel poster = new JLabel("  [Movie Poster]  ", SwingConstants.CENTER);
        poster.setPreferredSize(new Dimension(220, 300));
        poster.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        poster.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel runtime = new JLabel("Runtime: " + selectedMovie.getDurationMinutes() + " mins");
        JLabel genre = new JLabel("Genre: " + selectedMovie.getGenre());
        JLabel language = new JLabel("Language: " + selectedMovie.getLanguage());
        JLabel rating = new JLabel("Rating: " + selectedMovie.getRating());

        runtime.setFont(new Font("Arial", Font.ITALIC, 14));
        genre.setFont(new Font("Arial", Font.ITALIC, 14));
        language.setFont(new Font("Arial", Font.ITALIC, 14));
        rating.setFont(new Font("Arial", Font.ITALIC, 14));
        
        infoPanel.add(poster);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(runtime);
        infoPanel.add(genre);
        infoPanel.add(language);
        infoPanel.add(rating);
        
        mainPanel.add(infoPanel, BorderLayout.WEST);

        
        JTextArea descriptionArea = new JTextArea(selectedMovie.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Synopsis"));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }
    
    private JPanel createActionBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backBtn = new JButton("← Back to Home");
        backBtn.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });
        
        JButton showtimesBtn = new JButton("View Showtimes →");
        showtimesBtn.setBackground(new Color(255, 165, 0)); 
        showtimesBtn.setForeground(Color.BLACK);
        showtimesBtn.setFont(new Font("Arial", Font.BOLD, 16));
        showtimesBtn.addActionListener(e -> {
            
            dispose();
            new ShowtimeSelectionPage(
                parentFrame,
                loggedInCustomer,
                bookingService,
                selectedMovie
            );
        });

        panel.add(backBtn);
        panel.add(showtimesBtn);
        return panel;
    }
}
