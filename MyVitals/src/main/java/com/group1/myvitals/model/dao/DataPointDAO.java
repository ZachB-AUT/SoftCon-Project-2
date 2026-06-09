package com.group1.myvitals.model.dao;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * DAO interface for vital-sign data-point operations.
 */
public interface DataPointDAO {

    /**
     * Inserts a data point for the given data type id using the current timestamp.
     */
    void insert_data_point(int dataTypeId, int userId, String value);

    /**
     * Inserts a data point for the given data type id at a specific timestamp.
     * @param recordedAt ISO 8601 timestamp, e.g. "2024-03-15T10:30:00"
     */
    void insert_data_point(int dataTypeId, int userId, String value, String recordedAt);

    /**
     * Inserts a data point by data type name using the current timestamp.
     * @throws NoSuchElementException if dataTypeName is not found
     */
    void addDataPoint(String dataTypeName, String value) throws NoSuchElementException;

    /**
     * Inserts a data point linked to a user by data type name using the current timestamp.
     * @throws NoSuchElementException if dataTypeName is not found
     */
    void addDataPoint(String dataTypeName, String value, int userId) throws NoSuchElementException;

    /**
     * Inserts a data point by data type name at a specific timestamp.
     * @param recordedAt ISO 8601 timestamp, e.g. "2024-03-15T10:30:00"
     * @throws NoSuchElementException if dataTypeName is not found
     */
    void addDataPoint(String dataTypeName, String recordedAt, String value) throws NoSuchElementException;

    /**
     * Returns all data points for the named data type across all users.
     * Each element is [id, value, recorded_at].
     * @throws NoSuchElementException if dataTypeName is not found
     */
    ArrayList<String[]> getDataPoints(String dataTypeName) throws NoSuchElementException;

    /**
     * Returns all data points for the given data type id across all users.
     * Each element is [id, value, recorded_at].
     */
    ArrayList<String[]> getDataPoints(int dataTypeId);

    /**
     * Returns all data points for the given data type id and user.
     * Each element is [id, value, recorded_at].
     */
    ArrayList<String[]> getDataPoints(int dataTypeId, int userId);

    /**
     * Returns all data points for the given user across all data types.
     * Each element is [id, value, recorded_at].
     */
    ArrayList<String[]> getDataPointsByUser(int userId);

    /**
     * Returns the number of data points recorded for the named data type.
     * @throws NoSuchElementException if dataTypeName is not found
     */
    int getDataPointCount(String dataTypeName) throws NoSuchElementException;

    /**
     * Updates the value of the data point matching the given data type name and timestamp.
     * @throws NoSuchElementException if no matching data point is found
     */
    void editDataPointByDate(String dataTypeName, String dateTime, String newValue) throws NoSuchElementException;

    /**
     * Deletes the data point matching the given data type name and timestamp.
     * @throws NoSuchElementException if no matching data point is found
     */
    void deleteDataPointByDate(String dataTypeName, String dateTime) throws NoSuchElementException;
}
