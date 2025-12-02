package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

/**
 * Patient model representing a hospital patient
 */
public class Patient extends Model {

    private String fName;               // First name (varchar(15))
    private Character mInitial;         // Middle initial (char)
    private String lName;               // Last name (varchar(20))
    private String patientID;           // Patient ID (char(8)) - PRIMARY KEY
    private String ssn;                 // SSN (char(9))
    private String curPhoneNo;          // Current phone number (char(10))
    private String curAddress;          // Current address (VARCHAR2(50))
    private String curCity;             // Current city (char(15))
    private Integer curZip;             // Current zip code
    private String curState;            // Current state (char(15))
    private String perPhoneNo;          // Permanent phone number (char(10))
    private String perAddress;          // Permanent address (VARCHAR2(50))
    private String perCity;             // Permanent city (char(15))
    private Integer perZip;             // Permanent zip code
    private String perState;            // Permanent state (char(15))
    private Date bDate;                 // Birth date
    private Character sex;              // Sex: F=Female, M=Male, O=Other
    private Character condition;        // Condition: F=Fair, C=Critical, S=Stable
    private String priDoc;              // Primary doctor ID (VARCHAR2(12))
    private String secDoc;              // Secondary doctor ID (VARCHAR2(12))

    // Constructors
    public Patient() {
    }

    public Patient(String fName, Character mInitial, String lName, String patientID,
            String ssn, String curPhoneNo, String curAddress, String curCity,
            Integer curZip, String curState, String perPhoneNo, String perAddress,
            String perCity, Integer perZip, String perState, Date bDate,
            Character sex, Character condition, String priDoc, String secDoc) {
        this.fName = fName;
        this.mInitial = mInitial;
        this.lName = lName;
        this.patientID = patientID;
        this.ssn = ssn;
        this.curPhoneNo = curPhoneNo;
        this.curAddress = curAddress;
        this.curCity = curCity;
        this.curZip = curZip;
        this.curState = curState;
        this.perPhoneNo = perPhoneNo;
        this.perAddress = perAddress;
        this.perCity = perCity;
        this.perZip = perZip;
        this.perState = perState;
        this.bDate = bDate;
        this.sex = sex;
        this.condition = condition;
        this.priDoc = priDoc;
        this.secDoc = secDoc;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "PATIENT";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "patientID";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return patientID;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("fName", fName);
        map.put("mInitial", mInitial);
        map.put("lName", lName);
        map.put("patientID", patientID);
        map.put("ssn", ssn);
        map.put("curPhoneNo", curPhoneNo);
        map.put("curAddress", curAddress);
        map.put("curCity", curCity);
        map.put("curZip", curZip);
        map.put("curState", curState);
        map.put("perPhoneNo", perPhoneNo);
        map.put("perAddress", perAddress);
        map.put("perCity", perCity);
        map.put("perZip", perZip);
        map.put("perState", perState);
        map.put("bDate", bDate);
        map.put("sex", sex);
        map.put("condition", condition);
        map.put("priDoc", priDoc);
        map.put("secDoc", secDoc);
        return map;
    }

