USE ready2read;

-- Authors
INSERT INTO Authors (FirstName, LastName, Bio) VALUES
('George', 'Orwell', 'English novelist and essayist known for his sharp criticism of totalitarianism.'),
('Harper', 'Lee', 'American novelist best known for To Kill a Mockingbird.'),
('F. Scott', 'Fitzgerald', 'American novelist of the Jazz Age.'),
('Suzanne', 'Collins', 'American author best known for The Hunger Games trilogy.'),
('J.K.', 'Rowling', 'British author best known for the Harry Potter series.');

-- Books
INSERT INTO Books (Title, AuthorID, Genre, PublishedYear, ISBN, Description) VALUES
('1984', 1, 'Dystopian', 1949, '9780451524935', 'A chilling portrait of a totalitarian society.'),
('To Kill a Mockingbird', 2, 'Fiction', 1960, '9780061935466', 'A story of racial injustice and moral growth in the American South.'),
('The Great Gatsby', 3, 'Fiction', 1925, '9780743273565', 'A tale of wealth, ambition, and the American Dream.'),
('The Hunger Games', 4, 'Dystopian', 2008, '9780439023481', 'A teenager fights for survival in a televised death match.'),
('Harry Potter and the Sorcerers Stone', 5, 'Fantasy', 1997, '9780590353427', 'A young boy discovers he is a wizard and enrolls in Hogwarts.');

-- Users (3 personas)
-- Persona 1: classic lit reader
-- Persona 2: casual YA reader
-- Persona 3: heavy reader who tracks everything
INSERT INTO Users (Username, Email, Password, JoinDate, ProfileBio) VALUES
('purpleturtle', 'john@email.com', 'hashed_password', '2024-01-10', 'Don''t talk to me until I''ve had my coffee.'),
('iluvreading', 'sam@email.com', 'hashed_password', '2024-03-22', 'team peeta <3'),
('booknerd99', 'mary@email.com', 'hashed_password', '2023-11-05', 'eat, read, sleep.');

-- Reviews
INSERT INTO Reviews (UserID, BookID, Rating, ReviewText) VALUES
(1, 1, 5, 'One of the most important books ever written. Orwell was ahead of his time.'),
(1, 2, 4, 'A beautifully written story. Atticus Finch is one of literature greatest characters.'),
(1, 3, 3, 'Gorgeous prose but I found Gatsby himself unlikeable, which maybe is the point.'),
(2, 4, 5, 'Could not put it down. Read the whole thing in two days.'),
(2, 5, 5, 'A childhood favorite I keep coming back to. Magical every time.'),
(3, 1, 4, 'Brilliant but bleak. Not a fun read but an essential one.'),
(3, 3, 5, 'The green light, the parties, the tragedy. A perfect novel.'),
(3, 5, 4, 'Still holds up. The world-building is unmatched for a debut novel.');

-- Reading Lists
INSERT INTO ReadingList (UserID, BookID, Status) VALUES
-- purpleturtle
(1, 1, 'Finished'),
(1, 2, 'Finished'),
(1, 3, 'Finished'),
(1, 4, 'Want to Read'),
-- iluvreading
(2, 4, 'Finished'),
(2, 5, 'Finished'),
(2, 1, 'Currently Reading'),
-- booknerd99
(3, 1, 'Finished'),
(3, 2, 'Want to Read'),
(3, 3, 'Finished'),
(3, 5, 'Finished');
