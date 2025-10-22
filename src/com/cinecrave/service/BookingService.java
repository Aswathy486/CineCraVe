package com.cinecrave.service;

import com.cinecrave.dao.UserDAO;
import com.cinecrave.dao.MovieDAO;
import com.cinecrave.model.User;
import com.cinecrave.model.Movie;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookingService {
    
    private final UserDAO userDAO;
    private final MovieDAO movieDAO;

    public BookingService() {
        this.userDAO = new UserDAO();
        this.movieDAO = new MovieDAO();
    }

    // --- User Management ---

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
            
            // Check for specific error codes like duplicate email (UNIQUE constraint violation)
            if (e.getSQLState().startsWith("23")) { 
                throw new Exception("Registration failed. Email is already in use.", e);
            }
            throw new Exception("A system error occurred during registration.", e); 
        }
    }
    
    // --- Movie Management ---

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
}