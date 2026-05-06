USE ready2read;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE ReadingList;
TRUNCATE TABLE Reviews;
TRUNCATE TABLE Books;
TRUNCATE TABLE Users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- USERS  (1 admin + 14 regular users = 15 rows)
-- ============================================================
INSERT INTO Users (Username, Email, Password, Role, JoinDate, ProfileBio, AvatarURL) VALUES
('admin',        'admin@ready2read.com',     'admin123',    'admin', '2024-01-01', 'Platform administrator.',                           'https://api.dicebear.com/7.x/initials/svg?seed=Admin'),
('bookworm42',   'bookworm42@mail.com',      'password123', 'user',  '2024-02-14', 'Reading is my cardio.',                             'https://api.dicebear.com/7.x/initials/svg?seed=bookworm42'),
('pageturer',    'pageturer@mail.com',       'password123', 'user',  '2024-03-05', 'Always 5 books deep into a reading list.',          'https://api.dicebear.com/7.x/initials/svg?seed=pageturer'),
('litlover',     'litlover@mail.com',        'password123', 'user',  '2024-03-20', 'Classic literature and strong tea.',                'https://api.dicebear.com/7.x/initials/svg?seed=litlover'),
('readingqueen', 'readingqueen@mail.com',    'password123', 'user',  '2024-04-11', 'Fantasy and sci-fi are my happy place.',            'https://api.dicebear.com/7.x/initials/svg?seed=readingqueen'),
('novelseeker',  'novelseeker@mail.com',     'password123', 'user',  '2024-04-28', 'Looking for my next favourite book every day.',     'https://api.dicebear.com/7.x/initials/svg?seed=novelseeker'),
('classicfan',   'classicfan@mail.com',      'password123', 'user',  '2024-05-15', 'Nineteenth century fiction is underrated.',         'https://api.dicebear.com/7.x/initials/svg?seed=classicfan'),
('scifiguru',    'scifiguru@mail.com',       'password123', 'user',  '2024-06-01', 'Hard sci-fi, space opera, all of it.',              'https://api.dicebear.com/7.x/initials/svg?seed=scifiguru'),
('thrillerreader','thrillerreader@mail.com', 'password123', 'user',  '2024-06-22', 'I read thrillers so fast my library card is worn.', 'https://api.dicebear.com/7.x/initials/svg?seed=thrillerreader'),
('mysterymaniac','mysterymaniac@mail.com',   'password123', 'user',  '2024-07-08', 'The butler always did it.',                         'https://api.dicebear.com/7.x/initials/svg?seed=mysterymaniac'),
('fantasylover', 'fantasylover@mail.com',    'password123', 'user',  '2024-07-30', 'Give me dragons, magic, and chosen ones.',          'https://api.dicebear.com/7.x/initials/svg?seed=fantasylover'),
('romancereader','romancereader@mail.com',   'password123', 'user',  '2024-08-19', 'HEAs only, please.',                                'https://api.dicebear.com/7.x/initials/svg?seed=romancereader'),
('dystopiadave', 'dystopiadave@mail.com',    'password123', 'user',  '2024-09-03', 'Dystopian fiction hits different after the news.', 'https://api.dicebear.com/7.x/initials/svg?seed=dystopiadave'),
('nightreader',  'nightreader@mail.com',     'password123', 'user',  '2024-10-17', 'I read past midnight every single night.',          'https://api.dicebear.com/7.x/initials/svg?seed=nightreader'),
('wordsmith99',  'wordsmith99@mail.com',     'password123', 'user',  '2025-01-06', 'Amateur writer, full-time reader.',                 'https://api.dicebear.com/7.x/initials/svg?seed=wordsmith99');

