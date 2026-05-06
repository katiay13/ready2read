USE ready2read;

-- Admin account
INSERT INTO Users (Username, Email, Password, Role, JoinDate)
VALUES ('admin', 'admin@ready2read.com', 'admin123', 'admin', CURDATE());

-- Users (3 personas)
-- Persona 1: classic lit reader
-- Persona 2: casual YA reader
-- Persona 3: heavy reader who tracks everything
INSERT INTO Users (Username, Email, Password, JoinDate, ProfileBio) VALUES
('purpleturtle', 'john@email.com', 'hashed_password', '2024-01-10', 'Don''t talk to me until I''ve had my coffee.'),
('iluvreading', 'sam@email.com', 'hashed_password', '2024-03-22', 'team peeta <3'),
('booknerd99', 'mary@email.com', 'hashed_password', '2023-11-05', 'eat, read, sleep.');

-- Books
INSERT INTO Books (Title, Author, Genre, PublishedYear, ISBN, Description) VALUES
('1984', 'George Orwell', 'Dystopian', 1949, '9780451524935', 'A chilling portrait of a totalitarian society.'),
('To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960, '9780061935466', 'A story of racial injustice and moral growth in the American South.'),
('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', 1925, '9780743273565', 'A tale of wealth, ambition, and the American Dream.'),
('The Hunger Games', 'Suzanne Collins', 'Dystopian', 2008, '9780439023481', 'A teenager fights for survival in a televised death match.'),
('Harry Potter and the Sorcerers Stone', 'J.K. Rowling', 'Fantasy', 1997, '9780590353427', 'A young boy discovers he is a wizard and enrolls in Hogwarts.');

-- Reviews
INSERT INTO Reviews (UserID, BookID, Rating, ReviewText) VALUES
(2, 1, 5, 'One of the most important books ever written. Orwell was ahead of his time.'),
(2, 2, 4, 'A beautifully written story. Atticus Finch is one of literature greatest characters.'),
(2, 3, 3, 'Gorgeous prose but I found Gatsby himself unlikeable, which maybe is the point.'),
(3, 4, 5, 'Could not put it down. Read the whole thing in two days.'),
(3, 5, 5, 'A childhood favorite I keep coming back to. Magical every time.'),
(4, 1, 4, 'Brilliant but bleak. Not a fun read but an essential one.'),
(4, 3, 5, 'The green light, the parties, the tragedy. A perfect novel.'),
(4, 5, 4, 'Still holds up. The world-building is unmatched for a debut novel.');

-- Reading Lists
INSERT INTO ReadingList (UserID, BookID, Status) VALUES
-- purpleturtle
(2, 1, 'finished'),
(2, 2, 'finished'),
(2, 3, 'finished'),
(2, 4, 'want_to_read'),
-- iluvreading
(3, 4, 'finished'),
(3, 5, 'finished'),
(3, 1, 'currently_reading'),
-- booknerd99
(4, 1, 'finished'),
(4, 2, 'want_to_read'),
(4, 3, 'finished'),
(4, 5, 'finished');
