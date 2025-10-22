package com.cinecrave.model;
import java.time.LocalDateTime;

public class Show {
    private int showId;
    private int movieId;
    private int screenId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double basePrice;

    public Show(int showId, int movieId, int screenId, LocalDateTime startTime, LocalDateTime endTime, double basePrice) {
        this.showId = showId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.basePrice = basePrice;
    }

    public int getShowId() { return showId; }
    public int getMovieId() { return movieId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getBasePrice() { return basePrice; }
}
