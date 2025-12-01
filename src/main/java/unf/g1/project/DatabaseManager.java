//This file is for interfacing with the DBMS using CRUD operations and is responsible for building SQL queries
package unf.g1.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import unf.g1.project.models.Model;

public class DatabaseManager {

    /**
     * Insert a model object into the database
     * @param connection Database connection
     * @param model Model object to insert
     * @return Number of rows affected
     * @throws SQLException if database error occurs
     */
    public static int insert(Connection connection, Model model) throws SQLException {
        String sql = QueryBuilder.buildInsert(model);
        PreparedStatement pstmt = connection.prepareStatement(sql);

        // Set all parameters from the model's map
        Map<String, Object> data = model.toMap();
        int index = 1;
        for (Object value : data.values()) {
            setParameter(pstmt, index, value);
            index++;
        }

        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        return rowsAffected;
    }

    /**
     * Helper to set PreparedStatement parameters based on type
     * @param pstmt PreparedStatement to set parameter on
     * @param index Parameter index (1-based)
     * @param value Value to set
     * @throws SQLException if database error occurs
     */
    private static void setParameter(PreparedStatement pstmt, int index, Object value)
            throws SQLException {
        if (value == null) {
            pstmt.setNull(index, java.sql.Types.VARCHAR);
        } else if (value instanceof String) {
            pstmt.setString(index, (String) value);
        } else if (value instanceof Character) {
            pstmt.setString(index, value.toString());
        } else if (value instanceof Integer) {
            pstmt.setInt(index, (Integer) value);
        } else if (value instanceof Double) {
            pstmt.setDouble(index, (Double) value);
        } else if (value instanceof java.sql.Date) {
            pstmt.setDate(index, (java.sql.Date) value);
        } else if (value instanceof java.sql.Timestamp) {
            pstmt.setTimestamp(index, (java.sql.Timestamp) value);
        } else {
            pstmt.setString(index, value.toString());
        }
    }

    /**
     * Search for patients by ID or name
     * @param connection Database connection
     * @param patientId Patient ID to search (can be null/empty)
     * @param name Name to search (first or last, can be null/empty)
     * @return ResultSet containing matching patients
     * @throws SQLException if database error occurs
     */
    public static ResultSet searchPatient(Connection connection, String patientId, String name)
            throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM PATIENT WHERE 1=1");

        if (patientId != null && !patientId.isEmpty()) {
            sql.append(" AND patientID = ?");
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND (LOWER(fName) LIKE LOWER(?) OR LOWER(lName) LIKE LOWER(?))");
        }

        System.out.println("DEBUG DatabaseManager: SQL Query: " + sql.toString());
        System.out.println("DEBUG DatabaseManager: Patient ID param: '" + patientId + "'");
        System.out.println("DEBUG DatabaseManager: Name param: '" + name + "'");

        PreparedStatement pstmt = connection.prepareStatement(sql.toString());
        int index = 1;

        if (patientId != null && !patientId.isEmpty()) {
            pstmt.setString(index++, patientId);
            System.out.println("DEBUG DatabaseManager: Set patientID parameter at index " + (index-1));
        }
        if (name != null && !name.isEmpty()) {
            String pattern = "%" + name + "%";
            pstmt.setString(index++, pattern);
            pstmt.setString(index++, pattern);
            System.out.println("DEBUG DatabaseManager: Set name pattern: '" + pattern + "'");
        }

        System.out.println("DEBUG DatabaseManager: Executing query...");
        ResultSet rs = pstmt.executeQuery();
        System.out.println("DEBUG DatabaseManager: Query executed successfully");

        return rs;
    }

    /**
     * Search for doctors by ID or name
     * @param connection Database connection
     * @param doctorId Doctor ID to search (can be null/empty)
     * @param name Name to search (first or last, can be null/empty)
     * @return ResultSet containing matching doctors
     * @throws SQLException if database error occurs
     */
    public static ResultSet searchDoctor(Connection connection, String doctorId, String name)
            throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM DOCTOR WHERE 1=1");

        if (doctorId != null && !doctorId.isEmpty()) {
            sql.append(" AND doctor_id = ?");
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND (LOWER(first_name) LIKE LOWER(?) OR LOWER(last_name) LIKE LOWER(?))");
        }

        PreparedStatement pstmt = connection.prepareStatement(sql.toString());
        int index = 1;

        if (doctorId != null && !doctorId.isEmpty()) {
            pstmt.setString(index++, doctorId);
        }
        if (name != null && !name.isEmpty()) {
            String pattern = "%" + name + "%";
            pstmt.setString(index++, pattern);
            pstmt.setString(index++, pattern);
        }

        return pstmt.executeQuery();
    }

    /**
     * Search for departments by code or name
     * @param connection Database connection
     * @param deptCode Department code to search (can be null/empty)
     * @param deptName Department name to search (can be null/empty)
     * @return ResultSet containing matching departments
     * @throws SQLException if database error occurs
     */
    public static ResultSet searchDepartment(Connection connection, String deptCode, String deptName)
            throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM DEPARTMENT WHERE 1=1");

        if (deptCode != null && !deptCode.isEmpty()) {
            sql.append(" AND dept_code = ?");
        }
        if (deptName != null && !deptName.isEmpty()) {
            sql.append(" AND LOWER(dept_name) LIKE LOWER(?)");
        }

        PreparedStatement pstmt = connection.prepareStatement(sql.toString());
        int index = 1;

        if (deptCode != null && !deptCode.isEmpty()) {
            pstmt.setString(index++, deptCode);
        }
        if (deptName != null && !deptName.isEmpty()) {
            String pattern = "%" + deptName + "%";
            pstmt.setString(index++, pattern);
        }

        return pstmt.executeQuery();
    }
}
