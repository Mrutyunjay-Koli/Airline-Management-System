package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import net.proteanit.sql.DbUtils;

public class JourneyDetails extends JFrame implements ActionListener {
    
    JTable table;
    JTextField inputField;
    JButton show;
    JRadioButton searchByPNR, searchBySession;
    
    public JourneyDetails() {
        
        setTitle("Journey Details");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel lblSearch = new JLabel("Search By:");
        lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSearch.setBounds(50, 20, 100, 25);
        add(lblSearch);
        
        searchByPNR = new JRadioButton("PNR Number", true);
        searchByPNR.setBounds(160, 20, 120, 25);
        searchByPNR.setBackground(Color.WHITE);
        add(searchByPNR);
        
        searchBySession = new JRadioButton("Session ID");
        searchBySession.setBounds(290, 20, 150, 25);
        searchBySession.setBackground(Color.WHITE);
        add(searchBySession);
        
        ButtonGroup group = new ButtonGroup();
        group.add(searchByPNR);
        group.add(searchBySession);
        
        JLabel lblInput = new JLabel("Enter Value:");
        lblInput.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblInput.setBounds(50, 60, 100, 25);
        add(lblInput);
        
        inputField = new JTextField();
        inputField.setBounds(160, 60, 120, 25);
        add(inputField);
        
        show = new JButton("Show Details");
        show.setBackground(Color.BLACK);
        show.setForeground(Color.WHITE);
        show.setBounds(290, 60, 120, 25);
        show.addActionListener(this);
        add(show);
        
        table = new JTable();
        
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 1240, 200);
        jsp.setBackground(Color.WHITE);
        add(jsp);
        
        setSize(1250,500);
        setLocation(30,100);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            Conn conn = new Conn();
            String query = "";
            String value = inputField.getText().trim();
            
            // Input Validation
            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid value.");
                return;
            }

            if (searchByPNR.isSelected()) {
                query = "SELECT r.PNR, r.TICKET, r.Aadhar_Number, r.Name, r.Nationailty, r.Flight_Name, r.Flight_Code, " +
                        "r.Source, r.Destination, s.Seat_Number, r.Journey_Date, f.Frequency, f.Timings " +
                        "FROM reservations r " +
                        "JOIN seat_selection s ON r.PNR = s.PNR " +
                        "JOIN flight f ON r.Flight_Code = f.Flight_Code " +
                        "WHERE r.PNR = '" + value + "'";
            } else if (searchBySession.isSelected()) {
                query = "SELECT r.PNR, r.TICKET, r.Aadhar_Number, r.Name, r.Nationailty, r.Flight_Name, r.Flight_Code, " +
                        "r.Source, r.Destination, s.Seat_Number, r.Journey_Date, f.Frequency, f.Timings " +
                        "FROM reservations r " +
                        "JOIN seat_selection s ON r.PNR = s.PNR " +
                        "JOIN flight f ON r.Flight_Code = f.Flight_Code " +
                        "WHERE r.Session_ID = '" + value + "'";
            }
            
            ResultSet rs = conn.s.executeQuery(query);
            
            if (!rs.isBeforeFirst()) {  // Check if ResultSet is empty
                JOptionPane.showMessageDialog(null, "No Information Found");
                return;
            }
            
            table.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new JourneyDetails();
    }
}
