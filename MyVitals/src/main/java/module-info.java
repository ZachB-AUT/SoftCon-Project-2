module com.group1.myvitals {
 
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
 
    // JDBC - needed for Derby database access
    requires java.sql;
 
    // Derby and BCrypt are not fully modularised so they are accessed
    // via the classpath as unnamed modules - no requires needed here
 
    // Opens packages so JavaFX can access controllers via reflection
    opens com.group1.myvitals.view        to javafx.fxml;
    //opens com.group1.myvitals.controller to javafx.fxml; TO DO Uncomment once controller files have been written
 
    // Export the app package so JavaFX launcher can find App.java
    exports com.group1.myvitals.view;
}