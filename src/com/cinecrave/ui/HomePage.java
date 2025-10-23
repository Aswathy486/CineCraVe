package com.cinecrave.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.cinecrave.model.Customer;
import com.cinecrave.model.Movie;
import com.cinecrave.service.BookingService;

import java.util.List;

public class HomePage extends JFrame {

    private final Customer loggedInCustomer;
    private final BookingService bookingService;
    private JPanel movieGridPanel;
    private final JPanel contentPanel;

    public HomePage(Customer customer, BookingService service) {
        this.loggedInCustomer = customer;
        this.bookingService = service;

        setTitle("CineCraVe - Home | Welcome, " + customer.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600)); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(); 
        contentPanel.setLayout(new BorderLayout(10, 10)); 
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filterAndSearchPanel = createFilterAndSearchPanel();
        contentPanel.add(filterAndSearchPanel, BorderLayout.NORTH);

        movieGridPanel = new JPanel(); 
        contentPanel.add(movieGridPanel, BorderLayout.CENTER);
        
        loadMoviesToGrid(bookingService.getAllAvailableMovies()); 

        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(138, 43, 226)); 
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("🍿 CineCraVe", SwingConstants.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        panel.add(logo, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navPanel.setOpaque(false); 

        String[] navItems = {"Home", "My Bookings", "Profile"};
        for (String item : navItems) {
            JLabel label = new JLabel(item);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    HomePage.this.setVisible(false);

                    if (item.equals("My Bookings")) {
                        new BookingStatusPage(HomePage.this, loggedInCustomer, bookingService);
                    } else if (item.equals("Profile")) {
                        new ProfilePage(HomePage.this, loggedInCustomer, bookingService);
                    } else {
                        HomePage.this.setVisible(true);
                    }
                }
            });
            navPanel.add(label);
        }
        panel.add(navPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFilterAndSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JTextField searchField = new JTextField("Search movies....");
        searchField.setPreferredSize(new Dimension(800, 30));
        searchField.setMaximumSize(new Dimension(800, 30));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(255, 165, 0), 2));
        searchField.setHorizontalAlignment(JTextField.LEFT);
        
        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchWrapper.add(searchField);
        panel.add(searchWrapper);
        
        panel.add(Box.createVerticalStrut(15)); 
        
        searchField.addActionListener(e -> {
            String query = searchField.getText();
            List<Movie> results = bookingService.searchMoviesByTitle(query);
            loadMoviesToGrid(results);
        });
        
        JPanel filtersWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); 
        
        String[] genres = {"Action", "Comedy", "Thriller", "Romance"};
        String[] languages = {"English", "Hindi", "Tamil", "Telugu"};
        String[] ratings = {"G", "PG", "PG-13", "R"};

        filtersWrapper.add(createFilterButton("Genre", genres));
        filtersWrapper.add(createFilterButton("Language", languages));
        filtersWrapper.add(createFilterButton("Rating", ratings));

        JButton showtimesBtn = new JButton("View Showtimes..");
        showtimesBtn.setBackground(new Color(138, 43, 226));
        showtimesBtn.setForeground(Color.WHITE);
        filtersWrapper.add(showtimesBtn);
        
        panel.add(filtersWrapper);
        panel.add(Box.createVerticalStrut(15)); 

        return panel;
    }
    
    private JButton createFilterButton(String label, String[] options) {
        JButton button = new JButton(label);
        button.setBackground(new Color(255, 165, 0)); 
        button.setForeground(Color.BLACK);

        button.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            for (String option : options) {
                JMenuItem item = new JMenuItem(option);
                item.addActionListener(ae -> {
                    button.setText(label + ": " + option); 
                });
                menu.add(item);
            }
            menu.show(button, 0, button.getHeight());
        });
        return button;
    }

    private void loadMoviesToGrid(List<Movie> movies) {
        movieGridPanel.removeAll(); 
        
        int numMovies = movies.size();
        int columns = 4;
        int rows = (numMovies + columns - 1) / columns; 
        
        movieGridPanel.setLayout(new GridLayout(rows, columns, 15, 15));
        movieGridPanel.setBorder(BorderFactory.createTitledBorder("Now Showing (" + numMovies + " titles)..."));

        if (movies.isEmpty() && numMovies == 0) {
            movieGridPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            movieGridPanel.add(new JLabel("No movies found matching your criteria."));
        } else {
            for (Movie movie : movies) {
                movieGridPanel.add(createMovieCard(movie));
            }
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 2));
        card.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("<html><b>" + movie.getTitle() + "</b></html>", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel details = new JLabel(movie.getGenre() + " | " + movie.getDurationMinutes() + " min");
        details.setFont(new Font("Arial", Font.PLAIN, 12));
        details.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel poster = new JLabel("  [Poster]  ", SwingConstants.CENTER);
        poster.setPreferredSize(new Dimension(150, 200));
        poster.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        poster.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(Box.createVerticalStrut(5));
        card.add(poster); 
        card.add(Box.createVerticalStrut(5));
        card.add(title);
        card.add(details);
        card.add(Box.createVerticalStrut(5));
        
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HomePage.this.setVisible(false); 
                
                new MovieDetailPage( 
                    HomePage.this, 
                    loggedInCustomer, 
                    bookingService, 
                    movie 
                );
            }
        });
        
        return card;
    }
}
