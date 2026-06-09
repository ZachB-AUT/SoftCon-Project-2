package com.group1.myvitals.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

/*
 * The database stores users, dataTypes, and dataPoints.
 * It consists of five tables: users, data_types, data_points, medications, allergies.
 *
 * users:
 *   id      Unique user id (auto-generated).
 *   name    User's full name.
 *   age     User's age.
 *   dob     Date of birth as DD/MM/YYYY string.
 *   height  Height in metres.
 *   gender  Gender string.
 *   nhi     NHI number (unique).
 *
 * data_types:
 *   id      Unique id (auto-generated).
 *   name    Data type name, e.g. "heart_rate". Must be unique.
 *   unit    Unit string, e.g. "bpm".
 *
 * data_points:
 *   id           Unique id (auto-generated).
 *   data_type_id References data_types(id). Cascade deletes.
 *   user_id      Optional reference to users(id).
 *   value        Recorded value stored as a string.
 *   recorded_at  ISO 8601 timestamp, e.g. "2024-03-15T10:30:00".
 *
 * medications:
 *   id      Unique id (auto-generated).
 *   user_id References users(id). Cascade deletes.
 *   name    Medication name.
 *   dosage  Dosage as an integer.
 *
 * allergies:
 *   id      Unique id (auto-generated).
 *   user_id References users(id). Cascade deletes.
 *   allergy Allergy name.
 */

/**
 * @author zachb
 */
public class DB_DataInterface {

    private static final String DB_URL =
        "jdbc:derby:memory:MyVitalsDB;create=true";
    private Connection conn;

    private String[][] initialDataTypes = {
        { "heart_rate", "bpm" },
        { "blood_pressure", "mmHg" },
        { "sleep_duration", "hours" },
        { "respiratory_rate", "breaths/min" },
        // { "body_temperature",  "°C"             },
        // { "weight",            "kg"             },
        // { "blood_glucose",     "mmol/L"         },
        // { "oxygen_saturation", "%"              },
        // { "steps",             "steps/day"      },
        // { "bmi",               "kg/m²"          },
    };

    public DB_DataInterface() {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        initSchema();
    }

