package unf.g1.project.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Department model representing a hospital department
 */
public class Department extends Model {

    private String deptName;            // Department name (VARCHAR2(30))
    private String deptCode;            // Department code (VARCHAR2(10)) - PRIMARY KEY
    private String officeNumber;        // Office number (VARCHAR2(10))
    private String officePhone;         // Office phone (VARCHAR2(15))
    private String headId;              // Head doctor ID (VARCHAR2(8))

    // Constructors
    public Department() {}

    public Department(String deptName, String deptCode, String officeNumber,
                     String officePhone, String headId) {
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.officeNumber = officeNumber;
        this.officePhone = officePhone;
        this.headId = headId;
    }

    // Model implementation
    @Override
    public String getTableName() {
        return "DEPARTMENT";
    }

    @Override
    public String getPrimaryKeyColumn() {
        return "dept_code";
    }

    @Override
    public Object getPrimaryKeyValue() {
        return deptCode;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("dept_name", deptName);
        map.put("dept_code", deptCode);
        map.put("office_number", officeNumber);
        map.put("office_phone", officePhone);
        map.put("headID", headId);
        return map;
    }

    @Override
    public boolean validate() {
        // Basic validation
        if (deptName == null || deptName.isEmpty()) {
            return false;
        }
        if (deptCode == null || deptCode.isEmpty()) {
            return false;
        }
        return true;
    }

    // Getters and Setters
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }

    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }

    public String getOfficePhone() { return officePhone; }
    public void setOfficePhone(String officePhone) { this.officePhone = officePhone; }

    public String getHeadId() { return headId; }
    public void setHeadId(String headId) { this.headId = headId; }

    @Override
    public String toString() {
        return "Department{" +
                "deptCode='" + deptCode + '\'' +
                ", deptName='" + deptName + '\'' +
                ", officeNumber='" + officeNumber + '\'' +
                ", officePhone='" + officePhone + '\'' +
                ", headId='" + headId + '\'' +
                '}';
    }
}
