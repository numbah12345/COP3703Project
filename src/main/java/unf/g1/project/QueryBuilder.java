package unf.g1.project;

import java.lang.StringBuilder;
import java.util.Map;
import unf.g1.project.models.Model;

/**
 * Utility class for building SQL query strings.
 * Generates parameterized queries using ? placeholders for PreparedStatements.
 */
public class QueryBuilder {

    /**
     * Builds an INSERT statement from a Model object
     * Example: INSERT INTO Patient (patient_id, first_name, ...) VALUES (?, ?, ...)
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
     * Builds an UPDATE statement from a Model object
     * Example: UPDATE Patient SET first_name = ?, last_name = ? WHERE patient_id = ?
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
     * Builds a SELECT statement to find a record by primary key
     * Example: SELECT * FROM Patient WHERE patient_id = ?
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
     * Builds a DELETE statement by primary key
     * Example: DELETE FROM Patient WHERE patient_id = ?
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
        return "SELECT p.patient_id, p.first_name, p.middle_initial, p.last_name, " +
               "p.current_address, p.current_phone, " +
               "d.first_name || ' ' || d.last_name AS primary_doctor_name, " +
               "dept.dept_name AS primary_doctor_dept " +
               "FROM Patient p " +
               "LEFT JOIN Doctor d ON p.primary_doctor_id = d.doctor_id " +
               "LEFT JOIN Department dept ON d.dept_code = dept.dept_code " +
               "WHERE p.patient_id = ?";
    }



    /**
     * Builds query to get all procedures for a patient
     */
    public static String buildPatientProceduresQuery() {
        return "SELECT pp.procedure_number, proc.procedure_name, pp.procedure_date, " +
               "pp.procedure_time, pp.notes, " +
               "d.first_name || ' ' || d.last_name AS doctor_name " +
               "FROM Patient_Procedure pp " +
               "JOIN Procedure proc ON pp.procedure_number = proc.procedure_number " +
               "LEFT JOIN Procedure_Doctor pd ON pp.patient_id = pd.patient_id " +
               "    AND pp.procedure_number = pd.procedure_number " +
               "    AND pp.procedure_date = pd.procedure_date " +
               "LEFT JOIN Doctor d ON pd.doctor_id = d.doctor_id " +
               "WHERE pp.patient_id = ? " +
               "ORDER BY pp.procedure_date DESC, pp.procedure_time DESC";
    }

    /**
     * Builds query to get all interactions for a patient
     */
    public static String buildPatientInteractionsQuery() {
        return "SELECT interaction_id, interaction_date, interaction_time, description " +
               "FROM Interaction " +
               "WHERE patient_id = ? " +
               "ORDER BY interaction_date DESC, interaction_time DESC";
    }

    /**
     * Builds query to get all medications for a patient
     */
    public static String buildPatientMedicationsQuery() {
        return "SELECT pm.medication_name, m.description, pm.prescription_date, " +
               "d.first_name || ' ' || d.last_name AS prescribing_doctor " +
               "FROM Patient_Medication pm " +
               "JOIN Medication m ON pm.medication_name = m.medication_name " +
               "JOIN Doctor d ON pm.prescribing_doctor_id = d.doctor_id " +
               "WHERE pm.patient_id = ? " +
               "ORDER BY pm.prescription_date DESC";
    }

    /**
     * Builds query to find procedures by department
     */
    public static String buildProceduresByDepartmentQuery() {
        return "SELECT p.procedure_number, p.procedure_name, p.description, p.duration " +
               "FROM Procedure p " +
               "JOIN Department d ON p.dept_code = d.dept_code " +
               "WHERE d.dept_name = ? OR d.dept_code = ?";
    }

    /**
     * Builds query to list all procedures done by a doctor
     */
    public static String buildProceduresByDoctorQuery() {
        return "SELECT DISTINCT p.procedure_number, proc.procedure_name, " +
               "p.procedure_date, p.procedure_time, " +
               "pat.first_name || ' ' || pat.last_name AS patient_name " +
               "FROM Procedure_Doctor pd " +
               "JOIN Patient_Procedure p ON pd.patient_id = p.patient_id " +
               "    AND pd.procedure_number = p.procedure_number " +
               "    AND pd.procedure_date = p.procedure_date " +
               "JOIN Procedure proc ON p.procedure_number = proc.procedure_number " +
               "JOIN Patient pat ON p.patient_id = pat.patient_id " +
               "WHERE pd.doctor_id = ? " +
               "ORDER BY p.procedure_date DESC, p.procedure_time DESC";
    }
}
