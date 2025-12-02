package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Procedure model representing a medical procedure
 */
public class Procedure extends Model {

    private String procedureName;       // Procedure name (VARCHAR2(50))
    private String description;         // Description (VARCHAR2(400))
    private String procedureNo;         // Procedure number (VARCHAR2(7)) - PRIMARY KEY
    private Integer durationMinutes;    // Duration in minutes

    // Constructors
    public Procedure() {
    }

    public Procedure(String procedureName, String description, String procedureNo,
            Integer durationMinutes) {
        this.procedureName = procedureName;
        this.description = description;
        this.procedureNo = procedureNo;
        this.durationMinutes = durationMinutes;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "PROCEDURE";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "procedure_no";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return procedureNo;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("procedure_name", procedureName);
        map.put("description", description);
        map.put("procedure_no", procedureNo);
        map.put("duration_minutes", durationMinutes);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (procedureName == null || procedureName.isEmpty()) {
            return false;
        }
        if (procedureNo == null || !procedureNo.matches("^[A-Z]{3}\\d{4}$")) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcedureNo() {
        return procedureNo;
    }

    public void setProcedureNo(String procedureNo) {
        this.procedureNo = procedureNo;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "Procedure{"
                + "procedureNo='" + procedureNo + '\''
                + ", procedureName='" + procedureName + '\''
                + ", description='" + description + '\''
                + ", durationMinutes=" + durationMinutes
                + '}';
    }
}