-- ============================================================
-- BOOKS  (15 rows)
-- UserIDs above:  admin=1, bookworm42=2, pageturer=3, litlover=4,
--   readingqueen=5, novelseeker=6, classicfan=7, scifiguru=8,
--   thrillerreader=9, mysterymaniac=10, fantasylover=11,
--   romancereader=12, dystopiadave=13, nightreader=14, wordsmith99=15
-- ============================================================
INSERT INTO Books (Title, Author, Genre, PublishedYear, ISBN, Description) VALUES
('The Great Gatsby',                          'F. Scott Fitzgerald', 'Fiction',        1925, '9780743273565', 'A portrait of the Jazz Age through the eyes of narrator Nick Carraway and his mysterious neighbour Jay Gatsby. A meditation on the American Dream and its discontents.'),
('To Kill a Mockingbird',                     'Harper Lee',          'Fiction',        1960, '9780061935466', 'Lawyer Atticus Finch defends a Black man falsely accused of a crime in 1930s Alabama, seen through the eyes of his young daughter Scout. A landmark of American literature.'),
('1984',                                      'George Orwell',       'Dystopian',      1949, '9780451524935', 'Winston Smith struggles to retain his humanity in a totalitarian superstate ruled by Big Brother. One of the most influential dystopian novels ever written.'),
('The Hobbit',                                'J.R.R. Tolkien',      'Fantasy',        1937, '9780547928227', 'Hobbit Bilbo Baggins is swept into an epic quest to reclaim the dwarf kingdom of Erebor from the dragon Smaug. The prelude to The Lord of the Rings.'),
('Harry Potter and the Sorcerers Stone',      'J.K. Rowling',        'Fantasy',        1997, '9780590353427', 'Orphan Harry Potter discovers he is a wizard and begins his education at Hogwarts School of Witchcraft and Wizardry. The start of a global phenomenon.'),
('The Hunger Games',                          'Suzanne Collins',     'Dystopian',      2008, '9780439023481', 'Sixteen-year-old Katniss Everdeen volunteers to replace her sister in a televised fight-to-the-death competition. A gripping tale of survival, resistance, and media manipulation.'),
('Pride and Prejudice',                       'Jane Austen',         'Romance',        1813, '9780141439518', 'Elizabeth Bennet navigates love, class, and family expectations as she spars with the proud Mr. Darcy. Austen''s most beloved novel and a masterpiece of wit and social observation.'),
('The Alchemist',                             'Paulo Coelho',        'Fiction',        1988, '9780061122415', 'Santiago, an Andalusian shepherd boy, journeys to the Egyptian pyramids in search of treasure and discovers that the real journey is within. A modern fable about following your dreams.'),
('Dune',                                      'Frank Herbert',       'Science Fiction', 1965, '9780441013593', 'Paul Atreides navigates the treacherous politics of the desert planet Arrakis, the sole source of the universe''s most valuable substance. An epic of ecology, religion, and power.'),
('The Da Vinci Code',                         'Dan Brown',           'Mystery',        2003, '9780385504201', 'Symbologist Robert Langdon races across Europe to unravel a conspiracy hidden in works by Leonardo da Vinci. A page-turning thriller that sparked worldwide debate.'),
('Gone Girl',                                 'Gillian Flynn',       'Thriller',       2012, '9780307588371', 'On the morning of their fifth anniversary, Amy Dunne disappears and suspicion falls on her husband Nick. A twisting, darkly comic dissection of marriage and media.'),
('The Hitchhikers Guide to the Galaxy',       'Douglas Adams',       'Science Fiction', 1979, '9780345391803', 'Moments before Earth is demolished for a hyperspace bypass, Arthur Dent is swept into the galaxy by his alien friend Ford Prefect. A comic masterpiece of absurdist science fiction.'),
('A Study in Scarlet',                        'Arthur Conan Doyle',  'Mystery',        1887, '9780140439083', 'Dr. Watson meets the eccentric Sherlock Holmes and the two investigate a mysterious murder in London. The story that introduced one of fiction''s most iconic detectives.'),
('Brave New World',                           'Aldous Huxley',       'Dystopian',      1932, '9780060850524', 'In a future society engineered for happiness, Bernard Marx begins to question the price of stability. A prescient and unsettling vision of pleasure used as a tool of control.'),
('The Catcher in the Rye',                    'J.D. Salinger',       'Fiction',        1951, '9780316769174', 'Teenager Holden Caulfield wanders New York City after being expelled from prep school, railing against the phoniness of the adult world. A defining novel of adolescent alienation.');

