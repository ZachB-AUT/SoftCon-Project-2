package com.group1.myvitals.model.dao;

import java.util.HashSet;

/**
 * DAO interface for allergy operations.
 */
public interface AllergyDAO {

    /**
     * Adds an allergy entry for the given user.
     */
    void addAllergy(int userId, String allergy);

    /**
     * Removes the named allergy for the given user.
     */
    void removeAllergy(int userId, String allergy);

    /**
     * Returns the user's allergies as a set of allergy name strings.
     */
    HashSet<String> getAllergies(int userId);
}
