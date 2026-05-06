package com.ready2read.models;

import java.time.LocalDate;

public class User {

    private int userID;
    private String username;
    private String email;
    private String password;
    private String role;
    private LocalDate joinDate;
    private String profileBio;
    private String avatarURL;

    public User(int userID, String username, String email, String password,
                String role, LocalDate joinDate, String profileBio, String avatarURL) {
        this.userID     = userID;
        this.username   = username;
        this.email      = email;
        this.password   = password;
        this.role       = role;
        this.joinDate   = joinDate;
        this.profileBio = profileBio;
        this.avatarURL  = avatarURL;
    }

    public int getUserID()           { return userID; }
    public String getUsername()      { return username; }
    public String getEmail()         { return email; }
    public String getPassword()      { return password; }
    public String getRole()          { return role; }
    public LocalDate getJoinDate()   { return joinDate; }
    public String getProfileBio()    { return profileBio; }
    public String getAvatarURL()     { return avatarURL; }

    public void setUsername(String username)      { this.username   = username; }
    public void setEmail(String email)            { this.email      = email; }
    public void setPassword(String password)      { this.password   = password; }
    public void setRole(String role)              { this.role       = role; }
    public void setProfileBio(String profileBio)  { this.profileBio = profileBio; }
    public void setAvatarURL(String avatarURL)    { this.avatarURL  = avatarURL; }
}
