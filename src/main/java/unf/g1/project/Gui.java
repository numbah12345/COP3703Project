package unf.g1.project;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import oracle.sql.ConcreteProxyUtil;

public class Gui {
    JFrame mainWindow;
    JFrame addWindow;  // Track the add window
    JFrame searchWindow;
    JTextArea mainTextArea;

    public Gui() {
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
        
        searchWindow.setSize(600,400);
        searchWindow.setLocationRelativeTo(mainWindow);  // Center on main window
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
        addWindow.setSize(600, 800);
        
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
        controlContainer.add(new JButton("Cancel"), constraints1);
        controlContainer.add(new JButton("Submit"), constraints1);

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
        forms.add(new JTextField(8), constraints);
        row++;

        // First Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("First Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(15), constraints);
        row++;

        // Middle Initial
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Middle Initial:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(1), constraints);
        row++;

        // Last Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Last Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(20), constraints);
        row++;

        // SSN
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("SSN* (9 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(9), constraints);
        row++;

        // Sex
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Sex* (F/M/O):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(1), constraints);
        row++;

        // Condition
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Condition* (F/C/S):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(1), constraints);
        row++;

        // Current Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current Phone* (10 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Current City
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current City:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(15), constraints);
        row++;

        // Current State
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current State:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(15), constraints);
        row++;

        // Current Zip
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Current Zip:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(5), constraints);
        row++;

        // Permanent Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent Phone* (10 digits):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Permanent City
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent City:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(15), constraints);
        row++;

        // Permanent State
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent State:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(15), constraints);
        row++;

        // Permanent Zip
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Permanent Zip:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(5), constraints);

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
        forms.add(new JTextField(10), constraints);
        row++;

        // First Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("First Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(15), constraints);
        row++;

        // Middle Initial
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Middle Initial:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(1), constraints);
        row++;

        // Last Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Last Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(20), constraints);
        row++;

        // Department Code
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Code*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Phone Number:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Specialization
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Specialization:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(30), constraints);

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
        forms.add(new JTextField(10), constraints);
        row++;

        // Department Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(30), constraints);
        row++;

        // Location
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Location:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(30), constraints);
        row++;

        // Manager ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Manager ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Phone Number
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Phone Number:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);

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
        forms.add(new JLabel("Procedure Number*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Procedure Name
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Procedure Name*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(30), constraints);
        row++;

        // Department Code
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Department Code*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Duration (minutes)
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Duration (minutes):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(5), constraints);
        row++;

        // Cost
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Cost:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Description
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Description:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(50), constraints);

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
        forms.add(new JTextField(30), constraints);
        row++;

        // Medication Type
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Type:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(20), constraints);
        row++;

        // Dosage
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Dosage:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(20), constraints);
        row++;

        // Manufacturer
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Manufacturer:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(30), constraints);
        row++;

        // Side Effects
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Side Effects:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(50), constraints);
        row++;

        // Description
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Description:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(50), constraints);

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

        // Interaction ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Interaction ID*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Patient ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Patient ID*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(8), constraints);
        row++;

        // Doctor ID
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Doctor ID:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Interaction Date
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Date (YYYY-MM-DD)*:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(10), constraints);
        row++;

        // Interaction Time
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Time (HH:MM):"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(5), constraints);
        row++;

        // Type
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Type:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(20), constraints);
        row++;

        // Description
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.3;
        forms.add(new JLabel("Description:"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.7;
        forms.add(new JTextField(50), constraints);

        forms.revalidate();
        forms.repaint();
    }
}