-- ============================================================
-- REVIEWS  (17 rows — at least 15 required)
-- Spread across users 2–15, books 1–15. One review per user+book.
-- BookIDs: Gatsby=1, TKAM=2, 1984=3, Hobbit=4, HP=5, HungerGames=6,
--   P&P=7, Alchemist=8, Dune=9, DaVinci=10, GoneGirl=11,
--   Hitchhiker=12, StudyInScarlet=13, BraveNewWorld=14, Catcher=15
-- ============================================================
INSERT INTO Reviews (UserID, BookID, Rating, ReviewText, DateCreated, DateModified) VALUES
(2,  1,  4, 'Fitzgerald''s prose is dazzling. Gatsby himself is tragic but the green light stays with you forever.',                  '2024-03-01 10:15:00', '2024-03-01 10:15:00'),
(3,  1,  5, 'A perfect novel. I reread it every summer and discover something new each time.',                                        '2024-04-10 14:22:00', '2024-04-10 14:22:00'),
(4,  2,  5, 'Atticus Finch is the moral compass I needed as a teenager. This book changed how I see justice.',                        '2024-05-03 09:45:00', '2024-05-03 09:45:00'),
(7,  2,  4, 'Beautifully written and morally complex. Harper Lee earned every award this book received.',                             '2024-05-20 16:30:00', '2024-05-20 16:30:00'),
(5,  3,  5, 'Terrifying and completely relevant today. Everyone should read this at least once.',                                     '2024-06-02 11:00:00', '2024-06-02 11:00:00'),
(13, 3,  5, 'Orwell predicted so much. The concept of doublethink is more useful now than ever.',                                     '2024-08-14 20:05:00', '2024-08-14 20:05:00'),
(11, 4,  5, 'The adventure that started it all for Tolkien fans. Cozy and epic at the same time.',                                    '2024-07-08 13:15:00', '2024-07-08 13:15:00'),
(8,  5,  4, 'The magic system, the characters, the world — Rowling built something extraordinary with this first book.',              '2024-06-28 19:40:00', '2024-06-28 19:40:00'),
(9,  6,  5, 'Could not put it down. The Reaping scene genuinely made me emotional on my third reread.',                              '2024-07-15 08:55:00', '2024-07-15 08:55:00'),
(12, 7,  5, 'Austen''s wit is unmatched. Darcy''s second letter is the most satisfying moment in all of English literature.',        '2024-08-03 15:10:00', '2024-08-03 15:10:00'),
(6,  8,  4, 'The Alchemist is short but leaves a lasting impression. I highlighted half the book.',                                   '2024-09-11 10:25:00', '2024-09-11 10:25:00'),
(8,  9,  4, 'Dense and demanding but absolutely worth it. Dune rewards patient readers generously.',                                  '2024-09-22 21:30:00', '2024-09-22 21:30:00'),
(10, 10, 4, 'Fast-paced and clever. Not high literature but exactly what a mystery thriller should be.',                              '2024-10-05 12:00:00', '2024-10-05 12:00:00'),
(14, 11, 5, 'One of the best unreliable narrator setups in modern fiction. The twist earned it.',                                     '2024-10-18 23:15:00', '2024-10-18 23:15:00'),
(2,  12, 5, 'Funniest book I have ever read. I laughed out loud on public transport three times.',                                    '2024-11-07 17:45:00', '2024-11-07 17:45:00'),
(15, 14, 4, 'Huxley''s dystopia is more seductive than Orwell''s, which makes it even scarier.',                                     '2024-12-01 14:00:00', '2024-12-01 14:00:00'),
(3,  15, 3, 'Holden is exhausting on purpose, which is brilliant, but I needed a break halfway through.',                             '2025-01-20 09:30:00', '2025-01-20 09:30:00');

