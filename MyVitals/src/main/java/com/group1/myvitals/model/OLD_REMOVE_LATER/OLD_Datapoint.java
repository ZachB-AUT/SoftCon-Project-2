// package com.group1.myvitals.model.OLD_REMOVE_LATER;
// /**
//  * Represents a single data point in the health data CSV file.
//  * @author ZachB
//  * @version 1.0
//  */
// public class Datapoint {
//     private String dataTypeName;
//     private String value;
//     private String dateTime;
//     /**
//      * Construct a single datapoint from a line in the CSV file.
//      * @param csvLine A line from the CSV file in the format "dataTypeName,timeRecorded,value"
//      */
//     public Datapoint(String csvLine) {
//         String[] parts = csvLine.split(",");
//         this.dataTypeName = parts[0];
//         this.dateTime = parts[1];
//         this.value = parts[2];
//     }
//     public Datapoint(String dataTypeName, String dateTime, String value) {
//         this.dataTypeName = dataTypeName;
//         this.dateTime = dateTime;
//         this.value = value;
//     }
//     public String toCSVLine() {
//         /**
//          * returns a string appropriate to represent this datapoint in the CSV
//          */
//         return dataTypeName + "," + dateTime + "," + value;
//     }
//     public String getDateTime() {
//         return this.dateTime;
//     }
//     public String getValue() {
//         return this.value;
//     }
//     public void setValue(String value) {
//         this.value = value;
//     }
//     public void setDateTime(String dateTime) {
//         this.dateTime = dateTime;
//     }
// }
