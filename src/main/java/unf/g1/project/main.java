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



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridBagLayoutInfo;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import unf.g1.project.QueryBuilder;

import java.util.Scanner;

import javax.naming.spi.DirStateFactory;
import javax.swing.text.JTextComponent;

import unf.g1.project.models.Patient;

public class main {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello COP3703!");

        Connection connection =null;

        connection = connectToDB();
        if (connection != null) {
            System.out.println("Connection success!");
        }
        else {
            System.out.println("Connection failed!");
        }


        
        Gui gui = new Gui();
        /*TODO:
        Create the rest of the models    
        Connect add gui to models 
        Create search gui 
        */

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


