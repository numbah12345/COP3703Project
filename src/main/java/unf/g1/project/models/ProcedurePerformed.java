package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;

/**
 * ProcedurePerformed model representing a procedure performed on a patient
 */
public class ProcedurePerformed extends Model {

    private String procedureNo;         // Procedure number (CHAR(7)) - format: ABC1234
    private String patientId;           // Patient ID (CHAR(8))
    private String docId;               // Doctor ID (VARCHAR2(8))
    private Timestamp performedAt;      // Timestamp when procedure was performed
    private String notes;               // Notes about the procedure (CLOB)

    // Constructors
    public ProcedurePerformed() {}

    public ProcedurePerformed(String procedureNo, String patientId, String docId,
                             Timestamp performedAt, String notes) {
        this.procedureNo = procedureNo;
        this.patientId = patientId;
        this.docId = docId;
        this.performedAt = performedAt;
        this.notes = notes;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "PROCEDURE_PERFORMED";
    }

    @Override
    public String getPrimaryKeyColumn() {
        // Using procedureNo as primary for simplicity (composite key in actual DB)
        return "procedure_no";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return procedureNo;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("procedure_no", procedureNo);
        map.put("patient_id", patientId);
        map.put("docID", docId);
        map.put("performed_at", performedAt);
        map.put("notes", notes);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (procedureNo == null || procedureNo.length() != 7) {
            return false;
        }
        if (patientId == null || patientId.length() != 8) {
            return false;
        }
        if (docId == null || docId.isEmpty()) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getProcedureNo() { return procedureNo; }
    public void setProcedureNo(String procedureNo) { this.procedureNo = procedureNo; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public Timestamp getPerformedAt() { return performedAt; }
    public void setPerformedAt(Timestamp performedAt) { this.performedAt = performedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "ProcedurePerformed{" +
                "procedureNo='" + procedureNo + '\'' +
                ", patientId='" + patientId + '\'' +
                ", docId='" + docId + '\'' +
                ", performedAt=" + performedAt +
                ", notes='" + notes + '\'' +
                '}';
    }
}
