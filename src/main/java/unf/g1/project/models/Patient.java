package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Patient model representing a hospital patient
 */
public class Patient extends Model {

    private String fName;               // First name (varchar(15))
    private Character mInitial;         // Middle initial (char)
    private String lName;               // Last name (varchar(20))
    private String patientID;           // Patient ID (char(8))
    private String ssn;                 // Primary key: SSN (char(9))
    private Character condition;        // Condition: F=Fair, C=Critical, S=Stable
    private Character sex;              // Sex: F=Female, M=Male, O=Other
    private String phoneNumber;        // Phone number (10 digits)
    private String curCity;             // Current city (char(15))
    private Integer curZip;             // Current zip code
    private String curState;            // Current state (char(15))
    private String perCity;             // Permanent city (char(15))
    private Integer perZip;             // Permanent zip code
    private String perState;            // Permanent state (char(15))

    // Constructors
    public Patient() {}

    public Patient(String fName, Character mInitial, String lName, String patientID,
                   String ssn, Character condition, Character sex, String phoneNumber,
                   String curCity, Integer curZip, String curState,
                   String perCity, Integer perZip, String perState) {
        this.fName = fName;
        this.mInitial = mInitial;
        this.lName = lName;
        this.patientID = patientID;
        this.ssn = ssn;
        this.condition = condition;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.curCity = curCity;
        this.curZip = curZip;
        this.curState = curState;
        this.perCity = perCity;
        this.perZip = perZip;
        this.perState = perState;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "PATIENT";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "ssn";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return ssn;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("fName", fName);
        map.put("mInitial", mInitial);
        map.put("lName", lName);
        map.put("patientID", patientID);
        map.put("ssn", ssn);
        map.put("condition", condition);
        map.put("sex", sex);
        map.put("phoneNumber", phoneNumber);
        map.put("curCity", curCity);
        map.put("curZip", curZip);
        map.put("curState", curState);
        map.put("perCity", perCity);
        map.put("perZip", perZip);
        map.put("perState", perState);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (patientID == null || patientID.length() != 8) {
            return false;
        }
        if (ssn == null || ssn.length() != 9) {
            return false;
        }
        if (fName == null || lName == null) {
            return false;
        }
        if (condition != null && condition != 'F' && condition != 'C' && condition != 'S') {
            return false;
        }
        if (sex != null && sex != 'F' && sex != 'M' && sex != 'O') {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getfName() { return fName; }
    public void setfName(String fName) { this.fName = fName; }

    public Character getmInitial() { return mInitial; }
    public void setmInitial(Character mInitial) { this.mInitial = mInitial; }

    public String getlName() { return lName; }
    public void setlName(String lName) { this.lName = lName; }

    public String getPatientID() { return patientID; }
    public void setPatientID(String patientID) { this.patientID = patientID; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public Character getCondition() { return condition; }
    public void setCondition(Character condition) { this.condition = condition; }

    public Character getSex() { return sex; }
    public void setSex(Character sex) { this.sex = sex; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCurCity() { return curCity; }
    public void setCurCity(String curCity) { this.curCity = curCity; }

    public Integer getCurZip() { return curZip; }
    public void setCurZip(Integer curZip) { this.curZip = curZip; }

    public String getCurState() { return curState; }
    public void setCurState(String curState) { this.curState = curState; }

    public String getPerCity() { return perCity; }
    public void setPerCity(String perCity) { this.perCity = perCity; }

    public Integer getPerZip() { return perZip; }
    public void setPerZip(Integer perZip) { this.perZip = perZip; }

    public String getPerState() { return perState; }
    public void setPerState(String perState) { this.perState = perState; }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID='" + patientID + '\'' +
                ", name='" + fName + " " + mInitial + " " + lName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", sex=" + sex +
                ", condition=" + condition +
                ", phoneNumber=" + phoneNumber +
                ", currentAddress='" + curCity + ", " + curState + " " + curZip + '\'' +
                ", permanentAddress='" + perCity + ", " + perState + " " + perZip + '\'' +
                '}';
    }
}
