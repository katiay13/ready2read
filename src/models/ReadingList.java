package models;

import java.time.LocalDateTime;

public class ReadingList {

    public enum Status {
        WANT_TO_READ("Want to Read"),
        CURRENTLY_READING("Currently Reading"),
        FINISHED("Finished");

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

    public void setStatus(Status status)                  { this.status       = status; }
    public void setDateFinished(LocalDateTime dateFinished) { this.dateFinished = dateFinished; }
}
