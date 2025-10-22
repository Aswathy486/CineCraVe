package com.cinecrave.model;
public class Movie {
    private int movieId;
    private String title;
    private int durationMinutes;
    private String genre;
    private String language;
    private String rating;
    private String description;

    public Movie(int movieId, String title, int durationMinutes, String genre, String language, String rating, String description) {
        this.movieId = movieId;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
        this.description = description;
    }
    
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public String getRating() { return rating; }
    public String getDescription() { return description; }
}
