package session;

public class SessionManager {

    private static int    userID;
    private static String username;
    private static String role;

    public static void login(int userID, String username, String role) {
        SessionManager.userID   = userID;
        SessionManager.username = username;
        SessionManager.role     = role;
    }

    public static void logout() {
        userID   = 0;
        username = null;
        role     = null;
    }

    public static boolean isLoggedIn() {
        return username != null;
    }

    public static boolean isAdmin() {
        return "admin".equals(role);
    }

    public static int    getUserID()   { return userID; }
    public static String getUsername() { return username; }
    public static String getRole()     { return role; }
}
