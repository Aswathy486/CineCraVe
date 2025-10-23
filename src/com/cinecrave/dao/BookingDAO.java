package com.cinecrave.dao;

import com.cinecrave.model.Booking;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private static final String SELECT_BY_CUSTOMER_ID_SQL = 
        "SELECT b.booking_id, b.customer_id, b.show_id, b.booking_time, b.total_price, b.status, GROUP_CONCAT(bs.seat_number) AS seats " +
        "FROM Booking b JOIN BookedSeat bs ON b.booking_id = bs.booking_id " +
        "WHERE b.customer_id = ? GROUP BY b.booking_id ORDER BY b.booking_time DESC";

    public List<Booking> getBookingsByCustomerId(int customerId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CUSTOMER_ID_SQL)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime bookingTime = rs.getTimestamp("booking_time").toLocalDateTime();

                    Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("show_id"),
                        bookingTime,
                        rs.getDouble("total_price"),
                        rs.getString("status"),
                        rs.getString("seats")
                    );
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
}
