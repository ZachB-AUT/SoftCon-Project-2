package com.group1.myvitals.model.dao;

/**
 * DAO interface for user profile and authentication operations.
 */
public interface UserDAO {

    /**
     * Inserts a new user and returns their generated id, or -1 on failure.
     */
    int insert_user(String name, int age, String dob, double height, String gender, String nhi);

    /**
     * Returns the user with the given id as [id, name, age, dob, height, gender, nhi],
     * or null if not found.
     */
    String[] get_user(int userId);

    /**
     * Updates the profile of an existing user.
     */
    void update_user(int userId, String name, int age, String dob, double height, String gender, String nhi);

    /**
     * Registers a new user with a hashed password.
     * Returns the generated user id, or -1 if the username is already taken.
     */
    int registerUser(String username, String password, String name, int age, String dob, double height, String gender, String nhi);

    /**
     * Verifies login credentials.
     * Returns the user id on success, or -1 if credentials are invalid.
     */
    int verifyLogin(String username, String password);

    /**
     * Updates the stored password hash for the given user.
     */
    void updatePassword(int userId, String newPassword);
}