-- ============================================================
-- READING LIST  (17 rows — at least 15 required)
-- All three statuses used. DateFinished is NULL for non-finished.
-- ============================================================
INSERT INTO ReadingList (UserID, BookID, Status, DateAdded, DateFinished) VALUES
(2,  1,  'finished',          '2024-03-01 08:00:00', '2024-03-07 22:30:00'),
(2,  3,  'want_to_read',      '2024-06-15 10:00:00', NULL),
(2,  12, 'finished',          '2024-11-01 19:00:00', '2024-11-05 21:45:00'),
(3,  1,  'finished',          '2024-04-05 09:00:00', '2024-04-12 20:00:00'),
(3,  15, 'currently_reading', '2025-01-15 18:00:00', NULL),
(4,  2,  'finished',          '2024-05-01 07:30:00', '2024-05-10 23:00:00'),
(4,  7,  'want_to_read',      '2024-07-20 11:00:00', NULL),
(5,  3,  'finished',          '2024-05-28 20:00:00', '2024-06-03 21:30:00'),
(5,  6,  'currently_reading', '2024-10-10 14:00:00', NULL),
(6,  8,  'finished',          '2024-09-05 16:00:00', '2024-09-09 22:00:00'),
(7,  2,  'finished',          '2024-05-15 09:00:00', '2024-05-25 20:30:00'),
(8,  9,  'finished',          '2024-09-15 17:00:00', '2024-10-02 23:00:00'),
(9,  6,  'finished',          '2024-07-10 10:00:00', '2024-07-14 21:00:00'),
(10, 13, 'want_to_read',      '2024-10-01 12:00:00', NULL),
(11, 4,  'finished',          '2024-07-01 08:00:00', '2024-07-08 22:00:00'),
(12, 7,  'finished',          '2024-07-28 15:00:00', '2024-08-05 20:00:00'),
(13, 14, 'currently_reading', '2025-02-10 19:00:00', NULL);

-- ============================================================
-- BOOKS  (IDs 16–25, 10 additional rows)
-- ============================================================
INSERT INTO Books (Title, Author, Genre, PublishedYear, ISBN, Description) VALUES
('The Kite Runner',                          'Khaled Hosseini',      'Fiction',        2003, '9781594631931', 'Amir, a wealthy boy from Kabul, and his servant Hassan share a cherished friendship that is shattered by a single act of cowardice. A sweeping story of guilt, redemption, and the bonds of brotherhood set against the fall of Afghanistan.'),
('The Girl with the Dragon Tattoo',          'Stieg Larsson',        'Thriller',       2005, '9780307454546', 'Disgraced journalist Mikael Blomkvist teams with brilliant but troubled hacker Lisbeth Salander to investigate a forty-year-old disappearance within a powerful Swedish dynasty. A gripping thriller that launched a global phenomenon.'),
('Sapiens: A Brief History of Humankind',    'Yuval Noah Harari',    'Non-Fiction',    2011, '9780062316097', 'Harari traces the full sweep of human history from the emergence of Homo sapiens in Africa through the revolutions of agriculture, empire, and science. A provocative and accessible account of how our species came to dominate the planet.'),
('The Road',                                 'Cormac McCarthy',      'Fiction',        2006, '9780307387899', 'A father and his young son journey through a desolate post-apocalyptic America, carrying the fire of hope against the darkness. McCarthy''s Pulitzer-winning novel is a stark and deeply moving meditation on love and survival.'),
('Norwegian Wood',                           'Haruki Murakami',      'Fiction',        1987, '9780375704024', 'Tokyo student Toru Watanabe grapples with grief, desire, and the loss of innocence after the suicide of his best friend. Murakami''s most intimate novel, set against the backdrop of late-1960s Japan.'),
('The Name of the Wind',                     'Patrick Rothfuss',     'Fantasy',        2007, '9780756404741', 'Kvothe recounts his legendary rise from homeless orphan to the most feared wizard of his age in a narrative of magic, music, and myth. The first entry in the acclaimed Kingkiller Chronicle.'),
('Ender''s Game',                            'Orson Scott Card',     'Science Fiction', 1985, '9780812550702', 'Child prodigy Ender Wiggin is recruited to a rigorous battle school in space to train for an alien invasion. A landmark of science fiction that explores genius, manipulation, and the moral cost of war.'),
('The Shadow of the Wind',                   'Carlos Ruiz Zafon',    'Mystery',        2001, '9780143034902', 'In post-war Barcelona, young Daniel discovers a novel by the forgotten author Julian Carax and becomes entangled in a dangerous mystery involving a man who destroys every copy of Carax''s work. A gothic love letter to books and the city of Barcelona.'),
('Educated',                                 'Tara Westover',        'Memoir',         2018, '9780399590504', 'Tara Westover grew up in the mountains of Idaho, kept from school by her survivalist family, and eventually made her way to Cambridge and Harvard. A stunning memoir about the power of education to transform a life.'),
('The Midnight Library',                     'Matt Haig',            'Fiction',        2020, '9780525559474', 'Nora Seed finds herself in a library between life and death, where each book contains a different version of the life she could have lived. A moving exploration of regret, possibility, and what makes life worth living.');

