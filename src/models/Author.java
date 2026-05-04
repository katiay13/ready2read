package models;

public class Author {

    private int authorID;
    private String firstName;
    private String lastName;
    private String bio;

    public Author(int authorID, String firstName, String lastName, String bio) {
        this.authorID  = authorID;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.bio       = bio;
    }

    public int getAuthorID()      { return authorID; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getBio()        { return bio; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName)   { this.lastName  = lastName; }
    public void setBio(String bio)             { this.bio       = bio; }
}
