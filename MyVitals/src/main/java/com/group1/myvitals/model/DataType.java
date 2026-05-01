package com.group1.myvitals.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class represents a health data type.
 * @author ZachB
 * @version 1.0
 */
public class DataType {

    public String name;
    private File dataFile;
    private ArrayList<Datapoint> dataPoints = new ArrayList<>();
    private static String csvHeader = "DataTypeName,dateTime,value";

    /**
     *
     * @param dataTypeName The name of the datatype.
     * @param dataFile The file containing the data for this datatype.
     */
    public DataType(String dataTypeName, File dataFile) {
        this.name = dataTypeName;
        this.dataFile = dataFile;

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ioException) {
                System.err.println(
                    "Failed to create data file: " + ioException.getMessage()
                );
            }
        }
        if (dataFile.length() == 0) {
            try {
                Files.writeString(dataFile.toPath(), csvHeader);
            } catch (IOException ioException) {
                System.err.println(
                    "Failed to write header to data file: " +
                        ioException.getMessage()
                );
            }
        }

        this.readCSV();
    }

    public void printDebugSummary() {
        System.out.println("Data type: " + this.name);
        System.out.println("Number of datapoints: " + this.dataPoints.size());
        System.out.println("Current datapoints:");

        for (int i = 0; i < dataPoints.size(); i++) {
            System.out.println(dataPoints.get(i).toCSVLine());
        }
    }

    public void readCSV() {
        /**
         * Reads the CSV file and converts each line into a Datapoint, then
         * loads them all into the ArrayList.
         */
        try {
            for (String line : Files.readAllLines(dataFile.toPath())) {
                if (!line.isEmpty() && !line.startsWith(csvHeader)) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        dataPoints.add(
                            new Datapoint(parts[0], parts[1], parts[2])
                        );
                    }
                }
            }
        } catch (IOException ioException) {
            System.err.println(
                "Failed to read data file: " + ioException.getMessage()
            );
        }
    }

    public int getNumberOfDatapoints() {
        /**
         * Returns the number of datapoints in the data file.
         */
        return dataPoints.size();
    }

    public ArrayList<Datapoint> getDataPoints() {
        return dataPoints;
    }

    public void addDataPoint(Datapoint dataPoint) {
        /**
         * @param datapoint Datapoint to add to system
         */
        this.dataPoints.add(dataPoint);
        this.writeCSV();
    }

    public void addDataPoint(String dateTime, String value) {
        this.addDataPoint(new Datapoint(this.name, dateTime, value));
    }

    public void writeCSV() {
        /**
         * Overwrites the CSV file content with the CURRENT datapoints arraylist content.
         * Call this every time a new datapoint is added to ensure the arraylist and file do not desync.
         */
        try {
            // Delete current file content
            Files.writeString(
                dataFile.toPath(),
                csvHeader + "\n",
                StandardOpenOption.TRUNCATE_EXISTING
            );

            // Write out all datapoints as CSV lines
            for (Datapoint datapoint : dataPoints) {
                Files.writeString(
                    dataFile.toPath(),
                    datapoint.toCSVLine() + "\n",
                    StandardOpenOption.APPEND
                );
            }
        } catch (IOException ioException) {
            System.err.println(
                "Failed to write data file: " + ioException.getMessage()
            );
        }
    }

    public void deleteDataPoint_by_dateTime(String dateTime)
        throws NoSuchElementException {
        for (int i = 0; i < this.dataPoints.size(); i++) {
            if (this.dataPoints.get(i).getDateTime().equals(dateTime)) {
                this.dataPoints.remove(i);
                this.writeCSV();
                return;
            }
        }
        throw new NoSuchElementException(
            "No datapoint found with dateTime: " + dateTime
        );
    }

    public void editDataPointByDate(String dateTime, String newValue)
        throws NoSuchElementException {
        for (int i = 0; i < this.dataPoints.size(); i++) {
            if (this.dataPoints.get(i).getDateTime().equals(dateTime)) {
                this.dataPoints.get(i).setValue(newValue);
                this.writeCSV();
                return;
            }
        }
        throw new NoSuchElementException(
            "No datapoint found with dateTime: " + dateTime
        );
    }
}
