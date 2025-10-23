package com.cinecrave.dao;
import com.cinecrave.model.Show;
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
    
    private static final String INSERT_BOOKING_SQL = 
        "INSERT INTO Booking (customer_id, show_id, booking_time, total_price, status) VALUES (?, ?, NOW(), ?, 'CONFIRMED')";
    
    private static final String INSERT_BOOKED_SEAT_SQL = 
        "INSERT INTO BookedSeat (booking_id, show_id, seat_number, final_price, seat_status) VALUES (?, ?, ?, ?, 'BOOKED')";

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

    public int createBooking(int customerId, Show show, List<String> seatNumbers, double totalPrice) throws SQLException {
        Connection conn = null;
        int bookingId = -1;

        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement bookingStmt = conn.prepareStatement(INSERT_BOOKING_SQL, Statement.RETURN_GENERATED_KEYS);
            bookingStmt.setInt(1, customerId);
            bookingStmt.setInt(2, show.getShowId());
            bookingStmt.setDouble(3, totalPrice);
            bookingStmt.executeUpdate();

            ResultSet generatedKeys = bookingStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                bookingId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve auto-generated booking ID.");
            }
            
            PreparedStatement seatStmt = conn.prepareStatement(INSERT_BOOKED_SEAT_SQL);
            
            double baseShowPrice = show.getBasePrice();

            for (String seatId : seatNumbers) {
                double seatModifier = (seatId.charAt(0) < 'C') ? 250.00 : 150.00; 
                double finalSeatPrice = baseShowPrice + seatModifier;

                seatStmt.setInt(1, bookingId);
                seatStmt.setInt(2, show.getShowId());
                seatStmt.setString(3, seatId);
                seatStmt.setDouble(4, finalSeatPrice);
                seatStmt.addBatch();
            }
            seatStmt.executeBatch();

            conn.commit();
            return bookingId;
        } catch (SQLException e) {
            if (conn != null) { conn.rollback(); } 
            throw e;
        } finally {
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }
}
