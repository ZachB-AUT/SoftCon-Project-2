package com.group1.myvitals.model;

public abstract class datapoint {

    private String date_recorded;
    private String value;

    public String getDate_recorded() {
        return date_recorded;
    }

    public void setDate_recorded(String date_recorded) {
        this.date_recorded = date_recorded;
    }

    public String db_string() {
        return String.format(
            "INSERT INTO datapoints (date_recorded, value) VALUES ('%s', '%s')",
            date_recorded,
            value
        );
    }
}
