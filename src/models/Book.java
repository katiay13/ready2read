package models;

public class Book {

    private int bookID;
    private String title;
    private String author;
    private String genre;
    private int publishedYear;
    private String isbn;
    private String description;

    public Book(int bookID, String title, String author, String genre,
                int publishedYear, String isbn, String description) {
        this.bookID        = bookID;
        this.title         = title;
        this.author        = author;
        this.genre         = genre;
        this.publishedYear = publishedYear;
        this.isbn          = isbn;
        this.description   = description;
    }

    public int getBookID()           { return bookID; }
    public String getTitle()         { return title; }
    public String getAuthor()        { return author; }
    public String getGenre()         { return genre; }
    public int getPublishedYear()    { return publishedYear; }
    public String getIsbn()          { return isbn; }
    public String getDescription()   { return description; }

    public void setTitle(String title)               { this.title         = title; }
    public void setAuthor(String author)             { this.author        = author; }
    public void setGenre(String genre)               { this.genre         = genre; }
    public void setPublishedYear(int publishedYear)  { this.publishedYear = publishedYear; }
    public void setIsbn(String isbn)                 { this.isbn          = isbn; }
    public void setDescription(String description)   { this.description   = description; }
}
