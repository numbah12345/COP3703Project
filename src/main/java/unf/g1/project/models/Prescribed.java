package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

/**
 * Prescribed model representing a medication prescribed to a patient
 */
public class Prescribed extends Model {

    private String pId;                 // Patient ID (CHAR(8))
    private String dId;                 // Doctor ID (VARCHAR2(8))
    private String medName;             // Medication name (VARCHAR2(20))
    private Date datePres;              // Prescription date

    // Constructors
    public Prescribed() {}

    public Prescribed(String pId, String dId, String medName, Date datePres) {
        this.pId = pId;
        this.dId = dId;
        this.medName = medName;
        this.datePres = datePres;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "PRESCRIBED";
    }

    @Override
    public String getPrimaryKeyColumn() {
        // Using pId as primary for simplicity (composite key in actual DB)
        return "pID";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return pId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("pID", pId);
        map.put("dID", dId);
        map.put("medName", medName);
        map.put("datePres", datePres);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (pId == null || pId.length() != 8) {
            return false;
        }
        if (dId == null || dId.isEmpty()) {
            return false;
        }
        if (medName == null || medName.isEmpty()) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getpId() { return pId; }
    public void setpId(String pId) { this.pId = pId; }

    public String getdId() { return dId; }
    public void setdId(String dId) { this.dId = dId; }

    public String getMedName() { return medName; }
    public void setMedName(String medName) { this.medName = medName; }

    public Date getDatePres() { return datePres; }
    public void setDatePres(Date datePres) { this.datePres = datePres; }

    @Override
    public String toString() {
        return "Prescribed{" +
                "pId='" + pId + '\'' +
                ", dId='" + dId + '\'' +
                ", medName='" + medName + '\'' +
                ", datePres=" + datePres +
                '}';
    }
}
