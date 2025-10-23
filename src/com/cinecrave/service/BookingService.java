package com.cinecrave.service;

import com.cinecrave.dao.UserDAO;
import com.cinecrave.dao.MovieDAO;
import com.cinecrave.dao.ShowDAO;
import com.cinecrave.dao.BookingDAO;
import com.cinecrave.model.User;
import com.cinecrave.model.Movie;
import com.cinecrave.model.Show;
import com.cinecrave.model.Booking;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookingService {
    
    private final UserDAO userDAO;
    private final MovieDAO movieDAO;
    private final ShowDAO showDAO;
    private final BookingDAO bookingDAO;

    public BookingService() {
        this.userDAO = new UserDAO();
        this.movieDAO = new MovieDAO();
        this.showDAO = new ShowDAO();
        this.bookingDAO = new BookingDAO();
    }

    public User authenticateUser(String email, String password) throws Exception {
        try {
            return userDAO.authenticate(email, password);
        } catch (SQLException e) {
            System.err.println("Authentication Database Error: " + e.getMessage());
            throw new Exception("A system error occurred during login verification.", e); 
        }
    }

    public boolean signUpCustomer(String name, String email, String phone, String password) throws Exception {
        try {
            return userDAO.register(name, email, phone, password); 
        } catch (SQLException e) {
            System.err.println("Registration Database Error: " + e.getMessage());
            
            if (e.getSQLState().startsWith("23")) { 
                throw new Exception("Registration failed. Email is already in use.", e);
            }
            throw new Exception("A system error occurred during registration.", e); 
        }
    }
    
    public boolean updateProfile(int userId, String name, String phone) throws Exception {
        System.out.println("SERVICE CONTRACT: User " + userId + " profile update called.");
        return true; 
    }
    
    public boolean resetPassword(int userId, String newPassword) throws Exception {
        System.out.println("SERVICE CONTRACT: User " + userId + " password reset called.");
        return true; 
    }

    public List<Movie> getAllAvailableMovies() {
        try {
            return movieDAO.getAllMovies();
        } catch (SQLException e) {
            System.err.println("Database Error fetching movies: " + e.getMessage());
            return Collections.emptyList(); 
        }
    }
    
    public List<Movie> searchMoviesByTitle(String title) {
        if (title == null || title.trim().isEmpty() || title.equals("Search movies....")) {
            return getAllAvailableMovies();
        }
        try {
            return movieDAO.searchMoviesByTitle(title);
        } catch (SQLException e) {
            System.err.println("Database Error searching movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Show> getShowtimesByMovieId(int movieId) {
        try {
            return showDAO.getShowtimesByMovieId(movieId);
        } catch (SQLException e) {
            System.err.println("Database Error fetching showtimes: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<Booking> getBookingHistory(int customerId) {
        try {
            return bookingDAO.getBookingsByCustomerId(customerId);
        } catch (SQLException e) {
            System.err.println("Database Error fetching booking history: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public int processBooking(int customerId, Show show, List<String> seatNumbers, double totalPrice) throws Exception {
        try {
            return bookingDAO.createBooking(customerId, show, seatNumbers, totalPrice);
        } catch (SQLException e) {
            System.err.println("Database Error during booking process: " + e.getMessage());
            throw new Exception("Booking failed due to a system error.", e);
        }
    }
}
