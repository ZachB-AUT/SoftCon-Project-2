package com.group1.myvitals.model.dao;

/**
 * Aggregate DAO interface for the MyVitals application.
 *
 * Extends all domain-specific DAO interfaces so that callers can work
 * against a single abstraction while the domain split remains explicit.
 * The concrete implementation is DerbyVitalsDAO (DB_DataInterface).
 *
 * Pattern: Data Access Object (DAO)
 */
public interface VitalsDAO extends UserDAO, DataTypeDAO, DataPointDAO, MedicationDAO, AllergyDAO {
}
