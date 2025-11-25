//This is the main entry point of the project

/*
Should allow us to:
    add patient, department, procedures, doctors, medication, and interaction records.
    Add information about a procedure done on a patient.
    Add medication/s prescribed to a patient.
    Given a patient ID, generate their complete health record. The health record should include patient information (name, current address and current phone, primary doctor's name, primary doctor's department), (a) all the procedures they had, (b) all the interactions they had with the hospital (with dates and the details), and (c) all the medications prescribed to them (with dates and details).
    Given a department name or code find the procedures offered. 
    Given a doctor's ID list all the procedures they have done.
 */
package unf.g1.project;

/**
 *
 * @author jakez
 */

import java.awt.geom.QuadCurve2D;
import java.sql.Connection;
import java.sql.DriverManager;

import unf.g1.project.QueryBuilder;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        System.out.println("Hello COP3703!");

        Connection connection =null;

        connection = connectToDB();
        if (connection != null) {
            System.out.println("Connection success!");
        }
        else {
            System.out.println("Connection failed!");
        }

        System.out.println("\t\t\tDataBase Manager\t\t\t");
        System.out.println("Options:");
        System.out.println("1. Write Data\n2. Read Data");

        Scanner scanner = new Scanner(System.in);

        int menuInput = scanner.nextInt();

        switch (menuInput) {
            case 1 -> {
                System.out.println("Would you like to add, change, or remove data?");
                System.out.println("1. Add data\n2. Change data\n3. Remove data");
                int writeOption = scanner.nextInt();

                switch (writeOption) {
                    case 1 -> {
                        System.out.println("What would you like to add:");
                        System.out.println("1. Patient\n2. Department\n3. Procedure\n4. Doctor\n5. Medication\n6. Interaction");
                        int addOption = scanner.nextInt();

                        switch (addOption) {
                            case 1 -> System.out.println("Adding Patient...");
                            case 2 -> System.out.println("Adding Department...");
                            case 3 -> System.out.println("Adding Procedure...");
                            case 4 -> System.out.println("Adding Doctor...");
                            case 5 -> System.out.println("Adding Medication...");
                            case 6 -> System.out.println("Adding Interaction...");
                            default -> System.out.println("Invalid option");
                        }
                    }
                    case 2 -> {
                        System.out.println("Changing data...");
                    }
                    case 3 -> {
                        System.out.println("Removing data...");
                    }
                    default -> System.out.println("Invalid option");
                }
            }
            case 2 -> {
                System.out.println("What would you like to read?");
                System.out.println("1. Patient\n2. Department\n3. Doctor");
                int readOption = scanner.nextInt();

                switch (readOption) {
                    case 1 -> {
                        System.out.println("Enter patient ID: ");
                    }
                    case 2 -> {
                        System.out.println("Enter department name or code: ");
                    }
                    case 3 -> {
                        System.out.println("Enter doctor ID: ");
                    }
                    default -> System.out.println("Invalid option");
                }
            }
            default -> System.out.println("Invalid option");
        }
    }


    static Connection connectToDB() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@cisvm-oracle.unfcsd.unf.edu:1521:orcl", 
                "G01", 
                "r9Qi0oVD"
            );

            System.out.println("it worked?");
            return conn;
        } 
        catch (Exception e) {
            System.out.println("\n\n\n\n" + e.getMessage()+ "\n\n\n\n");
            return null;
        }
        
    }
}


