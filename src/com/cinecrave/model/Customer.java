package com.cinecrave.model;

import java.util.List;
import java.util.ArrayList; 


public class Customer extends User {

    private List<String> bookings; 

    public Customer(int userId, String name, String email, String phone) {
        super(userId, name, email, phone);
        this.bookings = new ArrayList<>();
    }

    public void addBooking(String bookingId) { 
        this.bookings.add(bookingId);
    }
   
    public List<String> getBookings() {
        return this.bookings;
    }
}