-- ============================================================
-- REVIEWS  (12 new rows referencing BookIDs 16–25)
-- ============================================================
INSERT INTO Reviews (UserID, BookID, Rating, ReviewText, DateCreated, DateModified) VALUES
(4,  16, 5, 'One of the most emotionally devastating books I have ever read. Hosseini writes with such tenderness and honesty.',            '2025-01-05 11:20:00', '2025-01-05 11:20:00'),
(7,  16, 4, 'The depiction of pre-war Kabul is vivid and heartbreaking. A story about guilt that lingers long after the last page.',       '2025-02-14 09:00:00', '2025-02-14 09:00:00'),
(9,  17, 5, 'Lisbeth Salander is one of the greatest fictional characters ever written. This thriller is relentless and unforgettable.',    '2025-01-18 21:10:00', '2025-01-18 21:10:00'),
(6,  18, 5, 'Harari makes you rethink everything you thought you knew about history. Absolutely essential reading.',                        '2025-02-03 14:45:00', '2025-02-03 14:45:00'),
(14, 19, 5, 'Brutal, beautiful, and unlike anything else. McCarthy''s sparse prose cuts straight to the bone.',                            '2025-02-20 23:55:00', '2025-02-20 23:55:00'),
(15, 20, 4, 'Murakami''s most accessible novel. Haunting and melancholy in a way that feels deeply personal.',                             '2025-03-01 10:30:00', '2025-03-01 10:30:00'),
(11, 21, 5, 'Rothfuss writes Kvothe''s voice with such confidence and flair. I read the last hundred pages in one breathless sitting.',    '2025-03-09 19:20:00', '2025-03-09 19:20:00'),
(13, 21, 4, 'The world-building is extraordinary and the magic system is one of the most original I have encountered in fantasy.',          '2025-03-22 16:40:00', '2025-03-22 16:40:00'),
(8,  22, 5, 'Ender''s Game is required reading for any sci-fi fan. The twist ending is one of the best in the genre.',                     '2025-02-28 17:00:00', '2025-02-28 17:00:00'),
(10, 23, 5, 'Zafon weaves atmosphere, mystery, and literary passion into a novel that feels like a dream of Barcelona.',                    '2025-03-15 13:05:00', '2025-03-15 13:05:00'),
(5,  24, 5, 'Westover''s story is almost impossible to believe, yet completely authentic. One of the best memoirs of the decade.',         '2025-04-01 08:50:00', '2025-04-01 08:50:00'),
(2,  25, 4, 'The Midnight Library is warm, wise, and exactly what I needed. Haig asks the right questions about regret and second chances.','2025-04-12 20:15:00', '2025-04-12 20:15:00');

-- ============================================================
-- READING LIST  (12 new rows referencing BookIDs 16–25)
-- ============================================================
INSERT INTO ReadingList (UserID, BookID, Status, DateAdded, DateFinished) VALUES
(4,  16, 'finished',          '2025-01-01 08:00:00', '2025-01-05 22:00:00'),
(7,  16, 'finished',          '2025-02-10 09:00:00', '2025-02-14 21:00:00'),
(9,  17, 'finished',          '2025-01-12 19:00:00', '2025-01-18 23:30:00'),
(6,  18, 'finished',          '2025-01-25 10:00:00', '2025-02-03 20:00:00'),
(14, 19, 'currently_reading', '2025-02-18 21:00:00', NULL),
(15, 20, 'finished',          '2025-02-22 11:00:00', '2025-03-01 22:30:00'),
(11, 21, 'finished',          '2025-03-03 14:00:00', '2025-03-09 23:00:00'),
(13, 21, 'want_to_read',      '2025-03-20 16:00:00', NULL),
(8,  22, 'finished',          '2025-02-24 17:00:00', '2025-02-28 21:00:00'),
(13, 22, 'want_to_read',      '2025-03-01 12:00:00', NULL),
(5,  24, 'finished',          '2025-03-25 09:00:00', '2025-04-01 20:00:00'),
(2,  25, 'want_to_read',      '2025-04-10 18:00:00', NULL),
(12, 25, 'currently_reading', '2025-04-20 15:00:00', NULL),
(10, 23, 'want_to_read',      '2025-03-12 13:00:00', NULL);
