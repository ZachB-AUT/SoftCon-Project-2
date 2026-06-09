package com.group1.myvitals.model.dao;

import java.util.HashMap;

/**
 * DAO interface for medication operations.
 */
public interface MedicationDAO {

    /**
     * Adds a medication entry for the given user.
     */
    void addMedication(int userId, String name, int dosage);

    /**
     * Removes the named medication for the given user.
     */
    void removeMedication(int userId, String name);

    /**
     * Returns the user's medications as a map of medication name → dosage (mg).
     */
    HashMap<String, Integer> getMedications(int userId);
}
