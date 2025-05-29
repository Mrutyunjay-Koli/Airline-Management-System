package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class FlightInfo extends JFrame {
    
    public FlightInfo() {
        
        setTitle("Flight Information");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JTable table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevents table from being edited
            }
        };
        
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("SELECT * FROM flight");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 0, 1000, 500);
        add(jsp);
        
        setSize(1000, 500);
        setLocation(200, 100);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new FlightInfo();
    }
}
