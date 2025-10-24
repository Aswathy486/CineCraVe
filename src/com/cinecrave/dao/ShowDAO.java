package com.cinecrave.dao;
import com.cinecrave.model.Show;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {
    public List<Show> getShowtimesByMovieId(int movieId) throws SQLException {
        List<Show> shows = new ArrayList<>();
        String sql = "SELECT * FROM `Show` WHERE movie_id = ? AND start_time >= NOW() ORDER BY start_time";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, movieId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                   
                    LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                    
                    Show show = new Show(
                        rs.getInt("show_id"),
                        rs.getInt("movie_id"),
                        rs.getInt("screen_id"),
                        startTime,
                        endTime,
                        rs.getDouble("base_price")
                    );
                    shows.add(show);
                }
            }
        }
        return shows;
    }
}
