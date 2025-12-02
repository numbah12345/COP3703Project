package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;

/**
 * Interaction model representing a patient interaction record
 */
public class Interaction extends Model {

    private String patintId;            // Patient ID (CHAR(8)) - PRIMARY KEY
    private Integer interactionId;      // Interaction ID (INT)
    private Timestamp interactionTime;  // Interaction time (TIMESTAMP)
    private String description;         // Description (VARCHAR2(500))

    // Constructors
    public Interaction() {
    }

    public Interaction(String patintId, Integer interactionId, Timestamp interactionTime,
            String description) {
        this.patintId = patintId;
        this.interactionId = interactionId;
        this.interactionTime = interactionTime;
        this.description = description;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "INTERACTION_RECORD";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "patint_id";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return patintId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("patint_id", patintId);
        map.put("interaction_id", interactionId);
        map.put("interactionTime", interactionTime);
        map.put("description", description);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (patintId == null || patintId.length() != 8) {
            return false;
        }
        if (interactionId == null) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getPatintId() {
        return patintId;
    }

    public void setPatintId(String patintId) {
        this.patintId = patintId;
    }

    public Integer getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(Integer interactionId) {
        this.interactionId = interactionId;
    }

    public Timestamp getInteractionTime() {
        return interactionTime;
    }

    public void setInteractionTime(Timestamp interactionTime) {
        this.interactionTime = interactionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Interaction{"
                + "patintId='" + patintId + '\''
                + ", interactionId=" + interactionId
                + ", interactionTime=" + interactionTime
                + ", description='" + description + '\''
                + '}';
    }
}
