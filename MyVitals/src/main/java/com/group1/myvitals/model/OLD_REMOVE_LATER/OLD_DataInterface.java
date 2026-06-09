// package com.group1.myvitals.model.OLD_REMOVE_LATER;
// import java.io.File;
// import java.util.ArrayList;
// import java.util.NoSuchElementException;
// /**
//  * This class handles storing, reading, writing, and editing data.
//  * @author ZachB
//  * @version 1.0
//  *
//  */
// public class DataInterface {
//     private String[] initialDataTypeNames;
//     private File dataDir;
//     protected ArrayList<DataType> dataTypes = new ArrayList<>();
//     DataInterface(String[] initialDataTypes) {
//         /**
//          * Constructs a DataInterface with the given initial data types.
//          *
//          * @param initialDataTypes the initial data types to add
//          */
//         this.dataDir = new File("data"); // Directory to store data
//         this.initialDataTypeNames = initialDataTypes;
//         // Init data directory
//         if (!this.dataDir.exists()) {
//             System.out.println(
//                 "Data directory does not exist, attempting to create it."
//             );
//             if (this.dataDir.mkdir()) {
//                 System.out.println("Data directory created successfully");
//             } else {
//                 System.out.println("Failed to create data directory");
//             }
//         }
//         // Create DataTypes from each of the DataTypeNames
//         for (String dataTypeName : this.initialDataTypeNames) {
//             try {
//                 this.addNewDatatype(dataTypeName);
//             } catch (IllegalArgumentException IAE) {
//                 System.err.println("Invalid data type name: " + dataTypeName);
//             }
//         }
//     }
//     public void addNewDatatype(String dataTypeName)
//         /**
//          * Adds a new datatype to the data interface.
//          *
//          * @param dataTypeName the name of the new datatype
//          * @throws IllegalArgumentException if the data type name contains a comma
//          */
//         throws IllegalArgumentException {
//         if (dataTypeName.contains(",")) {
//             throw new IllegalArgumentException(
//                 "Data type name cannot contain a comma: " + dataTypeName
//             );
//         }
//         File newDataFile = new File(this.dataDir, dataTypeName + ".csv");
//         DataType newDataType = new DataType(dataTypeName, newDataFile);
//         this.dataTypes.add(newDataType);
//     }
//     public ArrayList<DataType> getDataTypes() {
//         return this.dataTypes;
//     }
//     public void updateAllDataTypesFromCSVs() {
//         for (int i = 0; i < this.dataTypes.size(); i++) {
//             this.dataTypes.get(i).readCSV();
//         }
//     }
//     /**
//      * Takes a date and data type name
//      * @param dateTime
//      * @param dataTypeName
//      * @param newValue
//      * @throws NoSuchElementException
//      */
//     public void editDataPointByDate(
//         String dateTime,
//         String dataTypeName,
//         String newValue
//     ) throws NoSuchElementException {
//         for (DataType dt : this.dataTypes) {
//             if (dt.name == dataTypeName) {
//                 dt.editDataPointByDate(dateTime, newValue);
//                 return;
//             }
//         }
//         throw new NoSuchElementException(
//             "Data type not found: " + dataTypeName
//         );
//     }
//     public void deleteDataPointByDate(String dateTime, String dataTypeName)
//         throws NoSuchElementException {
//         for (DataType dt : this.dataTypes) {
//             if (dt.name == dataTypeName) {
//                 dt.deleteDataPoint_by_dateTime(dateTime);
//                 return;
//             }
//         }
//         throw new NoSuchElementException(
//             "Data type not found: " + dataTypeName
//         );
//     }
//     public void setInitialDataTypeNames(String[] initialDataTypeNames) {
//         this.initialDataTypeNames = initialDataTypeNames;
//     }
//     public File getDataDir() {
//         return dataDir;
//     }
//     public void setDataDir(File dataDir) {
//         this.dataDir = dataDir;
//     }
//     public void setDataTypes(ArrayList<DataType> dataTypes) {
//         this.dataTypes = dataTypes;
//     }
// }
