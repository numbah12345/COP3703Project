package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

/**
 * Doctor model representing a hospital doctor
 */
public class Doctor extends Model {

    private String firstName;           // First name (VARCHAR2(30))
    private Character mInitial;         // Middle initial (CHAR)
    private String lastName;            // Last name (VARCHAR2(30))
    private String ssn;                 // SSN (CHAR(9)) - stored as digits only
    private String doctorId;            // Doctor ID (VARCHAR2(8)) - PRIMARY KEY
    private String addrs;               // Address (VARCHAR2(50))
    private String city;                // City (VARCHAR2(15))
    private String zip;                 // Zip code (VARCHAR2(15))
    private String dState;              // State (VARCHAR2(15))
    private String phone;               // Phone (CHAR(10))
    private Date bDate;                 // Birth date
    private String contactNo;           // Contact number (CHAR(10))

    // Constructors
    public Doctor() {}

    public Doctor(String firstName, String lastName, String ssn, String doctorId,
                  String addrs, String city, String zip, String dState,
                  String phone, Date bDate, String contactNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.doctorId = doctorId;
        this.addrs = addrs;
        this.city = city;
        this.zip = zip;
        this.dState = dState;
        this.phone = phone;
        this.bDate = bDate;
        this.contactNo = contactNo;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "DOCTOR";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "doctor_id";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return doctorId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("first_name", firstName);
        map.put("mInitial", mInitial);
        map.put("last_name", lastName);
        map.put("ssn", ssn);
        map.put("doctor_id", doctorId);
        map.put("addrs", addrs);
        map.put("city", city);
        map.put("zip", zip);
        map.put("dState", dState);
        map.put("phone", phone);
        map.put("bDate", bDate);
        map.put("contactNo", contactNo);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (firstName == null || lastName == null) {
            return false;
        }
        if (ssn == null || ssn.length() != 9) {
            return false;
        }
        if (doctorId == null || doctorId.length() != 8) {
            return false;
        }
        if (phone != null && phone.length() != 10) {
            return false;
        }
        if (contactNo != null && contactNo.length() != 10) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public Character getmInitial() { return mInitial; }
    public void setmInitial(Character mInitial) { this.mInitial = mInitial; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getAddrs() { return addrs; }
    public void setAddrs(String addrs) { this.addrs = addrs; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public String getdState() { return dState; }
    public void setdState(String dState) { this.dState = dState; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Date getbDate() { return bDate; }
    public void setbDate(Date bDate) { this.bDate = bDate; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=D'" + doctorId + '\'' +
                ", name='" + firstName + " " + lastName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", bDate=" + bDate +
                ", phone='" + phone + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", address='" + addrs + ", " + city + ", " + dState + " " + zip + '\'' +
                '}';
    }
}
