package com.group1.myvitals.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit 4 tests covering database operations and business logic.
 * All tests share the same in-memory Derby DB within the JVM process.
 * Unique usernames per test prevent UNIQUE constraint conflicts.
 */
public class DBTest {

    private static DB_DataInterface db;

    @BeforeClass
    public static void setup() {
        db = new DB_DataInterface();
    }

    // --- Test 1: User Registration and Retrieval ---
    @Test
    public void testRegisterAndGetUser() {
        int id = db.registerUser("test_alice", "secret123", "Alice Smith",
            30, "01/01/1994", 1.65, "Female", "ABC1234");
        assertNotEquals("registerUser should return a positive ID", -1, id);

        String[] user = db.get_user(id);
        assertNotNull("get_user should return a non-null array", user);
        assertEquals("Name should match", "Alice Smith", user[1]);
    }

    // --- Test 2: Login Verification — correct password ---
    @Test
    public void testVerifyLoginSuccess() {
        int id = db.registerUser("test_bob", "password99", "Bob Jones",
            25, "", 0.0, "", "");
        assertNotEquals(-1, id);

        int verified = db.verifyLogin("test_bob", "password99");
        assertEquals("verifyLogin should return the user id on success", id, verified);
    }

    // --- Test 3: Login Verification — wrong password ---
    @Test
    public void testVerifyLoginFailure() {
        db.registerUser("test_carol", "rightpass", "Carol White", 0, "", 0.0, "", "");
        int failed = db.verifyLogin("test_carol", "wrongpass");
        assertEquals("verifyLogin should return -1 for wrong password", -1, failed);
    }

    // --- Test 4: Duplicate Username Rejected ---
    @Test
    public void testDuplicateUsernameReturnsMinusOne() {
        db.registerUser("test_dave", "pass1", "Dave", 0, "", 0.0, "", "");
        int duplicate = db.registerUser("test_dave", "pass2", "Dave2", 0, "", 0.0, "", "");
        assertEquals("Duplicate username should return -1", -1, duplicate);
    }

    // --- Test 5: Data Point Insert and Retrieval ---
    @Test
    public void testAddAndGetDataPoint() {
        int userId = db.registerUser("test_eve", "pass", "Eve Green", 0, "", 0.0, "", "");
        db.addDataPoint("heart_rate", "72", userId);

        int typeId = db.getDataTypeId("heart_rate");
        ArrayList<String[]> points = db.getDataPoints(typeId, userId);

        assertFalse("Should have at least one data point", points.isEmpty());
        boolean found = points.stream().anyMatch(p -> "72".equals(p[1]));
        assertTrue("Value '72' should be present", found);
    }

    // --- Test 6: Medication CRUD ---
    @Test
    public void testMedicationCRUD() {
        int userId = db.registerUser("test_frank", "pass", "Frank Blue", 0, "", 0.0, "", "");

        db.addMedication(userId, "Aspirin", 100);
        HashMap<String, Integer> meds = db.getMedications(userId);
        assertTrue("Aspirin should be in medications", meds.containsKey("Aspirin"));
        assertEquals("Dosage should be 100", Integer.valueOf(100), meds.get("Aspirin"));

        db.removeMedication(userId, "Aspirin");
        meds = db.getMedications(userId);
        assertFalse("Aspirin should be removed", meds.containsKey("Aspirin"));
    }

    // --- Test 7: Allergy CRUD ---
    @Test
    public void testAllergyCRUD() {
        int userId = db.registerUser("test_grace", "pass", "Grace Red", 0, "", 0.0, "", "");

        db.addAllergy(userId, "Peanuts");
        HashSet<String> allergies = db.getAllergies(userId);
        assertTrue("Peanuts should be in allergies", allergies.contains("Peanuts"));

        db.removeAllergy(userId, "Peanuts");
        allergies = db.getAllergies(userId);
        assertFalse("Peanuts should be removed", allergies.contains("Peanuts"));
    }

    // --- Test 8: Session Business Logic (no DB) ---
    @Test
    public void testSessionLoginLogout() {
        Session session = Session.getInstance();
        session.logout();

        assertFalse("Should not be logged in after logout", session.isLoggedIn());
        assertEquals("userId should be -1 after logout", -1, session.getCurrentUserId());

        session.login(42, "testuser");
        assertTrue("Should be logged in after login", session.isLoggedIn());
        assertEquals("userId should match", 42, session.getCurrentUserId());
        assertEquals("username should match", "testuser", session.getUsername());

        session.logout();
        assertFalse("Should not be logged in after second logout", session.isLoggedIn());
        assertNull("username should be null after logout", session.getUsername());
    }
}
