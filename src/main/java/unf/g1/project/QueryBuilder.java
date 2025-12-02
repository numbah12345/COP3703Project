package unf.g1.project;

import java.util.Map;
import unf.g1.project.models.Model;

/**
 * Utility class for building SQL query strings. Generates parameterized queries
 * using ? placeholders for PreparedStatements.
 */
public class QueryBuilder {

    /**
     * Builds an INSERT statement from a Model object Example: INSERT INTO
     * Patient (patient_id, first_name, ...) VALUES (?, ?, ...)
     *
     * @param model The model to insert
     * @return SQL INSERT statement with ? placeholders
     */
    public static String buildInsert(Model model) {
        Map<String, Object> data = model.toMap();
        return buildInsert(model.getTableName(), data);
    }

    /**
     * Builds an INSERT statement from table name and data map
     *
     * @param tableName The table to insert into
     * @param data Map of column names to values
     * @return SQL INSERT statement with ? placeholders
     */
    public static String buildInsert(String tableName, Map<String, Object> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        int count = 0;
        for (String column : data.keySet()) {
            if (count > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(column);
            values.append("?");  // Use ? placeholder for PreparedStatement
            count++;
        }

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, columns.toString(), values.toString());
    }

    /**
     * Builds an UPDATE statement from a Model object Example: UPDATE Patient
     * SET first_name = ?, last_name = ? WHERE patient_id = ?
     *
     * @param model The model to update
     * @return SQL UPDATE statement with ? placeholders
     */
    public static String buildUpdate(Model model) {
        Map<String, Object> data = model.toMap();
        String pkColumn = model.getPrimaryKeyColumn();

        StringBuilder sets = new StringBuilder();
        int count = 0;
        for (String column : data.keySet()) {
            // Skip primary key in SET clause
            if (column.equals(pkColumn)) {
                continue;
            }

            if (count > 0) {
                sets.append(", ");
            }
            sets.append(column).append(" = ?");
            count++;
        }

        return String.format("UPDATE %s SET %s WHERE %s = ?",
                model.getTableName(), sets.toString(), pkColumn);
    }

    /**
     * Builds a SELECT statement to find a record by primary key Example: SELECT
     * * FROM Patient WHERE patient_id = ?
     *
     * @param tableName The table to select from
     * @param pkColumn The primary key column name
     * @return SQL SELECT statement with ? placeholder
     */
    public static String buildSelectById(String tableName, String pkColumn) {
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, pkColumn);
    }

    /**
     * Builds a SELECT statement with a custom WHERE clause
     *
     * @param tableName The table to select from
     * @param whereClause The WHERE condition (without "WHERE" keyword)
     * @return SQL SELECT statement
     */
    public static String buildSelect(String tableName, String whereClause) {
        if (whereClause == null || whereClause.isEmpty()) {
            return String.format("SELECT * FROM %s", tableName);
        }
        return String.format("SELECT * FROM %s WHERE %s", tableName, whereClause);
    }

    /**
     * Builds a DELETE statement by primary key Example: DELETE FROM Patient
     * WHERE patient_id = ?
     *
     * @param tableName The table to delete from
     * @param pkColumn The primary key column name
     * @return SQL DELETE statement with ? placeholder
     */
    public static String buildDeleteById(String tableName, String pkColumn) {
        return String.format("DELETE FROM %s WHERE %s = ?", tableName, pkColumn);
    }

    /**
     * Builds a DELETE statement with custom WHERE clause
     *
     * @param tableName The table to delete from
     * @param whereClause The WHERE condition (without "WHERE" keyword)
     * @return SQL DELETE statement
     */
    public static String buildDelete(String tableName, String whereClause) {
        return String.format("DELETE FROM %s WHERE %s", tableName, whereClause);
    }

    // Complex query builders for specific business logic
    /**
     * Builds query to get patient basic info with primary doctor
     */
    public static String buildPatientInfoQuery() {
        return "SELECT p.patientID, p.fName, p.mInitial, p.lName, "
                + "p.curAddress, p.curPhoneNo, "
                + "d.doctor_id AS primary_doctor_id, "
                + "d.first_name || ' ' || d.last_name AS primary_doctor_name, "
                + "dept.dept_name AS primary_doctor_dept "
                + "FROM PATIENT p "
                + "LEFT JOIN DOCTOR d ON p.priDoc = d.doctor_id "
                + "LEFT JOIN DEPARTMENTDOC dd ON d.doctor_id = dd.docID "
                + "LEFT JOIN DEPARTMENT dept ON dd.depCode = dept.dept_code "
                + "WHERE p.patientID = ?";
    }

    /**
     * Builds query to get all procedures for a patient
     */
    public static String buildPatientProceduresQuery() {
        return "SELECT pp.procedure_no, proc.procedure_name, pp.performed_at, pp.notes, "
                + "d.doctor_id, d.first_name || ' ' || d.last_name AS doctor_name "
                + "FROM PROCEDURE_PERFORMED pp "
                + "JOIN PROCEDURE proc ON pp.procedure_no = proc.procedure_no "
                + "LEFT JOIN DOCTOR d ON pp.docID = d.doctor_id "
                + "WHERE pp.patient_id = ? "
                + "ORDER BY pp.performed_at DESC";
    }

    /**
     * Builds query to get all interactions for a patient
     */
    public static String buildPatientInteractionsQuery() {
        return "SELECT interaction_id, interactionTime, description "
                + "FROM INTERACTION_RECORD "
                + "WHERE patint_id = ? "
                + "ORDER BY interactionTime DESC";
    }

    /**
     * Builds query to get all medications for a patient
     */
    public static String buildPatientMedicationsQuery() {
        return "SELECT pm.medName, m.description, pm.datePres, "
                + "d.doctor_id, d.first_name || ' ' || d.last_name AS prescribing_doctor "
                + "FROM PRESCRIBED pm "
                + "JOIN MEDICATION m ON pm.medName = m.med_name "
                + "JOIN DOCTOR d ON pm.dID = d.doctor_id "
                + "WHERE pm.pID = ? "
                + "ORDER BY pm.datePres DESC";
    }

    /**
     * Builds query to find procedures by department
     */
    public static String buildProceduresByDepartmentQuery() {
        return "SELECT p.procedure_no, p.procedure_name, p.description, p.duration_minutes "
                + "FROM PROCEDURE p "
                + "JOIN OFFERS o ON p.procedure_no = o.procNo "
                + "JOIN DEPARTMENT d ON o.depCode = d.dept_code "
                + "WHERE d.dept_name = ? OR d.dept_code = ?";
    }

    /**
     * Builds query to list all procedures done by a doctor
     */
    public static String buildProceduresByDoctorQuery() {
        return "SELECT p.procedure_no, proc.procedure_name, "
                + "p.performed_at, p.notes, "
                + "pat.fName || ' ' || pat.lName AS patient_name "
                + "FROM PROCEDURE_PERFORMED p "
                + "JOIN PROCEDURE proc ON p.procedure_no = proc.procedure_no "
                + "JOIN PATIENT pat ON p.patient_id = pat.patientID "
                + "WHERE p.docID = ? "
                + "ORDER BY p.performed_at DESC";
    }

}
