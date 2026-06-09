package com.group1.myvitals.model;

/**
 * Singleton that holds the currently logged-in user's id and username,
 * and provides the single shared DB connection for the application.
 *
 * Pattern: Singleton
 */
public class Session {

    private static Session instance;

    private int currentUserId = -1;
    private String currentUsername = null;
    private final DB_DataInterface db;

    private Session() {
        this.db = new DB_DataInterface();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(int userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
    }

    public void logout() {
        this.currentUserId = -1;
        this.currentUsername = null;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public String getUsername() {
        return currentUsername;
    }

    public boolean isLoggedIn() {
        return currentUserId != -1;
    }

    public DB_DataInterface getDb() {
        return db;
    }
}
