package com.group1.myvitals.model.dao;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * DAO interface for data-type catalogue operations.
 */
public interface DataTypeDAO {

    /**
     * Inserts a new data type with the given name and unit.
     */
    void insert_data_type(String name, String unit);

    /**
     * Returns all data types. Each element is [id, name, unit].
     */
    ArrayList<String[]> getDataTypes();

    /**
     * Returns the id of the named data type.
     * @throws NoSuchElementException if not found
     */
    int getDataTypeId(String dataTypeName) throws NoSuchElementException;
}
