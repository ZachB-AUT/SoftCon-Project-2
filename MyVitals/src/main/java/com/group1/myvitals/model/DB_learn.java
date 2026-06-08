package com.group1.myvitals.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zachb
 */
public class DB_learn {

    private DB_learn() {}

    public DB_learn DB_learn_test() {
        return new DB_learn();
    }

    public static void main(String[] args) /*throws SQLException*/ {
        DB_DataInterface db = new DB_DataInterface();

        db.get

    }

    private void populate(DB_DataInterface db) {}
}
