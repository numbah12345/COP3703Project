package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

/**
 * Patient model representing a hospital patient
 */
public class Patient extends Model {

    private String patientId;           // Primary key: P12345678
    private String ssn;                 // AAA-GG-SSSS
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String currentAddress;
    private String currentPhone;
    private String permAddress;
    private String permPhone;
    private String permCity;
    private String permState;
    private String permZip;
    private Date birthdate;
    private String sex;                 // Male, Female, or user-provided
    private String condition;           // Critical, Stable, Fair
    private String primaryDoctorId;     // FK to Doctor
    private String secondaryDoctorId;   // FK to Doctor (nullable)

    // Constructors
    public Patient() {}

    public Patient(String patientId, String ssn, String firstName, String middleInitial,
                   String lastName, String currentAddress, String currentPhone,
                   String permAddress, String permPhone, String permCity, String permState,
                   String permZip, Date birthdate, String sex, String condition,
                   String primaryDoctorId, String secondaryDoctorId) {
        this.patientId = patientId;
        this.ssn = ssn;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.currentAddress = currentAddress;
        this.currentPhone = currentPhone;
        this.permAddress = permAddress;
        this.permPhone = permPhone;
        this.permCity = permCity;
        this.permState = permState;
        this.permZip = permZip;
        this.birthdate = birthdate;
        this.sex = sex;
        this.condition = condition;
        this.primaryDoctorId = primaryDoctorId;
        this.secondaryDoctorId = secondaryDoctorId;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "Patient";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "patient_id";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return patientId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("patient_id", patientId);
        map.put("ssn", ssn);
        map.put("first_name", firstName);
        map.put("middle_initial", middleInitial);
        map.put("last_name", lastName);
        map.put("current_address", currentAddress);
        map.put("current_phone", currentPhone);
        map.put("perm_address", permAddress);
        map.put("perm_phone", permPhone);
        map.put("perm_city", permCity);
        map.put("perm_state", permState);
        map.put("perm_zip", permZip);
        map.put("birthdate", birthdate);
        map.put("sex", sex);
        map.put("condition", condition);
        map.put("primary_doctor_id", primaryDoctorId);
        map.put("secondary_doctor_id", secondaryDoctorId);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (patientId == null || !patientId.matches("P\\d{8}")) {
            return false;
        }
        if (ssn == null || !ssn.matches("\\d{3}-\\d{2}-\\d{4}")) {
            return false;
        }
        if (firstName == null || lastName == null) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleInitial() { return middleInitial; }
    public void setMiddleInitial(String middleInitial) { this.middleInitial = middleInitial; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCurrentAddress() { return currentAddress; }
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }

    public String getCurrentPhone() { return currentPhone; }
    public void setCurrentPhone(String currentPhone) { this.currentPhone = currentPhone; }

    public String getPermAddress() { return permAddress; }
    public void setPermAddress(String permAddress) { this.permAddress = permAddress; }

    public String getPermPhone() { return permPhone; }
    public void setPermPhone(String permPhone) { this.permPhone = permPhone; }

    public String getPermCity() { return permCity; }
    public void setPermCity(String permCity) { this.permCity = permCity; }

    public String getPermState() { return permState; }
    public void setPermState(String permState) { this.permState = permState; }

    public String getPermZip() { return permZip; }
    public void setPermZip(String permZip) { this.permZip = permZip; }

    public Date getBirthdate() { return birthdate; }
    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getPrimaryDoctorId() { return primaryDoctorId; }
    public void setPrimaryDoctorId(String primaryDoctorId) { this.primaryDoctorId = primaryDoctorId; }

    public String getSecondaryDoctorId() { return secondaryDoctorId; }
    public void setSecondaryDoctorId(String secondaryDoctorId) { this.secondaryDoctorId = secondaryDoctorId; }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + firstName + " " + middleInitial + " " + lastName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
