package unf.g1.project;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import unf.g1.project.models.Department;
import unf.g1.project.models.Doctor;
import unf.g1.project.models.Interaction;
import unf.g1.project.models.Medication;
import unf.g1.project.models.Patient;
import unf.g1.project.models.Prescribed;
import unf.g1.project.models.Procedure;
import unf.g1.project.models.ProcedurePerformed;

public class Gui {
    JFrame mainWindow;
    JFrame addWindow;  
    JFrame searchWindow;
    JFrame reportsWindow;
    JTextArea mainTextArea;

    // Database connection
    private final Connection connection;

    // Date formatters for consistent date display
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    private static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sqlTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Convert MM/DD/YYYY date string to SQL date format (YYYY-MM-DD)
     */
    private String convertToSqlDate(String dateStr) throws java.text.ParseException {
        java.util.Date date = dateFormat.parse(dateStr);
        return sqlDateFormat.format(date);
    }

    /**
     * Convert MM/DD/YYYY HH:MM AM/PM to SQL timestamp format (YYYY-MM-DD HH:MM:SS)
     */
    private String convertToSqlTimestamp(String timestampStr) throws java.text.ParseException {
        java.util.Date date = dateTimeFormat.parse(timestampStr);
        return sqlTimestampFormat.format(date);
    }

    // Track which form is currently active
    private String currentFormType;

    // Patient form fields
    private JTextField patientIdField, fNameField, mInitialField, lNameField;
    private JTextField ssnField, sexField, conditionField;
    private JTextField curPhoneField, curCityField, curZipField, curStateField;
    private JTextField curAddressField, perPhoneField, perCityField, perZipField;
    private JTextField perStateField, perAddressField, bDateField;
    private JTextField priDocField, secDocField;

    // Doctor form fields
    private JTextField doctorIdField, docFNameField, docMInitialField, docLNameField;
    private JTextField docSsnField, docPhoneField, docContactField;
    private JTextField docAddressField, docCityField, docZipField, docStateField;
    private JTextField docBDateField;

    // Department form fields
    private JTextField deptCodeField, deptNameField, officeNumField;
    private JTextField officePhoneField, headIdField;

    // Procedure form fields
    private JTextField procNoField, procNameField, procDescField, procDurationField, procDepCodeField;

    // Medication form fields
    private JTextField medNameField, medManufacturerField, medDescField;

    // Interaction form fields
    private JTextField intPatientIdField, intIdField, intDateField, intDescField;

    // Procedure Performed form fields
    private JTextField perfProcNoField, perfPatientIdField, perfDocIdField, perfDateTimeField;
    private JTextField perfNotesField;

    // Prescribed (Prescription) form fields
    private JTextField presPatientIdField, presDocIdField, presMedNameField, presDateField;

    // Search form tracking
    private String currentSearchFormType;

    // Search form fields for Patient
    private JTextField searchPatientIdField, searchPatientNameField;

    // Search form fields for Doctor
    private JTextField searchDoctorIdField, searchDoctorNameField;

    // Search form fields for Department
    private JTextField searchDeptCodeField, searchDeptNameField;

    public Gui(Connection connection) {
        this.connection = connection;
        mainWindow = new JFrame("Database Manager");
        mainWindow.setSize(1280,720);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close all windows when main closes
        mainWindow.setLayout(new GridBagLayout());
        mainWindow.setBackground(Color.DARK_GRAY);

        // Add window listener to close all child windows when main window closes
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (addWindow != null) {
                    addWindow.dispose();
                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();

        // Sidebar (Left side)
        Container sideBar = new Container();
        sideBar.setLayout(new GridBagLayout());
        GridBagConstraints sidebarConstraints = new GridBagConstraints();
        sidebarConstraints.fill = GridBagConstraints.HORIZONTAL;
        sidebarConstraints.insets = new Insets(5, 5, 5, 5);

        //Add button
        JButton addButton = new JButton("Add/Insert Data");
        addButton.addActionListener(e -> openAddWindow());
        sidebarConstraints.gridx = 0;
        sidebarConstraints.gridy = 1;
        sideBar.add(addButton, sidebarConstraints);

        //View button
        JButton viewButton = new JButton("Search/View Data");
        viewButton.addActionListener(e -> openSearchWindow());
        sidebarConstraints.gridy = 0;
        sideBar.add(viewButton, sidebarConstraints);

        //Reports button
        JButton reportsButton = new JButton("View Reports");
        reportsButton.addActionListener(e -> openReportsWindow());
        sidebarConstraints.gridy = 2;
        sideBar.add(reportsButton, sidebarConstraints);

        // Main Content (Right side)
        Container mainContent = new Container();
        mainContent.setBackground(Color.GRAY);
        mainContent.setLayout(new BorderLayout());
        mainTextArea = new JTextArea("Database content will appear here...");
        mainTextArea.setBackground(Color.LIGHT_GRAY);
        mainTextArea.setEditable(false);
        mainContent.add(mainTextArea, BorderLayout.CENTER);

        // Add sidebar to main window (left column)
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;  // Don't expand horizontally
        constraints.weighty = 1.0;  // Expand vertically
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        mainWindow.add(sideBar, constraints);

        // Add main content to main window (right column)
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;  // Expand to fill remaining horizontal space
        constraints.weighty = 1.0;  // Expand vertically
        constraints.fill = GridBagConstraints.BOTH;
        mainWindow.add(mainContent, constraints);

        mainWindow.setVisible(true);


    }

    private void openSearchWindow() {
        // Check if window already exists and is visible
        if (searchWindow != null && searchWindow.isVisible()) {
            searchWindow.toFront(); // Bring to front instead of creating new one
            return;
        }

        // Create new window if it doesn't exist
        searchWindow = new JFrame("Search Database");
        searchWindow.setSize(600, 600);
        searchWindow.setLocationRelativeTo(mainWindow);  // Center on main window
        searchWindow.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15, 15, 15, 15);

        // Model Container (Top - Fixed height)
        Container modelContainer = new Container();
        modelContainer.setLayout(new FlowLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        searchWindow.add(modelContainer, constraints);

        // Forms Container (Middle - Takes most space)
        Container formsContainer = new Container();
        formsContainer.setLayout(new GridBagLayout());

        constraints.gridy = 1;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        searchWindow.add(formsContainer, constraints);

        // Control Container (Bottom - Fixed height)
        Container controlContainer = new Container();
        controlContainer.setLayout(new GridBagLayout());

        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.weightx = 1;

        // Cancel button - disposes the search window
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> searchWindow.dispose());
        controlContainer.add(cancelButton, constraints1);

        // Search button - handles search submission
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> handleSearchSubmit());
        controlContainer.add(searchButton, constraints1);

