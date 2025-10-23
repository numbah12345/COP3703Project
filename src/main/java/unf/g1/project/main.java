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

import java.sql.Connection;
import java.sql.DriverManager;


public class main {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@cisvm-oracle.unfcsd.unf.edu:1521:orcl", 
                "G01", 
                "r9Qi0oVD"
            );

            System.out.println("it worked?");
        } 
        catch (Exception e) {
            System.out.println("\n\n\n\n" + e.getMessage()+ "\n\n\n\n");
        }

   


        System.out.println("Hello COP3703!");
    }
}
