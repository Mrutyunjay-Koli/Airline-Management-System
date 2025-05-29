package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardingPass extends JFrame implements ActionListener{
    
    JTextField tfpnr;
    JLabel tfname, tfnationailty, tfsrc, lbldest, labelfname, labelfcode, labeldate, lblobday, lbltiming;
    JButton fetchUser, printButton;
    
    public BoardingPass(){
        
        setTitle("Boarding Pass");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
         
        JLabel heading = new JLabel("AIR INDIA");
        heading.setBounds(380, 10, 450, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        add(heading);
        
        JLabel subheading = new JLabel("Boarding Pass");
        subheading.setBounds(380, 50, 300, 30);
        subheading.setFont(new Font("Tahoma", Font.PLAIN, 24));
        subheading.setForeground(Color.BLUE);
        add(subheading);
        
        JLabel lblaadhar = new JLabel("PNR DETAILS");
        lblaadhar.setBounds(60, 100, 150, 25);
        lblaadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblaadhar);
        
        tfpnr = new JTextField();
        tfpnr.setBounds(220, 100, 150, 25);
        add(tfpnr);
        
        fetchUser  = new JButton("Enter");
        fetchUser.setBackground(Color.BLACK);
        fetchUser.setForeground(Color.WHITE);
        fetchUser.setBounds(380, 100, 120, 25);
        fetchUser.addActionListener(this);
        add(fetchUser);
        
        JLabel lblname = new JLabel("NAME");
        lblname.setBounds(60, 140, 150, 25);
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblname);
        
        tfname = new JLabel();
        tfname.setBounds(220, 140, 150, 25);
        add(tfname);
        
        JLabel lblnationationailty = new JLabel("NATIONALITY");
        lblnationationailty.setBounds(60, 180, 150, 25);
        lblnationationailty.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblnationationailty);
        
        tfnationailty = new JLabel();
        tfnationailty.setBounds(220, 180, 150, 25);
        add(tfnationailty);
        
        JLabel lbladdress = new JLabel("SOURCE");
        lbladdress.setBounds(60, 220, 150, 25);
        lbladdress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbladdress);
        
        tfsrc = new JLabel();
        tfsrc.setBounds(220, 220, 150, 25);
        add(tfsrc);
        
        JLabel lblgender = new JLabel("DESTINATION");
        lblgender.setBounds(380, 215, 150, 25);
        lblgender.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblgender);
        
        lbldest = new JLabel();
        lbldest.setBounds(540, 215, 150, 25);
        add(lbldest);
        
        JLabel lblfname = new JLabel("FLIGHT NAME");
        lblfname.setBounds(60, 260, 150, 25);
        lblfname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblfname);
        
        labelfname = new JLabel();
        labelfname.setBounds(220, 260, 150, 25);
        add(labelfname);
        
        JLabel lblfcode = new JLabel("FLIGHT CODE");
        lblfcode.setBounds(380, 260, 150, 25);
        lblfcode.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblfcode);
        
        labelfcode = new JLabel();
        labelfcode.setBounds(540, 260, 150, 25);
        add(labelfcode);
        
        JLabel lbldate = new JLabel("DATE");
        lbldate.setBounds(60, 300, 150, 25);
        lbldate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbldate);
        
        labeldate = new JLabel();
        labeldate.setBounds(220, 300, 150, 25);
        add(labeldate);
        
        // New Fields for Operating Days and Timing
        JLabel lblopday = new JLabel("OPERATING DAY");
        lblopday.setBounds(60, 340, 150, 25);
        lblopday.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblopday);
        
        lblobday = new JLabel();
        lblobday.setBounds(220, 340, 150, 25);
        add(lblobday);
        
        JLabel lbltime = new JLabel("TIMING");
        lbltime.setBounds(380, 340, 150, 25);
        lbltime.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbltime);
        
        lbltiming = new JLabel();
        lbltiming.setBounds(540, 340, 150, 25);
        add(lbltiming);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/airindia.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 230, Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(i2);
        JLabel lblimage = new JLabel(image);
        lblimage.setBounds(600, 0, 300, 300);
        add(lblimage);
        
        printButton = new JButton("Print");
        printButton.setBounds(380, 390, 120, 30);
        printButton.setBackground(Color.BLACK);
        printButton.setForeground(Color.WHITE);
        printButton.addActionListener(this);
        add(printButton);
        
        setSize(950, 500);
        setLocation(200, 90);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        String pnr = tfpnr.getText();
        try {
            Conn conn = new Conn();
            
            String query = "SELECT * FROM reservations WHERE PNR = '"+pnr+"'";
            
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()){
                tfname.setText(rs.getString("name"));
                tfnationailty.setText(rs.getString("nationailty"));
                tfsrc.setText(rs.getString("source"));
                lbldest.setText(rs.getString("destination"));
                labelfname.setText(rs.getString("flight_name"));
                labelfcode.setText(rs.getString("flight_code"));
                labeldate.setText(rs.getString("journey_date"));
                lblobday.setText(rs.getString("Operating_Day")); // Fetching Operating Days
                lbltiming.setText(rs.getString("Timing")); // Fetching Timing
            }  else {
                JOptionPane.showMessageDialog(null, "Please enter PNR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (ae.getSource() == printButton) {
            if (!tfname.getText().isEmpty() && !tfnationailty.getText().isEmpty() && !tfsrc.getText().isEmpty() &&
                !lbldest.getText().isEmpty() && !labelfname.getText().isEmpty() && !labelfcode.getText().isEmpty() &&
                !labeldate.getText().isEmpty() && !lblobday.getText().isEmpty() && !lbltiming.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Print Successful");
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled for Print");
            }
        }
    }
    
    public static void main(String[] args) {
        new BoardingPass();
    }
}