    @Override
    public boolean validate() {
        System.out.println("DEBUG Patient.validate(): Starting validation");

        // Patient ID validation
        System.out.println("DEBUG Patient.validate(): patientID = '" + patientID + "' (length: "
                + (patientID == null ? "null" : patientID.length()) + ")");
        if (patientID == null || patientID.length() != 8) {
            System.out.println("DEBUG Patient.validate(): FAILED - patientID must be exactly 8 characters");
            return false;
        }

        // SSN validation
        System.out.println("DEBUG Patient.validate(): ssn = '" + ssn + "' (length: "
                + (ssn == null ? "null" : ssn.length()) + ")");
        if (ssn == null || ssn.length() != 9) {
            System.out.println("DEBUG Patient.validate(): FAILED - ssn must be exactly 9 characters");
            return false;
        }

        // Name validation
        System.out.println("DEBUG Patient.validate(): fName = '" + fName + "', lName = '" + lName + "'");
        if (fName == null || lName == null) {
            System.out.println("DEBUG Patient.validate(): FAILED - fName and lName are required");
            return false;
        }

        // Current phone validation
        System.out.println("DEBUG Patient.validate(): curPhoneNo = '" + curPhoneNo + "' (length: "
                + (curPhoneNo == null ? "null" : curPhoneNo.length()) + ")");
        if (curPhoneNo == null || curPhoneNo.length() != 10) {
            System.out.println("DEBUG Patient.validate(): FAILED - curPhoneNo must be exactly 10 characters");
            return false;
        }

        // Permanent phone validation
        System.out.println("DEBUG Patient.validate(): perPhoneNo = '" + perPhoneNo + "' (length: "
                + (perPhoneNo == null ? "null" : perPhoneNo.length()) + ")");
        if (perPhoneNo == null || perPhoneNo.length() != 10) {
            System.out.println("DEBUG Patient.validate(): FAILED - perPhoneNo must be exactly 10 characters");
            return false;
        }

        // Sex validation
        System.out.println("DEBUG Patient.validate(): sex = '" + sex + "'");
        if (sex == null || (sex != 'F' && sex != 'M' && sex != 'O')) {
            System.out.println("DEBUG Patient.validate(): FAILED - sex must be F, M, or O");
            return false;
        }

        // Condition validation
        System.out.println("DEBUG Patient.validate(): condition = '" + condition + "'");
        if (condition == null || (condition != 'F' && condition != 'C' && condition != 'S')) {
            System.out.println("DEBUG Patient.validate(): FAILED - condition must be F, C, or S");
            return false;
        }

        System.out.println("DEBUG Patient.validate(): PASSED - All validations successful");
        return true;
    }

    // Getters and Setters
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Character getmInitial() {
        return mInitial;
    }

    public void setmInitial(Character mInitial) {
        this.mInitial = mInitial;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getCurPhoneNo() {
        return curPhoneNo;
    }

    public void setCurPhoneNo(String curPhoneNo) {
        this.curPhoneNo = curPhoneNo;
    }

    public String getCurAddress() {
        return curAddress;
    }

    public void setCurAddress(String curAddress) {
        this.curAddress = curAddress;
    }

    public String getCurCity() {
        return curCity;
    }

    public void setCurCity(String curCity) {
        this.curCity = curCity;
    }

    public Integer getCurZip() {
        return curZip;
    }

    public void setCurZip(Integer curZip) {
        this.curZip = curZip;
    }

    public String getCurState() {
        return curState;
    }

    public void setCurState(String curState) {
        this.curState = curState;
    }

    public String getPerPhoneNo() {
        return perPhoneNo;
    }

    public void setPerPhoneNo(String perPhoneNo) {
        this.perPhoneNo = perPhoneNo;
    }

    public String getPerAddress() {
        return perAddress;
    }

    public void setPerAddress(String perAddress) {
        this.perAddress = perAddress;
    }

    public String getPerCity() {
        return perCity;
    }

    public void setPerCity(String perCity) {
        this.perCity = perCity;
    }

    public Integer getPerZip() {
        return perZip;
    }

    public void setPerZip(Integer perZip) {
        this.perZip = perZip;
    }

    public String getPerState() {
        return perState;
    }

    public void setPerState(String perState) {
        this.perState = perState;
    }

    public Date getbDate() {
        return bDate;
    }

    public void setbDate(Date bDate) {
        this.bDate = bDate;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public Character getCondition() {
        return condition;
    }

    public void setCondition(Character condition) {
        this.condition = condition;
    }

    public String getPriDoc() {
        return priDoc;
    }

    public void setPriDoc(String priDoc) {
        this.priDoc = priDoc;
    }

    public String getSecDoc() {
        return secDoc;
    }

    public void setSecDoc(String secDoc) {
        this.secDoc = secDoc;
    }

    @Override
    public String toString() {
        return "Patient{"
                + "patientID='" + patientID + '\''
                + ", name='" + fName + " " + mInitial + " " + lName + '\''
                + ", ssn='" + ssn + '\''
                + ", bDate=" + bDate
                + ", sex=" + sex
                + ", condition=" + condition
                + ", curPhoneNo='" + curPhoneNo + '\''
                + ", currentAddress='" + curAddress + ", " + curCity + ", " + curState + " " + curZip + '\''
                + ", perPhoneNo='" + perPhoneNo + '\''
                + ", permanentAddress='" + perAddress + ", " + perCity + ", " + perState + " " + perZip + '\''
                + ", priDoc='" + priDoc + '\''
                + ", secDoc='" + secDoc + '\''
                + '}';
    }
}
