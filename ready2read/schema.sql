CREATE DATABASE IF NOT EXISTS ready2read;
USE ready2read;

DROP TABLE IF EXISTS ReadingList;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
    UserID      INT          AUTO_INCREMENT PRIMARY KEY,
    Username    VARCHAR(50)  NOT NULL UNIQUE,
    Email       VARCHAR(100) NOT NULL UNIQUE,
    Password    VARCHAR(255) NOT NULL,
    Role        ENUM('admin', 'user') NOT NULL DEFAULT 'user',
    JoinDate    DATE         NOT NULL DEFAULT (CURRENT_DATE),
    ProfileBio  VARCHAR(2000) NOT NULL DEFAULT '',
    AvatarURL   VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE Books (
    BookID        INT          AUTO_INCREMENT PRIMARY KEY,
    Title         VARCHAR(255) NOT NULL,
    Author        VARCHAR(255) NOT NULL,
    Genre         VARCHAR(100) NOT NULL,
    PublishedYear SMALLINT     NOT NULL CHECK (PublishedYear BETWEEN 1 AND 2100),
    ISBN          VARCHAR(20)  NOT NULL UNIQUE,
    Description   TEXT         NOT NULL
);

CREATE TABLE Reviews (
    ReviewID     INT      AUTO_INCREMENT PRIMARY KEY,
    UserID       INT      NOT NULL,
    BookID       INT      NOT NULL,
    Rating       TINYINT  NOT NULL CHECK (Rating BETWEEN 1 AND 5),
    ReviewText   TEXT     NOT NULL,
    DateCreated  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DateModified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (UserID, BookID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ReadingList (
    EntryID      INT      AUTO_INCREMENT PRIMARY KEY,
    UserID       INT      NOT NULL,
    BookID       INT      NOT NULL,
    Status       ENUM('want_to_read', 'currently_reading', 'finished')
                          NOT NULL DEFAULT 'want_to_read',
    DateAdded    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (UserID, BookID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
        ON DELETE CASCADE ON UPDATE CASCADE
);
