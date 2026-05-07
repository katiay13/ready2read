package com.ready2read;

import com.ready2read.dao.BookDAO;
import com.ready2read.dao.UserDAO;
import com.ready2read.dao.ReviewDAO;
import com.ready2read.dao.ReadingListDAO;
import com.ready2read.models.Book;
import com.ready2read.models.User;
import com.ready2read.models.Review;
import com.ready2read.models.ReadingList;

import java.util.List;

/**
 * Run after seeding the DB. Creates/deletes its own test rows — no permanent side effects.
 * Compile: javac -cp lib/mysql-connector-j-9.7.0.jar tests/DAOTest.java src/dao/*.java src/db/*.java src/models/*.java src/session/*.java -d out/
 * Run:     java  -cp out:lib/mysql-connector-j-9.7.0.jar DAOTest
 */
public class DAOTest {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testBookDAO();
        testUserDAO();
        testReviewDAO();
        testReadingListDAO();
        System.out.println("\n=== RESULTS: " + passed + " passed, " + failed + " failed ===");
    }

    // -------------------------------------------------------------------------
    // BookDAO
    // -------------------------------------------------------------------------
    static void testBookDAO() {
        System.out.println("\n=== BookDAO ===");
        BookDAO dao = new BookDAO();

        // getAllBooks page 1
        List<Book> page1 = dao.getAllBooks(1, 10);
        check("getAllBooks(1,10) returns 5 books", page1.size() == 5);
        if (!page1.isEmpty())
            System.out.println("  first title (alpha): " + page1.get(0).getTitle());

        // pagination: page 1 size 2
        List<Book> p1s2 = dao.getAllBooks(1, 2);
        List<Book> p2s2 = dao.getAllBooks(2, 2);
        check("getAllBooks pagination: page1 size=2", p1s2.size() == 2);
        check("getAllBooks pagination: page2 size=2", p2s2.size() == 2);
        check("getAllBooks pagination: pages differ", !p1s2.isEmpty() && !p2s2.isEmpty() && !p1s2.get(0).getTitle().equals(p2s2.get(0).getTitle()));

        // getBooksByGenre
        List<Book> fiction = dao.getBooksByGenre("Fiction", 1, 10);
        check("getBooksByGenre('Fiction') returns 2", fiction.size() == 2);
        List<Book> noGenre = dao.getBooksByGenre("NoSuchGenre", 1, 10);
        check("getBooksByGenre unknown returns empty list (not null)", noGenre != null && noGenre.isEmpty());

        // getBookByID
        Book b1 = dao.getBookByID(1);
        check("getBookByID(1) is '1984'", b1 != null && "1984".equals(b1.getTitle()));
        Book missing = dao.getBookByID(99999);
        check("getBookByID(99999) returns null", missing == null);

        // getAverageRating — seed: book 1 has ratings 5 and 4 → avg 4.5
        double avg1 = dao.getAverageRating(1);
        check("getAverageRating(1) == 4.5", avg1 == 4.5);
        double avgNone = dao.getAverageRating(2);
        // book 2 has one rating of 4 from seed
        check("getAverageRating(2) == 4.0", avgNone == 4.0);
        double avgMissing = dao.getAverageRating(99999);
        check("getAverageRating(no reviews) == 0.0", avgMissing == 0.0);

        // counts
        int total = dao.getTotalBookCount();
        check("getTotalBookCount() == 5", total == 5);
        int fictionCount = dao.getTotalBookCountByGenre("Fiction");
        check("getTotalBookCountByGenre('Fiction') == 2", fictionCount == 2);
        int noneCount = dao.getTotalBookCountByGenre("NoSuchGenre");
        check("getTotalBookCountByGenre unknown == 0", noneCount == 0);

        // getAllGenres
        List<String> genres = dao.getAllGenres();
        check("getAllGenres() returns 3 genres", genres.size() == 3);
        check("getAllGenres() sorted — first is 'Dystopian'", "Dystopian".equals(genres.get(0)));

        // addBook / getBookByID / updateBook / deleteBook round-trip
        Book testBook = new Book(0, "Test Title", "Test Author", "Test Genre", 2024, "0000000000000", "Test desc");
        dao.addBook(testBook);
        List<Book> afterAdd = dao.getAllBooks(1, 100);
        int newTotal = dao.getTotalBookCount();
        check("addBook increases count to 6", newTotal == 6);
        Book found = afterAdd.stream().filter(b -> "Test Title".equals(b.getTitle())).findFirst().orElse(null);
        check("addBook: new book retrievable via getAllBooks", found != null);

        if (found != null) {
            int newID = found.getBookID();
            Book directFound = dao.getBookByID(newID);
            check("getBookByID finds newly added book", directFound != null);

            // updateBook
            Book updated = new Book(newID, "Updated Title", "Updated Author", "Updated Genre", 2025, "1111111111111", "Updated desc");
            dao.updateBook(updated);
            Book afterUpdate = dao.getBookByID(newID);
            check("updateBook changes title", afterUpdate != null && "Updated Title".equals(afterUpdate.getTitle()));

            // deleteBook
            dao.deleteBook(newID);
            check("deleteBook removes book", dao.getBookByID(newID) == null);
            check("getTotalBookCount back to 5 after delete", dao.getTotalBookCount() == 5);
        }
    }

    // -------------------------------------------------------------------------
    // UserDAO
    // -------------------------------------------------------------------------
    static void testUserDAO() {
        System.out.println("\n=== UserDAO ===");
        UserDAO dao = new UserDAO();

        // login — valid
        User u = dao.login("purpleturtle", "hashed_password");
        check("login valid credentials returns User", u != null);
        check("login returns correct username", u != null && "purpleturtle".equals(u.getUsername()));
        check("login populates role", u != null && u.getRole() != null && !u.getRole().isEmpty());

        // login — invalid
        User bad = dao.login("purpleturtle", "wrongpassword");
        check("login bad password returns null", bad == null);
        User badUser = dao.login("nobody", "anything");
        check("login unknown user returns null", badUser == null);

        // usernameExists / emailExists
        check("usernameExists existing", dao.usernameExists("purpleturtle"));
        check("usernameExists non-existent", !dao.usernameExists("__notauser__"));
        check("emailExists existing", dao.emailExists("john@email.com"));
        check("emailExists non-existent", !dao.emailExists("__nobody@nowhere.invalid__"));

        // registerUser + deleteUser round-trip
        String tmpName = "tmpuser_" + System.currentTimeMillis();
        User newUser = new User(0, tmpName, tmpName + "@test.invalid", "testpass123", "user", null, null, null);
        dao.registerUser(newUser);
        check("registerUser: usernameExists after register", dao.usernameExists(tmpName));

        // login after register
        User loggedIn = dao.login(tmpName, "testpass123");
        check("login works after registerUser", loggedIn != null);
        check("registerUser sets role to 'user'", loggedIn != null && "user".equals(loggedIn.getRole()));

        if (loggedIn != null) {
            int tmpID = loggedIn.getUserID();

            // updatePassword
            dao.updatePassword(tmpID, "newpass456");
            User withNew = dao.login(tmpName, "newpass456");
            check("updatePassword: login with new password succeeds", withNew != null);
            User withOld = dao.login(tmpName, "testpass123");
            check("updatePassword: old password no longer works", withOld == null);

            // deleteUser
            dao.deleteUser(tmpID);
            check("deleteUser: usernameExists after delete is false", !dao.usernameExists(tmpName));
        }
    }

    // -------------------------------------------------------------------------
    // ReviewDAO
    // -------------------------------------------------------------------------
    static void testReviewDAO() {
        System.out.println("\n=== ReviewDAO ===");
        ReviewDAO dao = new ReviewDAO();

        // getReviewsByBook — book 1 (1984) has 2 seed reviews
        List<Review> book1Reviews = dao.getReviewsByBook(1);
        check("getReviewsByBook(1) returns 2 reviews", book1Reviews.size() == 2);
        check("getReviewsByBook populates username", book1Reviews.stream().allMatch(r -> r.getUsername() != null));
        check("getReviewsByBook returns non-null list for unknown book", dao.getReviewsByBook(99999) != null);
        check("getReviewsByBook returns empty list for unknown book", dao.getReviewsByBook(99999).isEmpty());

        // getReviewsByUser — user 2 (purpleturtle) has 3 seed reviews
        List<Review> user2Reviews = dao.getReviewsByUser(2);
        check("getReviewsByUser(2) returns 3 reviews", user2Reviews.size() == 3);
        check("getReviewsByUser populates bookTitle", user2Reviews.stream().allMatch(r -> r.getBookTitle() != null));

        // getReviewByUserAndBook
        Review found = dao.getReviewByUserAndBook(2, 1);
        check("getReviewByUserAndBook finds existing review", found != null);
        Review notFound = dao.getReviewByUserAndBook(1, 1);  // admin has no reviews
        check("getReviewByUserAndBook returns null when not found", notFound == null);

        // addReview / updateReview / deleteReview using admin (UserID=1) + book 2
        // (admin has no reviews in seed, so no UNIQUE constraint conflict)
        Review newReview = new Review(0, 1, 2, 3, "Admin test review", null, null);
        dao.addReview(newReview);
        Review added = dao.getReviewByUserAndBook(1, 2);
        check("addReview: review findable after add", added != null);
        check("addReview: correct rating stored", added != null && added.getRating() == 3);

        if (added != null) {
            // updateReview
            Review updated = new Review(added.getReviewID(), 1, 2, 5, "Updated admin review", null, null);
            dao.updateReview(updated);
            Review afterUpdate = dao.getReviewByUserAndBook(1, 2);
            check("updateReview: rating changed to 5", afterUpdate != null && afterUpdate.getRating() == 5);
            check("updateReview: DateModified is now set", afterUpdate != null && afterUpdate.getDateModified() != null);

            // deleteReview
            dao.deleteReview(afterUpdate.getReviewID());
            check("deleteReview: review gone after delete", dao.getReviewByUserAndBook(1, 2) == null);
        }
    }

    // -------------------------------------------------------------------------
    // ReadingListDAO
    // -------------------------------------------------------------------------
    static void testReadingListDAO() {
        System.out.println("\n=== ReadingListDAO ===");
        ReadingListDAO dao = new ReadingListDAO();

        // getReadingListByUser — user 2 (purpleturtle) has 4 seed entries
        List<ReadingList> user2List = dao.getReadingListByUser(2);
        check("getReadingListByUser(2) returns 4 entries", user2List.size() == 4);
        check("getReadingListByUser populates bookTitle", user2List.stream().allMatch(e -> e.getBookTitle() != null));
        check("getReadingListByUser populates bookAuthor", user2List.stream().allMatch(e -> e.getBookAuthor() != null));
        check("getReadingListByUser populates bookGenre", user2List.stream().allMatch(e -> e.getBookGenre() != null));
        check("getReadingListByUser empty list for unknown user", dao.getReadingListByUser(99999).isEmpty());

        // getEntry
        ReadingList entry = dao.getEntry(2, 1);
        check("getEntry finds existing entry", entry != null);
        ReadingList noEntry = dao.getEntry(1, 1);  // admin has no reading list entries
        check("getEntry returns null when not found", noEntry == null);

        // addEntry / updateStatus / removeEntry using admin (UserID=1) + book 1
        dao.addEntry(1, 1, "want_to_read");
        ReadingList added = dao.getEntry(1, 1);
        check("addEntry: entry exists after add", added != null);
        check("addEntry: status is want_to_read", added != null && added.getStatus() == ReadingList.Status.WANT_TO_READ);

        if (added != null) {
            int entryID = added.getEntryID();

            // updateStatus → currently_reading
            dao.updateStatus(entryID, "currently_reading");
            ReadingList current = dao.getEntry(1, 1);
            check("updateStatus to currently_reading", current != null && current.getStatus() == ReadingList.Status.CURRENTLY_READING);

            // updateStatus → finished
            dao.updateStatus(entryID, "finished");
            ReadingList finished = dao.getEntry(1, 1);
            check("updateStatus to finished", finished != null && finished.getStatus() == ReadingList.Status.FINISHED);

            // updateStatus back to want_to_read
            dao.updateStatus(entryID, "want_to_read");
            ReadingList cleared = dao.getEntry(1, 1);
            check("updateStatus back to want_to_read", cleared != null && cleared.getStatus() == ReadingList.Status.WANT_TO_READ);

            // removeEntry
            dao.removeEntry(entryID);
            check("removeEntry: entry gone after delete", dao.getEntry(1, 1) == null);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    static void check(String label, boolean condition) {
        if (condition) {
            System.out.println("  PASS  " + label);
            passed++;
        } else {
            System.out.println("  FAIL  " + label);
            failed++;
        }
    }
}
