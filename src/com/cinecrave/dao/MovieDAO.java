package com.cinecrave.dao;
import com.cinecrave.model.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MovieDAO {

    private static final String SELECT_ALL_SQL = "SELECT movie_id, title, duration_minutes, genre, language, rating, description FROM Movie";

    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                movies.add(new Movie(
                    rs.getInt("movie_id"), rs.getString("title"), rs.getInt("duration_minutes"),
                    rs.getString("genre"), rs.getString("language"), rs.getString("rating"), rs.getString("description")
                ));
            }
        }
        return movies;
    }

    public List<Movie> searchMoviesByTitle(String searchTitle) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movie WHERE title LIKE ?"; 
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + searchTitle + "%"); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(new Movie(
                        rs.getInt("movie_id"), rs.getString("title"), rs.getInt("duration_minutes"),
                        rs.getString("genre"), rs.getString("language"), rs.getString("rating"), rs.getString("description")
                    ));
                }
            }
        }
        return movies;
    }
}