    private void initSchema() {
        try (Statement smt = conn.createStatement()) {
            // SYS.SYSTABLES is Derby's system catalog — check before creating
            // ResultSet rs = smt.executeQuery(
            //     "SELECT COUNT(*) FROM SYS.SYSTABLES WHERE TABLENAME = 'DATA_TYPES' AND SCHEMANAME = 'APP'"
            // );
            // rs.next();
            // if (rs.getInt(1) > 0) return;

            smt.execute(
                """
                CREATE TABLE users (
                    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name          varchar(100) NOT NULL,
                    age           INTEGER,
                    dob           varchar(10),
                    height        DOUBLE,
                    gender        varchar(30),
                    nhi           varchar(20) UNIQUE,
                    username      varchar(50) UNIQUE,
                    password_hash varchar(64)
                )
                """
            );
            smt.execute(
                """
                CREATE TABLE data_types (
                    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name varchar(30) NOT NULL UNIQUE,
                    unit varchar(30)
                )
                """
            );
            smt.execute(
                """
                CREATE TABLE data_points (
                    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    data_type_id INTEGER NOT NULL REFERENCES data_types(id) ON DELETE CASCADE,
                    user_id      INTEGER,
                    value        varchar(30) NOT NULL,
                    recorded_at  varchar(30) NOT NULL
                )
                """
            );
            smt.execute(
                """
                CREATE TABLE medications (
                    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                    name    varchar(100) NOT NULL,
                    dosage  INTEGER NOT NULL
                )
                """
            );
            smt.execute(
                """
                CREATE TABLE allergies (
                    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                    allergy varchar(100) NOT NULL
                )
                """
            );

            for (String[] dt : initialDataTypes) {
                insert_data_type(dt[0], dt[1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---- Data Types ----

    public void insert_data_type(String name, String unit) {
        String sql = "INSERT INTO data_types (name, unit) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, unit);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all data types.
     * Each row is [id, name, unit].
     */
    public ArrayList<String[]> getDataTypes() {
        ArrayList<String[]> result = new ArrayList<>();
        try (Statement smt = conn.createStatement()) {
            ResultSet rs = smt.executeQuery(
                "SELECT id, name, unit FROM data_types"
            );
            while (rs.next()) {
                result.add(new String[] {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    rs.getString("unit"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns the id of the named data type.
     * @throws NoSuchElementException if not found
     */
    public int getDataTypeId(String dataTypeName)
        throws NoSuchElementException {
        String sql = "SELECT id FROM data_types WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dataTypeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NoSuchElementException(
            "Data type not found: " + dataTypeName
        );
    }

    // ---- Data Points ----

    public void insert_data_point(int data_type_id, int user_id, String value) {
        String current_time = new java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss"
        ).format(new java.util.Date());
        insert_data_point(data_type_id, user_id, value, current_time);
    }

    public void insert_data_point(
        int data_type_id,
        int user_id,
        String value,
        String recorded_at
    ) {
        String sql =
            "INSERT INTO data_points (data_type_id, user_id, value, recorded_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, data_type_id);
            pstmt.setInt(2, user_id);
            pstmt.setString(3, value);
            pstmt.setString(4, recorded_at);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a data point by data type name using the current timestamp.
     * @throws NoSuchElementException if dataTypeName is not found
     */
    public void addDataPoint(String dataTypeName, String value)
        throws NoSuchElementException {
        String current_time = new java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss"
        ).format(new java.util.Date());
        addDataPoint(dataTypeName, current_time, value);
    }

    /**
     * Inserts a data point linked to a user by data type name using the current timestamp.
     * @throws NoSuchElementException if dataTypeName is not found
     */
    public void addDataPoint(String dataTypeName, String value, int userId)
        throws NoSuchElementException {
        int typeId = getDataTypeId(dataTypeName);
        String current_time = new java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss"
        ).format(new java.util.Date());
        String sql =
            "INSERT INTO data_points (data_type_id, user_id, value, recorded_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, value);
            pstmt.setString(4, current_time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a data point by data type name with a specific timestamp.
     * @param recorded_at ISO 8601 date string, e.g. "2024-03-15T10:30:00"
     * @throws NoSuchElementException if dataTypeName is not found
     */
    public void addDataPoint(
        String dataTypeName,
        String recorded_at,
        String value
    ) throws NoSuchElementException {
        int typeId = getDataTypeId(dataTypeName);
        String sql =
            "INSERT INTO data_points (data_type_id, value, recorded_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            pstmt.setString(2, value);
            pstmt.setString(3, recorded_at);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all data points for the named data type (all users).
     * Each row is [id, value, recorded_at].
     * @throws NoSuchElementException if dataTypeName is not found
     */
    public ArrayList<String[]> getDataPoints(String dataTypeName)
        throws NoSuchElementException {
        return getDataPoints(getDataTypeId(dataTypeName));
    }

    /**
     * Returns all data points for the given data type id (all users).
     * Each row is [id, value, recorded_at].
     */
    public ArrayList<String[]> getDataPoints(int dataTypeId) {
        ArrayList<String[]> result = new ArrayList<>();
        String sql =
            "SELECT id, value, recorded_at FROM data_points WHERE data_type_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dataTypeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new String[] {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("value"),
                    rs.getString("recorded_at"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns all data points for the given data type id and user.
     * Each row is [id, value, recorded_at].
     */
    public ArrayList<String[]> getDataPoints(int dataTypeId, int userid) {
        ArrayList<String[]> result = new ArrayList<>();
        String sql =
            "SELECT id, value, recorded_at FROM data_points WHERE data_type_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dataTypeId);
            pstmt.setInt(2, userid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new String[] {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("value"),
                    rs.getString("recorded_at"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns all data points for the given user id across all data types.
     * Each row is [id, value, recorded_at].
     */
    public ArrayList<String[]> getDataPointsByUser(int userId) {
        ArrayList<String[]> result = new ArrayList<>();
        String sql =
            "SELECT id, value, recorded_at FROM data_points WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new String[] {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("value"),
                    rs.getString("recorded_at"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns the number of data points recorded for the named data type.
     * @throws NoSuchElementException if dataTypeName is not found
     */
    public int getDataPointCount(String dataTypeName)
        throws NoSuchElementException {
        int typeId = getDataTypeId(dataTypeName);
        String sql = "SELECT COUNT(*) FROM data_points WHERE data_type_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Updates the value of the data point matching the given data type name and timestamp.
     * @throws NoSuchElementException if no matching data point is found
     */
    public void editDataPointByDate(
        String dataTypeName,
        String dateTime,
        String newValue
    ) throws NoSuchElementException {
        int typeId = getDataTypeId(dataTypeName);
        String sql =
            "UPDATE data_points SET value = ? WHERE data_type_id = ? AND recorded_at = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, typeId);
            pstmt.setString(3, dateTime);
            if (pstmt.executeUpdate() == 0) {
                throw new NoSuchElementException(
                    "No datapoint found with dateTime: " + dateTime
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the data point matching the given data type name and timestamp.
     * @throws NoSuchElementException if no matching data point is found
     */
    public void deleteDataPointByDate(String dataTypeName, String dateTime)
        throws NoSuchElementException {
        int typeId = getDataTypeId(dataTypeName);
        String sql =
            "DELETE FROM data_points WHERE data_type_id = ? AND recorded_at = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            pstmt.setString(2, dateTime);
            if (pstmt.executeUpdate() == 0) {
                throw new NoSuchElementException(
                    "No datapoint found with dateTime: " + dateTime
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---- Users (from Person) ----

    /**
     * Inserts a new user and returns their generated id, or -1 on failure.
     * @param dob Date of birth as DD/MM/YYYY
     * @param height Height in metres
     */
    public int insert_user(
        String name,
        int age,
        String dob,
        double height,
        String gender,
        String nhi
    ) {
        String sql =
            "INSERT INTO users (name, age, dob, height, gender, nhi) VALUES (?, ?, ?, ?, ?, ?)";
        try (
            PreparedStatement pstmt = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            pstmt.setString(1, capitalise(name));
            pstmt.setInt(2, age);
            pstmt.setString(3, dob.isEmpty() ? null : dob);
            pstmt.setDouble(4, height);
            pstmt.setString(5, gender.isEmpty() ? null : capitalise(gender));
            pstmt.setString(6, nhi.isEmpty() ? null : capitalise(nhi));
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns the user with the given id as [id, name, age, dob, height, gender, nhi],
     * or null if not found.
     */
    public String[] get_user(int userId) {
        String sql =
            "SELECT id, name, age, dob, height, gender, nhi FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[] {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    String.valueOf(rs.getInt("age")),
                    rs.getString("dob"),
                    String.valueOf(rs.getDouble("height")),
                    rs.getString("gender"),
                    rs.getString("nhi"),
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the profile of an existing user.
     */
    public void update_user(
        int userId,
        String name,
        int age,
        String dob,
        double height,
        String gender,
        String nhi
    ) {
        String sql =
            "UPDATE users SET name=?, age=?, dob=?, height=?, gender=?, nhi=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, capitalise(name));
            pstmt.setInt(2, age);
            pstmt.setString(3, dob);
            pstmt.setDouble(4, height);
            pstmt.setString(5, capitalise(gender));
            pstmt.setString(6, capitalise(nhi));
            pstmt.setInt(7, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---- Medications ----

    public void addMedication(int userId, String name, int dosage) {
        String sql =
            "INSERT INTO medications (user_id, name, dosage) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, capitalise(name));
            pstmt.setInt(3, dosage);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMedication(int userId, String name) {
        String sql = "DELETE FROM medications WHERE user_id = ? AND name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, capitalise(name));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the user's medications as a map of medication name → dosage.
     */
    public HashMap<String, Integer> getMedications(int userId) {
        HashMap<String, Integer> result = new HashMap<>();
        String sql = "SELECT name, dosage FROM medications WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("dosage"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---- Allergies ----

    public void addAllergy(int userId, String allergy) {
        String sql = "INSERT INTO allergies (user_id, allergy) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, capitalise(allergy));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAllergy(int userId, String allergy) {
        String sql = "DELETE FROM allergies WHERE user_id = ? AND allergy = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, capitalise(allergy));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the user's allergies as a set of allergy name strings.
     */
    public HashSet<String> getAllergies(int userId) {
        HashSet<String> result = new HashSet<>();
        String sql = "SELECT allergy FROM allergies WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("allergy"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---- Auth ----

    /**
     * Registers a new user with a hashed password.
     * Returns the generated user id, or -1 if the username is already taken.
     */
    public int registerUser(
        String username,
        String password,
        String name,
        int age,
        String dob,
        double height,
        String gender,
        String nhi
    ) {
        String hash = hashPassword(password);
        String sql =
            "INSERT INTO users (username, password_hash, name, age, dob, height, gender, nhi) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (
            PreparedStatement pstmt = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            pstmt.setString(1, username.toLowerCase());
            pstmt.setString(2, hash);
            pstmt.setString(3, capitalise(name));
            pstmt.setInt(4, age);
            pstmt.setString(5, dob.isEmpty() ? null : dob);
            pstmt.setDouble(6, height);
            pstmt.setString(7, gender.isEmpty() ? null : capitalise(gender));
            // Store NULL for empty NHI to avoid UNIQUE constraint conflicts
            pstmt.setString(8, nhi.isEmpty() ? null : capitalise(nhi));
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            // UNIQUE constraint on username violated — return -1
        }
        return -1;
    }

    /**
     * Verifies login credentials.
     * Returns the user id on success, or -1 if credentials are invalid.
     */
    public int verifyLogin(String username, String password) {
        String sql = "SELECT id, password_hash FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username.toLowerCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("password_hash");
                if (hash != null && hash.equals(hashPassword(password))) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates the password hash for the given user.
     */
    public void updatePassword(int userId, String newPassword) {
        String hash = hashPassword(newPassword);
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hash);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---- Helpers ----

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(
                password.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private static String capitalise(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