        constraints.gridy = 2;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        searchWindow.add(controlContainer, constraints);

        // Entity type buttons
        JButton searchPatientButton = new JButton("Patient");
        searchPatientButton.addActionListener(e -> searchPatientForm(formsContainer));
        modelContainer.add(searchPatientButton);

        JButton searchDoctorButton = new JButton("Doctor");
        searchDoctorButton.addActionListener(e -> searchDoctorForm(formsContainer));
        modelContainer.add(searchDoctorButton);

        JButton searchDeptButton = new JButton("Department");
        searchDeptButton.addActionListener(e -> searchDepartmentForm(formsContainer));
        modelContainer.add(searchDeptButton);

        // Load Patient search form by default
        searchPatientForm(formsContainer);

        searchWindow.setVisible(true);
    }

    private void openAddWindow() {
        // Check if window already exists and is visible
        if (addWindow != null && addWindow.isVisible()) {
            addWindow.toFront();  // Bring to front instead of creating new one
            return;
        }

        // Create new window if it doesn't exist
        addWindow = new JFrame("Add Data");
        addWindow.setSize(700, 800);
        
        addWindow.setLocationRelativeTo(mainWindow);  // Center on main window
        addWindow.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15,15,15,15);

        // Model Container (Top - Fixed height)
        Container modelContainer = new Container();
        modelContainer.setLayout(new FlowLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;  // Take full width
        constraints.weighty = 0.0;  // Don't expand vertically
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        addWindow.add(modelContainer, constraints);

        // Forms Container (Middle - Takes most space)
        Container formsContainer = new Container();
        formsContainer.setLayout(new GridBagLayout());

        constraints.gridy = 1;
        constraints.weighty = 1.0;  // Expand to take all available vertical space
        constraints.fill = GridBagConstraints.BOTH;  // Fill both horizontal and vertical
        addWindow.add(formsContainer, constraints);

        // Control Container (Bottom - Fixed height)
        Container controlContainer = new Container();
        controlContainer.setLayout(new GridBagLayout());

        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.weightx = 1;

        // Cancel button - disposes the add window
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> addWindow.dispose());
        controlContainer.add(cancelButton, constraints1);

        // Submit button - handles form submission
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleFormSubmit());
        controlContainer.add(submitButton, constraints1);

        constraints.gridy = 2;
        constraints.weighty = 0.0;  // Don't expand vertically
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        addWindow.add(controlContainer, constraints);

        JButton addPatientButton = new JButton("Patient");
        addPatientButton.addActionListener(e -> addPatientForm(formsContainer));
        modelContainer.add(addPatientButton);

        JButton addDoctorButton = new JButton("Doctor");
        addDoctorButton.addActionListener(e -> addDoctorForm(formsContainer));
        modelContainer.add(addDoctorButton);

        JButton addDeptButton = new JButton("Department");
        addDeptButton.addActionListener(e -> addDepartmentForm(formsContainer));
        modelContainer.add(addDeptButton);

        JButton addProcButton = new JButton("Procedure");
        addProcButton.addActionListener(e -> addProcedureForm(formsContainer));
        modelContainer.add(addProcButton);

        JButton addMedicationButton = new JButton("Medication");
        addMedicationButton.addActionListener(e -> addMedicationForm(formsContainer));
        modelContainer.add(addMedicationButton);

        JButton addInteractionButton = new JButton("Interaction");
        addInteractionButton.addActionListener(e -> addInteractionForm(formsContainer));
        modelContainer.add(addInteractionButton);

        JButton addProcPerformedButton = new JButton("Procedure Performed");
        addProcPerformedButton.addActionListener(e -> addProcedurePerformedForm(formsContainer));
        modelContainer.add(addProcPerformedButton);

        JButton addPrescriptionButton = new JButton("Prescribe Medication");
        addPrescriptionButton.addActionListener(e -> addPrescriptionForm(formsContainer));
        modelContainer.add(addPrescriptionButton);

        // Load Patient form by default
        addPatientForm(formsContainer);

        addWindow.setVisible(true);
    }

    private void addPatientForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Patient ID (Primary Key)
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Patient ID*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        patientIdField = new JTextField(8);
        forms.add(patientIdField, constraints);
        row++;

        // First Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("First Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        fNameField = new JTextField(15);
        forms.add(fNameField, constraints);
        row++;

        // Middle Initial
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Middle Initial:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        mInitialField = new JTextField(1);
        forms.add(mInitialField, constraints);
        row++;

        // Last Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Last Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        lNameField = new JTextField(20);
        forms.add(lNameField, constraints);
        row++;

        // SSN
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("SSN* (9 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        ssnField = new JTextField(9);
        forms.add(ssnField, constraints);
        row++;

        // Birth Date
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Birth Date (MM/DD/YYYY):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        bDateField = new JTextField(10);
        forms.add(bDateField, constraints);
        row++;

        // Sex
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Sex* (F/M/O):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        sexField = new JTextField(1);
        forms.add(sexField, constraints);
        row++;

        // Condition
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Condition* (F/C/S):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        conditionField = new JTextField(1);
        forms.add(conditionField, constraints);
        row++;

        // Current Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current Phone* (10 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        curPhoneField = new JTextField(10);
        forms.add(curPhoneField, constraints);
        row++;

        // Current Address
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current Address:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        curAddressField = new JTextField(50);
        forms.add(curAddressField, constraints);
        row++;

        // Current City
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current City:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        curCityField = new JTextField(15);
        forms.add(curCityField, constraints);
        row++;

        // Current State
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current State:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        curStateField = new JTextField(15);
        forms.add(curStateField, constraints);
        row++;

        // Current Zip
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current Zip:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        curZipField = new JTextField(5);
        forms.add(curZipField, constraints);
        row++;

        // Permanent Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent Phone* (10 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perPhoneField = new JTextField(10);
        forms.add(perPhoneField, constraints);
        row++;

        // Permanent Address
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent Address:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perAddressField = new JTextField(50);
        forms.add(perAddressField, constraints);
        row++;

        // Permanent City
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent City:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perCityField = new JTextField(15);
        forms.add(perCityField, constraints);
        row++;

        // Permanent State
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent State:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perStateField = new JTextField(15);
        forms.add(perStateField, constraints);
        row++;

        // Permanent Zip
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent Zip:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perZipField = new JTextField(5);
        forms.add(perZipField, constraints);
        row++;

        // Primary Doctor
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Primary Doctor ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        priDocField = new JTextField(12);
        forms.add(priDocField, constraints);
        row++;

        // Secondary Doctor
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Secondary Doctor ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        secDocField = new JTextField(12);
        forms.add(secDocField, constraints);

        // Set current form type
        currentFormType = "patient";

        forms.revalidate();
        forms.repaint();
    }

    private void addDoctorForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Doctor ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Doctor ID*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        doctorIdField = new JTextField(10);
        forms.add(doctorIdField, constraints);
        row++;

        // First Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("First Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docFNameField = new JTextField(15);
        forms.add(docFNameField, constraints);
        row++;

        // Middle Initial
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Middle Initial:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docMInitialField = new JTextField(1);
        forms.add(docMInitialField, constraints);
        row++;

        // Last Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Last Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docLNameField = new JTextField(20);
        forms.add(docLNameField, constraints);
        row++;

        // SSN
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("SSN (9 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docSsnField = new JTextField(9);
        forms.add(docSsnField, constraints);
        row++;

        // Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Phone (10 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docPhoneField = new JTextField(10);
        forms.add(docPhoneField, constraints);
        row++;

        // Contact Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Contact Number (10 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docContactField = new JTextField(10);
        forms.add(docContactField, constraints);
        row++;

        // Address
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Address:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docAddressField = new JTextField(50);
        forms.add(docAddressField, constraints);
        row++;

        // City
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("City:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docCityField = new JTextField(15);
        forms.add(docCityField, constraints);
        row++;

        // State
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("State:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docStateField = new JTextField(15);
        forms.add(docStateField, constraints);
        row++;

        // Zip
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Zip:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docZipField = new JTextField(5);
        forms.add(docZipField, constraints);
        row++;

        // Birth Date
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Birth Date (MM/DD/YYYY):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        docBDateField = new JTextField(10);
        forms.add(docBDateField, constraints);

        // Set current form type
        currentFormType = "doctor";

        forms.revalidate();
        forms.repaint();
    }

    private void addDepartmentForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Department Code
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Code*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        deptCodeField = new JTextField(10);
        forms.add(deptCodeField, constraints);
        row++;

        // Department Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        deptNameField = new JTextField(30);
        forms.add(deptNameField, constraints);
        row++;

        // Office Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Office Number:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        officeNumField = new JTextField(10);
        forms.add(officeNumField, constraints);
        row++;

        // Office Phone
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Office Phone:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        officePhoneField = new JTextField(15);
        forms.add(officePhoneField, constraints);
        row++;

        // Head ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Head ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        headIdField = new JTextField(8);
        forms.add(headIdField, constraints);

        // Set current form type
        currentFormType = "department";

        forms.revalidate();
        forms.repaint();
    }

    private void addProcedureForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Procedure Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Procedure Number* (ABC1234):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        procNoField = new JTextField(7);
        forms.add(procNoField, constraints);
        row++;

        // Procedure Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Procedure Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        procNameField = new JTextField(50);
        forms.add(procNameField, constraints);
        row++;

        // Description
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Description:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        procDescField = new JTextField(400);
        forms.add(procDescField, constraints);
        row++;

        // Duration (minutes)
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Duration (minutes):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        procDurationField = new JTextField(5);
        forms.add(procDurationField, constraints);
        row++;

        // Department Code
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Code*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        procDepCodeField = new JTextField(4);
        forms.add(procDepCodeField, constraints);

        // Set current form type
        currentFormType = "procedure";

        forms.revalidate();
        forms.repaint();
    }

    private void addMedicationForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Medication Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Medication Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        medNameField = new JTextField(20);
        forms.add(medNameField, constraints);
        row++;

        // Manufacturer
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Manufacturer:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        medManufacturerField = new JTextField(30);
        forms.add(medManufacturerField, constraints);
        row++;

        // Description
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Description:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        medDescField = new JTextField(100);
        forms.add(medDescField, constraints);

        // Set current form type
        currentFormType = "medication";

        forms.revalidate();
        forms.repaint();
    }

    private void addInteractionForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Patient ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Patient ID*"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        intPatientIdField = new JTextField(8);
        forms.add(intPatientIdField, constraints);
        row++;

        // Interaction ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Interaction ID*"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        intIdField = new JTextField(8);
        forms.add(intIdField, constraints);
        row++;

        // Interaction Date/Time
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Date/Time (MM/DD/YYYY HH:MM AM/PM):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        intDateField = new JTextField(25);
        forms.add(intDateField, constraints);
        row++;

        // Description
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Description:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        intDescField = new JTextField(500);
        forms.add(intDescField, constraints);

        // Set current form type
        currentFormType = "interaction";

        forms.revalidate();
        forms.repaint();
    }

    private void addProcedurePerformedForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Procedure Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Procedure Number* "), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perfProcNoField = new JTextField(7);
        forms.add(perfProcNoField, constraints);
        row++;

        // Patient ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Patient ID*"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perfPatientIdField = new JTextField(8);
        forms.add(perfPatientIdField, constraints);
        row++;

        // Doctor ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Doctor ID*"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perfDocIdField = new JTextField(8);
        forms.add(perfDocIdField, constraints);
        row++;

        // Date/Time
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Date/Time (MM/DD/YYYY HH:MM AM/PM):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perfDateTimeField = new JTextField(19);
        forms.add(perfDateTimeField, constraints);
        row++;

        // Notes
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Notes:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        perfNotesField = new JTextField(500);
        forms.add(perfNotesField, constraints);

        // Set current form type
        currentFormType = "procedurePerformed";

        forms.revalidate();
        forms.repaint();
    }

    private void addPrescriptionForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Patient ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Patient ID*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        presPatientIdField = new JTextField(8);
        forms.add(presPatientIdField, constraints);
        row++;

        // Doctor ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Doctor ID*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        presDocIdField = new JTextField(8);
        forms.add(presDocIdField, constraints);
        row++;

        // Medication Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Medication Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        presMedNameField = new JTextField(20);
        forms.add(presMedNameField, constraints);
        row++;

        // Prescription Date
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Prescription Date* (MM/DD/YYYY):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        presDateField = new JTextField(10);
        forms.add(presDateField, constraints);

        // Set current form type
        currentFormType = "prescription";

        forms.revalidate();
        forms.repaint();
    }

    /**
     * Routes form submission to the appropriate handler based on currentFormType
     */
    private void handleFormSubmit() {
        if (currentFormType == null) {
            JOptionPane.showMessageDialog(addWindow,
                "Please select a form type first.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (currentFormType) {
            case "patient" -> submitPatient();
            case "doctor" -> submitDoctor();
            case "department" -> submitDepartment();
            case "procedure" -> submitProcedure();
            case "medication" -> submitMedication();
            case "interaction" -> submitInteraction();
            case "procedurePerformed" -> submitProcedurePerformed();
            case "prescription" -> submitPrescription();
            default -> JOptionPane.showMessageDialog(addWindow,
                    "Unknown form type: " + currentFormType,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit patient form with validation
     */
    private void submitPatient() {
        try {
            Patient patient = new Patient();

            // Extract and set all fields
            patient.setPatientID(patientIdField.getText().trim());
            patient.setfName(fNameField.getText().trim());

            String mInit = mInitialField.getText().trim();
            if (!mInit.isEmpty()) {
                patient.setmInitial(mInit.charAt(0));
            }

            patient.setlName(lNameField.getText().trim());
            patient.setSsn(ssnField.getText().trim());

            String sexText = sexField.getText().trim();
            if (!sexText.isEmpty()) {
                patient.setSex(sexText.charAt(0));
            }

            String condText = conditionField.getText().trim();
            if (!condText.isEmpty()) {
                patient.setCondition(condText.charAt(0));
            }

            patient.setCurPhoneNo(curPhoneField.getText().trim());
            patient.setCurAddress(curAddressField.getText().trim());
            patient.setCurCity(curCityField.getText().trim());
            patient.setCurState(curStateField.getText().trim());

            String curZipText = curZipField.getText().trim();
            if (!curZipText.isEmpty()) {
                patient.setCurZip(Integer.parseInt(curZipText));
            }

            patient.setPerPhoneNo(perPhoneField.getText().trim());
            patient.setPerAddress(perAddressField.getText().trim());
            patient.setPerCity(perCityField.getText().trim());
            patient.setPerState(perStateField.getText().trim());

            String perZipText = perZipField.getText().trim();
            if (!perZipText.isEmpty()) {
                patient.setPerZip(Integer.parseInt(perZipText));
            }

            String bDateText = bDateField.getText().trim();
            if (!bDateText.isEmpty()) {
                patient.setbDate(java.sql.Date.valueOf(convertToSqlDate(bDateText)));
            }

            patient.setPriDoc(priDocField.getText().trim());
            patient.setSecDoc(secDocField.getText().trim());

            // Validate the model
            if (!patient.validate()) {
                JOptionPane.showMessageDialog(addWindow, """
                                                         Validation failed! Please check required fields:
                                                         - Patient ID (8 chars)
                                                         - SSN (9 digits)
                                                         - First/Last Name
                                                         - Current Phone (10 digits)
                                                         - Permanent Phone (10 digits)
                                                         - Sex (F/M/O)
                                                         - Condition (F/C/S)""",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into database
            int rowsInserted = DatabaseManager.insert(connection, patient);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Patient added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearPatientForm();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid number format in zip code field",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid date format. Use MM/DD/YYYY",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting patient: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit doctor form with validation
     */
    private void submitDoctor() {
        try {
            Doctor doctor = new Doctor();
            doctor.setDoctorId(doctorIdField.getText().trim());
            doctor.setFirstName(docFNameField.getText().trim());

            String mInit = docMInitialField.getText().trim();
            if (!mInit.isEmpty()) {
                doctor.setmInitial(mInit.charAt(0));
            }

            doctor.setLastName(docLNameField.getText().trim());
            doctor.setSsn(docSsnField.getText().trim());
            doctor.setPhone(docPhoneField.getText().trim());
            doctor.setContactNo(docContactField.getText().trim());
            doctor.setAddrs(docAddressField.getText().trim());
            doctor.setCity(docCityField.getText().trim());
            doctor.setdState(docStateField.getText().trim());
            doctor.setZip(docZipField.getText().trim());

            String bDateText = docBDateField.getText().trim();
            if (!bDateText.isEmpty()) {
                doctor.setbDate(java.sql.Date.valueOf(convertToSqlDate(bDateText)));
            }

            if (!doctor.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Doctor ID (8 chars)\n" +
                    "- SSN (9 digits)\n" +
                    "- First/Last Name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, doctor);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Doctor added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearDoctorForm();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid date format. Use MM/DD/YYYY",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting doctor: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit department form with validation
     */
    private void submitDepartment() {
        try {
            Department dept = new Department();

            dept.setDeptCode(deptCodeField.getText().trim());
            dept.setDeptName(deptNameField.getText().trim());
            dept.setOfficeNumber(officeNumField.getText().trim());
            dept.setOfficePhone(officePhoneField.getText().trim());
            dept.setHeadId(headIdField.getText().trim());

            if (!dept.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Department Code\n" +
                    "- Department Name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, dept);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Department added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearDepartmentForm();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting department: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit procedure form with validation
     */
    private void submitProcedure() {
        try {
            // Extract and validate department code first
            String depCode = procDepCodeField.getText().trim().toUpperCase();
            if (depCode.isEmpty()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Department Code is required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Procedure proc = new Procedure();

            proc.setProcedureNo(procNoField.getText().trim());
            proc.setProcedureName(procNameField.getText().trim());
            proc.setDescription(procDescField.getText().trim());

            String durationText = procDurationField.getText().trim();
            if (!durationText.isEmpty()) {
                proc.setDurationMinutes(Integer.parseInt(durationText));
            }

            if (!proc.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Procedure Number (ABC1234 format)\n" +
                    "- Procedure Name\n" +
                    "- Department Code",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, proc);

            if (rowsInserted > 0) {
                // Procedure inserted successfully, now insert into OFFERS
                try {
                    String offersSql = "INSERT INTO OFFERS (procNo, depCode) VALUES (?, ?)";
                    PreparedStatement offersPstmt = connection.prepareStatement(offersSql);
                    offersPstmt.setString(1, proc.getProcedureNo());
                    offersPstmt.setString(2, depCode);

                    int offersRows = offersPstmt.executeUpdate();
                    offersPstmt.close();

                    if (offersRows > 0) {
                        JOptionPane.showMessageDialog(addWindow,
                            "Procedure added successfully and linked to department!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        clearProcedureForm();
                    } else {
                        JOptionPane.showMessageDialog(addWindow,
                            "Procedure added but failed to link to department.\n" +
                            "You may need to manually link this procedure to a department.",
                            "Partial Success",
                            JOptionPane.WARNING_MESSAGE);
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(addWindow,
                        "Procedure added but error linking to department: " + e.getMessage() +
                        "\nYou may need to manually link this procedure to a department.",
                        "Partial Success",
                        JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid number format in duration field",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting procedure: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit medication form with validation
     */
    private void submitMedication() {
        try {
            Medication med = new Medication();

            med.setMedName(medNameField.getText().trim());
            med.setManufacturer(medManufacturerField.getText().trim());
            med.setDescription(medDescField.getText().trim());

            if (!med.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Medication Name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, med);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Medication added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearMedicationForm();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting medication: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit interaction form with validation
     */
    private void submitInteraction() {
        try {
            Interaction interaction = new Interaction();

            interaction.setPatintId(intPatientIdField.getText().trim());

            String idText = intIdField.getText().trim();
            if (!idText.isEmpty()) {
                interaction.setInteractionId(Integer.parseInt(idText));
            }

            String dateTimeText = intDateField.getText().trim();
            if (!dateTimeText.isEmpty()) {
                // Convert MM/DD/YYYY HH:MM AM/PM to SQL timestamp format
                interaction.setInteractionTime(java.sql.Timestamp.valueOf(convertToSqlTimestamp(dateTimeText)));
            }

            interaction.setDescription(intDescField.getText().trim());

            if (!interaction.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Patient ID (8 chars)\n" +
                    "- Interaction ID (integer)",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, interaction);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Interaction added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearInteractionForm();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid number format for Interaction ID",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | java.text.ParseException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid timestamp format. Use MM/DD/YYYY HH:MM AM/PM",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting interaction: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit procedure performed form with validation
     */
    private void submitProcedurePerformed() {
        try {
            ProcedurePerformed procPerf = new ProcedurePerformed();

            procPerf.setProcedureNo(perfProcNoField.getText().trim());
            procPerf.setPatientId(perfPatientIdField.getText().trim());
            procPerf.setDocId(perfDocIdField.getText().trim());

            String dateTimeText = perfDateTimeField.getText().trim();
            if (!dateTimeText.isEmpty()) {
                procPerf.setPerformedAt(java.sql.Timestamp.valueOf(convertToSqlTimestamp(dateTimeText)));
            }

            procPerf.setNotes(perfNotesField.getText().trim());

            if (!procPerf.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Procedure Number (7 chars)\n" +
                    "- Patient ID (8 chars)\n" +
                    "- Doctor ID",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, procPerf);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Procedure performed record added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearProcedurePerformedForm();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid timestamp format. Use MM/DD/YYYY HH:MM AM/PM",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting procedure performed: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Submit prescription form with validation
     */
    private void submitPrescription() {
        try {
            Prescribed prescription = new Prescribed();

            prescription.setpId(presPatientIdField.getText().trim());
            prescription.setdId(presDocIdField.getText().trim());
            prescription.setMedName(presMedNameField.getText().trim());

            String dateText = presDateField.getText().trim();
            if (!dateText.isEmpty()) {
                prescription.setDatePres(java.sql.Date.valueOf(convertToSqlDate(dateText)));
            }

            if (!prescription.validate()) {
                JOptionPane.showMessageDialog(addWindow,
                    "Validation failed! Please check required fields:\n" +
                    "- Patient ID (8 chars)\n" +
                    "- Doctor ID\n" +
                    "- Medication Name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowsInserted = DatabaseManager.insert(connection, prescription);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(addWindow,
                    "Prescription added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearPrescriptionForm();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(addWindow,
                "Invalid date format. Use MM/DD/YYYY",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(addWindow,
                "Error inserting prescription: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clear patient form fields
     */
    private void clearPatientForm() {
        patientIdField.setText("");
        fNameField.setText("");
        mInitialField.setText("");
        lNameField.setText("");
        ssnField.setText("");
        sexField.setText("");
        conditionField.setText("");
        curPhoneField.setText("");
        curAddressField.setText("");
        curCityField.setText("");
        curStateField.setText("");
        curZipField.setText("");
        perPhoneField.setText("");
        perAddressField.setText("");
        perCityField.setText("");
        perStateField.setText("");
        perZipField.setText("");
        bDateField.setText("");
        priDocField.setText("");
        secDocField.setText("");
    }

    /**
     * Clear doctor form fields
     */
    private void clearDoctorForm() {
        doctorIdField.setText("");
        docFNameField.setText("");
        docMInitialField.setText("");
        docLNameField.setText("");
        docSsnField.setText("");
        docPhoneField.setText("");
        docContactField.setText("");
        docAddressField.setText("");
        docCityField.setText("");
        docStateField.setText("");
        docZipField.setText("");
        docBDateField.setText("");
    }

    /**
     * Clear department form fields
     */
    private void clearDepartmentForm() {
        deptCodeField.setText("");
        deptNameField.setText("");
        officeNumField.setText("");
        officePhoneField.setText("");
        headIdField.setText("");
    }

    /**
     * Clear procedure form fields
     */
    private void clearProcedureForm() {
        procNoField.setText("");
        procNameField.setText("");
        procDescField.setText("");
        procDurationField.setText("");
        procDepCodeField.setText("");
    }

    /**
     * Clear medication form fields
     */
    private void clearMedicationForm() {
        medNameField.setText("");
        medManufacturerField.setText("");
        medDescField.setText("");
    }

    /**
     * Clear interaction form fields
     */
    private void clearInteractionForm() {
        intPatientIdField.setText("");
        intIdField.setText("");
        intDateField.setText("");
        intDescField.setText("");
    }

    /**
     * Clear procedure performed form fields
     */
    private void clearProcedurePerformedForm() {
        perfProcNoField.setText("");
        perfPatientIdField.setText("");
        perfDocIdField.setText("");
        perfDateTimeField.setText("");
        perfNotesField.setText("");
    }

    /**
     * Clear prescription form fields
     */
    private void clearPrescriptionForm() {
        presPatientIdField.setText("");
        presDocIdField.setText("");
        presMedNameField.setText("");
        presDateField.setText("");
    }

    /**
     * Display search form for Patient
     */
    private void searchPatientForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Patient ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Patient ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        searchPatientIdField = new JTextField(8);
        forms.add(searchPatientIdField, constraints);
        row++;

        // Patient Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Name (First or Last):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        searchPatientNameField = new JTextField(20);
        forms.add(searchPatientNameField, constraints);

        currentSearchFormType = "patient";

        forms.revalidate();
        forms.repaint();
    }

    /**
     * Display search form for Doctor
     */
    private void searchDoctorForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Doctor ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Doctor ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        searchDoctorIdField = new JTextField(8);
        forms.add(searchDoctorIdField, constraints);
        row++;

        // Doctor Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Name (First or Last):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        searchDoctorNameField = new JTextField(20);
        forms.add(searchDoctorNameField, constraints);

        currentSearchFormType = "doctor";

        forms.revalidate();
        forms.repaint();
    }

    /**
     * Display search form for Department
     */
    private void searchDepartmentForm(Container forms) {
        forms.removeAll();
        forms.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        int row = 0;

        // Department Code
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Code:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        searchDeptCodeField = new JTextField(4);
        forms.add(searchDeptCodeField, constraints);
        row++;

        // Department Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Name:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        searchDeptNameField = new JTextField(30);
        forms.add(searchDeptNameField, constraints);

        currentSearchFormType = "department";

        forms.revalidate();
        forms.repaint();
    }

    /**
     * Handle search form submission
     */
    private void handleSearchSubmit() {
        if (currentSearchFormType == null) {
            JOptionPane.showMessageDialog(searchWindow,
                "Please select a search type first.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (currentSearchFormType) {
            case "patient" -> performPatientSearch();
            case "doctor" -> performDoctorSearch();
            case "department" -> performDepartmentSearch();
            default -> JOptionPane.showMessageDialog(searchWindow,
                    "Unknown search type: " + currentSearchFormType,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Perform patient search
     */
    private void performPatientSearch() {
        try {
            String patientId = searchPatientIdField.getText().trim();
            String name = searchPatientNameField.getText().trim();

            

            if (patientId.isEmpty() && name.isEmpty()) {
                JOptionPane.showMessageDialog(searchWindow,
                    "Please enter at least one search criterion",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use DatabaseManager to execute the search
            java.sql.ResultSet rs = DatabaseManager.searchPatient(connection, patientId, name);

            StringBuilder results = new StringBuilder("Search Results:\n\n");
            int count = 0;

            while (rs.next()) {
                count++;
                
                results.append("Patient ID: ").append("P").append(rs.getString("patientID")).append("\n");
                results.append("Name: ").append(rs.getString("fName")).append(" ");
                if (rs.getString("mInitial") != null) {
                    results.append(rs.getString("mInitial")).append(". ");
                }
                results.append(rs.getString("lName")).append("\n");
                results.append("SSN: ").append(rs.getString("ssn")).append("\n");
                results.append("Phone: ").append(rs.getString("curPhoneNo")).append("\n");
                results.append("Sex: ").append(rs.getString("sex")).append("\n");
                results.append("Condition: ").append(rs.getString("condition")).append("\n");
                results.append("Primary Doctor: ").append(rs.getString("priDoc")).append("\n");
                results.append("----------------------------------------\n");
            }

           

            if (count == 0) {
                results.append("No patients found matching the criteria.");
            } else {
                results.insert(0, "Found " + count + " patient(s)\n\n");
            }

            mainTextArea.setText(results.toString());
            
            rs.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(searchWindow,
                "Error searching patients: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Perform doctor search
     */
    private void performDoctorSearch() {
        try {
            String doctorId = searchDoctorIdField.getText().trim();
            String name = searchDoctorNameField.getText().trim();

            if (doctorId.isEmpty() && name.isEmpty()) {
                JOptionPane.showMessageDialog(searchWindow,
                    "Please enter at least one search criterion",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use DatabaseManager to execute the search
            java.sql.ResultSet rs = DatabaseManager.searchDoctor(connection, doctorId, name);

            StringBuilder results = new StringBuilder("Search Results:\n\n");
            int count = 0;

            while (rs.next()) {
                count++;
                results.append("Doctor ID: ").append("D").append(rs.getString("doctor_id")).append("\n");
                results.append("Name: ").append(rs.getString("first_name")).append(" ");
                if (rs.getString("mInitial") != null) {
                    results.append(rs.getString("mInitial")).append(". ");
                }
                results.append(rs.getString("last_name")).append("\n");
                results.append("SSN: ").append(rs.getString("ssn")).append("\n");
                results.append("Phone: ").append(rs.getString("phone")).append("\n");
                results.append("Contact: ").append(rs.getString("contactNo")).append("\n");
                results.append("Address: ").append(rs.getString("addrs")).append(", ");
                results.append(rs.getString("city")).append(", ");
                results.append(rs.getString("dState")).append(" ");
                results.append(rs.getString("zip")).append("\n");
                results.append("----------------------------------------\n");
            }

            if (count == 0) {
                results.append("No doctors found matching the criteria.");
            } else {
                results.insert(0, "Found " + count + " doctor(s)\n\n");
            }

            mainTextArea.setText(results.toString());
            rs.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(searchWindow,
                "Error searching doctors: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Perform department search
     */
    private void performDepartmentSearch() {
        try {
            String deptCode = searchDeptCodeField.getText().trim();
            String deptName = searchDeptNameField.getText().trim();

            if (deptCode.isEmpty() && deptName.isEmpty()) {
                JOptionPane.showMessageDialog(searchWindow,
                    "Please enter at least one search criterion",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use DatabaseManager to execute the search
            java.sql.ResultSet rs = DatabaseManager.searchDepartment(connection, deptCode, deptName);

            StringBuilder results = new StringBuilder("Search Results:\n\n");
            int count = 0;

            while (rs.next()) {
                count++;
                results.append("Department Code: ").append(rs.getString("dept_code")).append("\n");
                results.append("Department Name: ").append(rs.getString("dept_name")).append("\n");
                results.append("Office Number: ").append(rs.getString("office_number")).append("\n");
                results.append("Office Phone: ").append(rs.getString("office_phone")).append("\n");
                results.append("Head ID: ").append(rs.getString("headID")).append("\n");
                results.append("----------------------------------------\n");
            }

            if (count == 0) {
                results.append("No departments found matching the criteria.");
            } else {
                results.insert(0, "Found " + count + " department(s)\n\n");
            }

            mainTextArea.setText(results.toString());
            rs.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(searchWindow,
                "Error searching departments: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openReportsWindow() {
        // Check if window already exists and is visible
        if (reportsWindow != null && reportsWindow.isVisible()) {
            reportsWindow.toFront();
            return;
        }

        // Create new window
        reportsWindow = new JFrame("Reports");
        reportsWindow.setSize(900, 700);
        reportsWindow.setLocationRelativeTo(mainWindow);
        reportsWindow.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15, 15, 15, 15);

        // Report Selection Container (Top)
        Container reportTypeContainer = new Container();
        reportTypeContainer.setLayout(new FlowLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        reportsWindow.add(reportTypeContainer, constraints);

        // Input Container (Middle)
        Container inputContainer = new Container();
        inputContainer.setLayout(new GridBagLayout());

        constraints.gridy = 1;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        reportsWindow.add(inputContainer, constraints);

        // Report Display Area (Bottom - Takes most space)
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportTextArea.setText("Select a report type above and enter required information.");

        constraints.gridy = 2;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        reportsWindow.add(reportTextArea, constraints);

        // Report type buttons
        JButton patientRecordButton = new JButton("Patient Health Record");
        patientRecordButton.addActionListener(e -> showPatientHealthRecordForm(inputContainer, reportTextArea));
        reportTypeContainer.add(patientRecordButton);

        JButton procByDeptButton = new JButton("Procedures by Department");
        procByDeptButton.addActionListener(e -> showProceduresByDepartmentForm(inputContainer, reportTextArea));
        reportTypeContainer.add(procByDeptButton);

        JButton procByDocButton = new JButton("Procedures by Doctor");
        procByDocButton.addActionListener(e -> showProceduresByDoctorForm(inputContainer, reportTextArea));
        reportTypeContainer.add(procByDocButton);

        // Load patient health record form by default
        showPatientHealthRecordForm(inputContainer, reportTextArea);

        reportsWindow.setVisible(true);
    }

    private void showPatientHealthRecordForm(Container inputContainer, JTextArea reportArea) {
        inputContainer.removeAll();
        inputContainer.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        inputContainer.add(new JLabel("Patient ID:"), constraints);

        constraints.gridx = 1;
        JTextField patientIdField = new JTextField(15);
        inputContainer.add(patientIdField, constraints);

        constraints.gridx = 2;
        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(e -> {
            String patientId = patientIdField.getText().trim();
            if (patientId.isEmpty()) {
                JOptionPane.showMessageDialog(reportsWindow,
                    "Please enter a Patient ID",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String report = DatabaseManager.generatePatientHealthRecord(connection, patientId);
                reportArea.setText(report);
                reportArea.setCaretPosition(0);  // Scroll to top
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(reportsWindow,
                    "Error generating report: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        inputContainer.add(generateButton, constraints);

        inputContainer.revalidate();
        inputContainer.repaint();
        reportArea.setText("Enter a Patient ID and click 'Generate Report' to view complete health record.");
    }

    private void showProceduresByDepartmentForm(Container inputContainer, JTextArea reportArea) {
        inputContainer.removeAll();
        inputContainer.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        inputContainer.add(new JLabel("Department Code or Name:"), constraints);

        constraints.gridx = 1;
        JTextField deptSearchField = new JTextField(15);
        inputContainer.add(deptSearchField, constraints);

        constraints.gridx = 2;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchTerm = deptSearchField.getText().trim();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(reportsWindow,
                    "Please enter a department code or name",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                java.sql.ResultSet rs = DatabaseManager.searchProceduresByDepartment(connection, searchTerm);
                StringBuilder report = new StringBuilder();
                report.append("============ PROCEDURES BY DEPARTMENT ============\n");
                report.append("Search term: ").append(searchTerm).append("\n\n");

                int count = 0;
                while (rs.next()) {
                    count++;
                    report.append("• Procedure: ").append(rs.getString("procedure_name"))
                          .append(" (").append(rs.getString("procedure_no")).append(")\n");

                    String desc = rs.getString("description");
                    if (desc != null && !desc.isEmpty()) {
                        report.append("  Description: ").append(desc).append("\n");
                    }

                    int duration = rs.getInt("duration_minutes");
                    if (duration > 0) {
                        report.append("  Duration: ").append(duration).append(" minutes\n");
                    }
                    report.append("\n");
                }

                if (count == 0) {
                    report.append("No procedures found for this department.\n");
                } else {
                    report.insert(report.indexOf("\n\n") + 2, "Found " + count + " procedure(s)\n\n");
                }

                reportArea.setText(report.toString());
                reportArea.setCaretPosition(0);
                rs.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(reportsWindow,
                    "Error searching procedures: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        inputContainer.add(searchButton, constraints);

        inputContainer.revalidate();
        inputContainer.repaint();
        reportArea.setText("Enter a department code or name and click 'Search' to view procedures offered by that department.");
    }

    private void showProceduresByDoctorForm(Container inputContainer, JTextArea reportArea) {
        inputContainer.removeAll();
        inputContainer.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        inputContainer.add(new JLabel("Doctor ID:"), constraints);

        constraints.gridx = 1;
        JTextField doctorIdField = new JTextField(15);
        inputContainer.add(doctorIdField, constraints);

        constraints.gridx = 2;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String doctorId = doctorIdField.getText().trim();
            if (doctorId.isEmpty()) {
                JOptionPane.showMessageDialog(reportsWindow,
                    "Please enter a Doctor ID",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                java.sql.ResultSet rs = DatabaseManager.searchProceduresByDoctor(connection, doctorId);
                StringBuilder report = new StringBuilder();
                report.append("============ PROCEDURES BY DOCTOR ============\n");
                report.append("Doctor ID: D").append(doctorId).append("\n\n");

                int count = 0;
                while (rs.next()) {
                    count++;
                    report.append("• Procedure: ").append(rs.getString("procedure_name"))
                          .append(" (").append(rs.getString("procedure_no")).append(")\n");

                    java.sql.Timestamp performedAt = rs.getTimestamp("performed_at");
                    if (performedAt != null) {
                        report.append("  Performed: ").append(dateTimeFormat.format(performedAt)).append("\n");
                    }

                    String patientName = rs.getString("patient_name");
                    if (patientName != null) {
                        report.append("  Patient: ").append(patientName).append("\n");
                    }

                    String notes = rs.getString("notes");
                    if (notes != null && !notes.isEmpty()) {
                        report.append("  Notes: ").append(notes).append("\n");
                    }
                    report.append("\n");
                }

                if (count == 0) {
                    report.append("No procedures found for this doctor.\n");
                } else {
                    report.insert(report.indexOf("\n\n") + 2, "Found " + count + " procedure(s)\n\n");
                }

                reportArea.setText(report.toString());
                reportArea.setCaretPosition(0);
                rs.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(reportsWindow,
                    "Error searching procedures: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        inputContainer.add(searchButton, constraints);

        inputContainer.revalidate();
        inputContainer.repaint();
        reportArea.setText("Enter a Doctor ID and click 'Search' to view all procedures performed by that doctor.");
    }
}
