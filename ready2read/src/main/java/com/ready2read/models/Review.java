package com.ready2read.models;

import java.time.LocalDateTime;

public class Review {

    private int reviewID;
    private int userID;
    private int bookID;
    private int rating;
    private String reviewText;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String username;
    private String bookTitle;

    public Review(int reviewID, int userID, int bookID, int rating,
                  String reviewText, LocalDateTime dateCreated, LocalDateTime dateModified) {
        this.reviewID     = reviewID;
        this.userID       = userID;
        this.bookID       = bookID;
        this.rating       = rating;
        this.reviewText   = reviewText;
        this.dateCreated  = dateCreated;
        this.dateModified = dateModified;
    }

    public int getReviewID()                    { return reviewID; }
    public int getUserID()                      { return userID; }
    public int getBookID()                      { return bookID; }
    public int getRating()                      { return rating; }
    public String getReviewText()               { return reviewText; }
    public LocalDateTime getDateCreated()       { return dateCreated; }
    public LocalDateTime getDateModified()      { return dateModified; }
    public String getUsername()                 { return username; }
    public String getBookTitle()                { return bookTitle; }

    public boolean isEdited() {
        return dateCreated != null && dateModified != null && dateModified.isAfter(dateCreated);
    }

    public void setRating(int rating)               { this.rating       = rating; }
    public void setReviewText(String reviewText)     { this.reviewText   = reviewText; }
    public void setDateModified(LocalDateTime date)  { this.dateModified = date; }
    public void setUsername(String username)         { this.username     = username; }
    public void setBookTitle(String bookTitle)       { this.bookTitle    = bookTitle; }
}
