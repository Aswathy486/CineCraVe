package com.cinecrave.model;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int customerId;
    private int showId;
    private LocalDateTime bookingTime;
    private double totalPrice;
    private String status;
    private String seatNumbers;

    public Booking(int bookingId, int customerId, int showId, LocalDateTime bookingTime, double totalPrice, String status, String seatNumbers) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.showId = showId;
        this.bookingTime = bookingTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.seatNumbers = seatNumbers;
    }

    public int getBookingId() { return bookingId; }
    public int getCustomerId() { return customerId; }
    public int getShowId() { return showId; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getSeatNumbers() { return seatNumbers; }
}
