//This file is for interfacing with the DBMS using CRUD operations
package unf.g1.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import unf.g1.project.models.Model;

public class DatabaseManager {

    // Date formatters for consistent date display
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    /**
     * Format a date to MM/dd/yyyy
     */
    private static String formatDate(java.sql.Date date) {
        if (date == null) return "N/A";
        return dateFormat.format(date);
    }

    /**
     * Format a timestamp to MM/dd/yyyy hh:mm AM/PM
     */
    private static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) return "N/A";
        return dateTimeFormat.format(timestamp);
    }

    /**
     * Insert a model object into the database
     * @param connection Database connection
     * @param model Model object to insert
     * @return Number of rows affected
     * @throws SQLException if database error occurs
     */
    public static int insert(Connection connection, Model model) throws SQLException {
        String sql = QueryBuilder.buildInsert(model);
        System.out.println("DEBUG DatabaseManager.insert(): SQL: " + sql);

        int rowsAffected;
        // Set all parameters from the model's map
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set all parameters from the model's map
            Map<String, Object> data = model.toMap();
            System.out.println("DEBUG DatabaseManager.insert(): Setting " + data.size() + " parameters");
            int index = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                System.out.println("DEBUG DatabaseManager.insert(): Param " + index + " (" + entry.getKey() + "): " +
                        (value == null ? "NULL" : value.toString() + " [" + value.getClass().getSimpleName() + "]"));
                setParameter(pstmt, index, value);
                index++;
            }   System.out.println("DEBUG DatabaseManager.insert(): Executing update...");
            rowsAffected = pstmt.executeUpdate();
            System.out.println("DEBUG DatabaseManager.insert(): Rows affected: " + rowsAffected);
        }
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
        } else if (value instanceof String string) {
            pstmt.setString(index, string);
        } else if (value instanceof Character) {
            pstmt.setString(index, value.toString());
        } else if (value instanceof Integer integer) {
            pstmt.setInt(index, integer);
        } else if (value instanceof Double aDouble) {
            pstmt.setDouble(index, aDouble);
        } else if (value instanceof java.sql.Date date) {
            pstmt.setDate(index, date);
        } else if (value instanceof java.sql.Timestamp timestamp) {
            pstmt.setTimestamp(index, timestamp);
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

    /**
     * Generate complete health record for a patient
     * @param connection Database connection
     * @param patientId Patient ID to generate report for
     * @return Formatted string containing complete health record
     * @throws SQLException if database error occurs
     */
    public static String generatePatientHealthRecord(Connection connection, String patientId)
            throws SQLException {
        StringBuilder report = new StringBuilder();

        report.append("================== PATIENT HEALTH RECORD ==================\n\n");

        // Patient Info Section
        String patientInfoQuery = QueryBuilder.buildPatientInfoQuery();
        try (PreparedStatement pstmt = connection.prepareStatement(patientInfoQuery)) {
            pstmt.setString(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    report.append("Patient ID: ").append("P").append(rs.getString("patientID")).append("\n");
                    report.append("Name: ").append(rs.getString("fName")).append(" ");
                    String mInitial = rs.getString("mInitial");
                    if (mInitial != null && !mInitial.isEmpty()) {
                        report.append(mInitial).append(". ");
                    }
                    report.append(rs.getString("lName")).append("\n");
                    report.append("Address: ").append(rs.getString("curAddress")).append("\n");
                    report.append("Phone: ").append(rs.getString("curPhoneNo")).append("\n");
                    
                    String docId = rs.getString("primary_doctor_id");
                    String docName = rs.getString("primary_doctor_name");
                    if (docName != null) {
                        report.append("Primary Doctor: ").append(docName);
                        if (docId != null) {
                            report.append(" (D").append(docId).append(")");
                        }
                        report.append("\n");
                    }
                    String deptName = rs.getString("primary_doctor_dept");
                    if (deptName != null) {
                        report.append("Department: ").append(deptName).append("\n");
                    }
                } else {
                    report.append("Patient not found.\n");
                    return report.toString();
                }
            }
        }

        // Procedures Section
        report.append("\n==================== PROCEDURES ====================\n\n");
        String proceduresQuery = QueryBuilder.buildPatientProceduresQuery();
        try (PreparedStatement pstmt = connection.prepareStatement(proceduresQuery)) {
            pstmt.setString(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasProcedures = false;
                while (rs.next()) {
                    hasProcedures = true;
                    report.append("• Procedure: ").append(rs.getString("procedure_name"))
                            .append(" (").append(rs.getString("procedure_no")).append(")\n");
                    report.append("  Date: ").append(formatDateTime(rs.getTimestamp("performed_at"))).append("\n");
                    
                    String docId = rs.getString("doctor_id");
                    String docName = rs.getString("doctor_name");
                    if (docName != null) {
                        report.append("  Doctor: ").append(docName);
                        if (docId != null) {
                            report.append(" (D").append(docId).append(")");
                        }
                        report.append("\n");
                    }
                    
                    String notes = rs.getString("notes");
                    if (notes != null && !notes.isEmpty()) {
                        report.append("  Notes: ").append(notes).append("\n");
                    }
                    report.append("\n");
                }
                if (!hasProcedures) {
                    report.append("No procedures recorded.\n\n");
                }
            }
        }

        // Interactions Section
        report.append("==================== INTERACTIONS ====================\n\n");
        String interactionsQuery = QueryBuilder.buildPatientInteractionsQuery();
        try (PreparedStatement pstmt = connection.prepareStatement(interactionsQuery)) {
            pstmt.setString(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasInteractions = false;
                while (rs.next()) {
                    hasInteractions = true;
                    report.append("• Interaction ID: ").append(rs.getString("interaction_id")).append("\n");
                    report.append("  Time: ").append(formatDateTime(rs.getTimestamp("interactionTime"))).append("\n");
                    
                    String description = rs.getString("description");
                    if (description != null && !description.isEmpty()) {
                        report.append("  Description: ").append(description).append("\n");
                    }
                    report.append("\n");
                }
                if (!hasInteractions) {
                    report.append("No interactions recorded.\n\n");
                }
            }
        }

        // Medications Section
        report.append("==================== MEDICATIONS ====================\n\n");
        String medicationsQuery = QueryBuilder.buildPatientMedicationsQuery();
        try (PreparedStatement pstmt = connection.prepareStatement(medicationsQuery)) {
            pstmt.setString(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasMedications = false;
                while (rs.next()) {
                    hasMedications = true;
                    report.append("• Medication: ").append(rs.getString("medName")).append("\n");
                    
                    String description = rs.getString("description");
                    if (description != null && !description.isEmpty()) {
                        report.append("  Description: ").append(description).append("\n");
                    }
                    
                    report.append("  Prescribed: ").append(formatDate(rs.getDate("datePres"))).append("\n");
                    
                    String docId = rs.getString("doctor_id");
                    String docName = rs.getString("prescribing_doctor");
                    if (docName != null) {
                        report.append("  Prescribing Doctor: ").append(docName);
                        if (docId != null) {
                            report.append(" (D").append(docId).append(")");
                        }
                        report.append("\n");
                    }
                    report.append("\n");
                }
                if (!hasMedications) {
                    report.append("No medications prescribed.\n\n");
                }
            }
        }

        report.append("================== END OF REPORT ==================\n");
        return report.toString();
    }

    /**
     * Search for procedures offered by a department
     * @param connection Database connection
     * @param searchTerm Department code or name to search for
     * @return ResultSet containing matching procedures
     * @throws SQLException if database error occurs
     */
    public static ResultSet searchProceduresByDepartment(Connection connection, String searchTerm)
            throws SQLException {
        String query = QueryBuilder.buildProceduresByDepartmentQuery();
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, searchTerm);  // dept_name
        pstmt.setString(2, searchTerm);  // dept_code
        return pstmt.executeQuery();
    }

    /**
     * Search for procedures performed by a specific doctor
     * @param connection Database connection
     * @param doctorId Doctor ID to search for
     * @return ResultSet containing matching procedures
     * @throws SQLException if database error occurs
     */
    public static ResultSet searchProceduresByDoctor(Connection connection, String doctorId)
            throws SQLException {
        String query = QueryBuilder.buildProceduresByDoctorQuery();
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, doctorId);
        return pstmt.executeQuery();
    }

}
