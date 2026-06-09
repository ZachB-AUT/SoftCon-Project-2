package com.group1.myvitals.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author zachb
 */
public class DB_learn {

    private DB_learn() {}

    public DB_learn DB_learn_test() {
        return new DB_learn();
    }

    public static void main(String[] args) /*throws SQLException*/ {
        DB_learn dbl = new DB_learn();
        DB_DataInterface db = new DB_DataInterface();
        dbl.populate(db);
        dbl.print_user_datapoints(db, 1);
    }

    private void populate(DB_DataInterface db) {
        int[] userints = { 0, 0, 0 };

        userints[0] = db.insert_user(
            "Zach Barrett",
            22,
            "31/10/2003",
            1.80,
            "Male",
            "twz6852"
        );
        userints[1] = db.insert_user(
            "Marlo Thacker",
            21,
            "1/1/1",
            1.79,
            "Male",
            "whatever"
        );
        userints[2] = db.insert_user(
            "Briar Day",
            21,
            "20/03/2005",
            1.79,
            "REDACTED",
            "somethin"
        );

        for (int i = 0; i < userints.length; i++) {
            db.addDataPoint("sleep_distance", "10", userints[i]);
            db.addDataPoint("sleep_distance", "13", userints[i]);
            db.addDataPoint("sleep_distance", "8", userints[i]);
            db.addDataPoint("sleep_distance", "13", userints[i]);

            db.addDataPoint("heart_rate", "120", userints[i]);
            db.addDataPoint("heart_rate", "124", userints[i]);
            db.addDataPoint("heart_rate", "128", userints[i]);
            db.addDataPoint("heart_rate", "100", userints[i]);
        }
    }

    public void print_user_datapoints(DB_DataInterface db, int userid) {
        ArrayList<String[]> datapoints = db.getDataPointsByUser(userid);
        for (String[] dp : datapoints) {
            System.out.println(dp[0] + "," + dp[1] + "," + dp[2]);
        }
    }
}
