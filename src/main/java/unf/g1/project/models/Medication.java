package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Medication model representing a medication in the hospital system
 */
public class Medication extends Model {

    private String medName;             // Medication name (VARCHAR2(20)) - PRIMARY KEY
    private String manufacturer;        // Manufacturer (VARCHAR2(30))
    private String description;         // Description (VARCHAR2(100))

    // Constructors
    public Medication() {}

    public Medication(String medName, String manufacturer, String description) {
        this.medName = medName;
        this.manufacturer = manufacturer;
        this.description = description;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "MEDICATION";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "med_name";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return medName;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("med_name", medName);
        map.put("manufacturer", manufacturer);
        map.put("description", description);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (medName == null || medName.isEmpty()) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getMedName() { return medName; }
    public void setMedName(String medName) { this.medName = medName; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Medication{" +
                "medName='" + medName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
