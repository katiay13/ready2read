package models;

import java.time.LocalDateTime;

public class ReadingList {

    public enum Status {
        WANT_TO_READ("want_to_read"),
        CURRENTLY_READING("currently_reading"),
        FINISHED("finished");

        private final String value;

        Status(String value)       { this.value = value; }
        public String getValue()   { return value; }
    }

    private int entryID;
    private int userID;
    private int bookID;
    private Status status;
    private LocalDateTime dateAdded;
    private LocalDateTime dateFinished;
    private String bookTitle;
    private String bookAuthor;
    private String bookGenre;

    public ReadingList(int entryID, int userID, int bookID, Status status,
                       LocalDateTime dateAdded, LocalDateTime dateFinished) {
        this.entryID      = entryID;
        this.userID       = userID;
        this.bookID       = bookID;
        this.status       = status;
        this.dateAdded    = dateAdded;
        this.dateFinished = dateFinished;
    }

    public int getEntryID()                  { return entryID; }
    public int getUserID()                   { return userID; }
    public int getBookID()                   { return bookID; }
    public Status getStatus()                { return status; }
    public LocalDateTime getDateAdded()      { return dateAdded; }
    public LocalDateTime getDateFinished()   { return dateFinished; }
    public String getBookTitle()             { return bookTitle; }
    public String getBookAuthor()            { return bookAuthor; }
    public String getBookGenre()             { return bookGenre; }

    public void setStatus(Status status)                    { this.status       = status; }
    public void setDateFinished(LocalDateTime dateFinished) { this.dateFinished = dateFinished; }
    public void setBookTitle(String bookTitle)              { this.bookTitle    = bookTitle; }
    public void setBookAuthor(String bookAuthor)            { this.bookAuthor   = bookAuthor; }
    public void setBookGenre(String bookGenre)              { this.bookGenre    = bookGenre; }
}
