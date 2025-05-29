package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class Cancel extends JFrame implements ActionListener{
    
    JTextField tfpnr;
    JLabel tfname, cancellationno, lblfcode, lbldateoftrvel;
    JButton fetchUser, flight;
    
    public Cancel(){
        
        setTitle("Cancellation");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        Random random = new Random();
         

        JLabel heading = new JLabel("CANCELLATION");
        heading.setBounds(180, 20, 250, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        add(heading);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/cancel.jpg"));
        Image i2 = i1.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(470, 125, 250, 250);
        add(image);
        
        JLabel lblaadhar = new JLabel("PNR Number");
        lblaadhar.setBounds(60, 80, 150, 25);
        lblaadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblaadhar);
        
        tfpnr= new JTextField();
        tfpnr.setBounds(220, 80, 150, 25);
        add(tfpnr);
        
        fetchUser  = new JButton("Show Details");
        fetchUser.setBackground(Color.BLACK);
        fetchUser.setForeground(Color.WHITE);
        fetchUser.setBounds(380, 80, 120, 25);
        fetchUser.addActionListener(this);
        add(fetchUser);
        
        JLabel lblname = new JLabel("Name");
        lblname.setBounds(60, 130, 150, 25);
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblname);
        
        tfname = new JLabel();
        tfname.setBounds(220, 130, 150, 25);
        add(tfname);
        
        JLabel lblnationationailty = new JLabel("Cancellation No");
        lblnationationailty.setBounds(60, 180, 150, 25);
        lblnationationailty.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblnationationailty);
        
        cancellationno = new JLabel(""+ random.nextInt(100000));
        cancellationno.setBounds(220, 180, 150, 25);
        add(cancellationno);
        
        
        JLabel lbladdress = new JLabel("Flight Code");
        lbladdress.setBounds(60, 230, 150, 25);
        lbladdress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbladdress);
        
        lblfcode = new JLabel();
        lblfcode.setBounds(220, 230, 150, 25);
        add(lblfcode);
        
        JLabel lblgender = new JLabel("Date");
        lblgender.setBounds(60, 280, 150, 25);
        lblgender.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblgender);
        
        lbldateoftrvel = new JLabel();
        lbldateoftrvel.setBounds(220, 280, 150, 25);
        add(lbldateoftrvel);
        
        
        flight  = new JButton("Cancel");
        flight.setBackground(Color.BLACK);
        flight.setForeground(Color.WHITE);
        flight.setBounds(220, 348, 120, 25);
        flight.addActionListener(this);
        add(flight);
        
        setSize(800, 450);
        setLocation(300, 100);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if (ae.getSource() == fetchUser){
            String pnr = tfpnr.getText();
        
            try {
            Conn conn = new Conn();
            
            String query = "select * from reservations where PNR ='"+pnr+"'";
            
            ResultSet rs = conn.s.executeQuery(query);
            
           if (rs.next()){
                tfname.setText(rs.getString("name"));
                lblfcode.setText(rs.getString("flight_code"));
                lbldateoftrvel.setText(rs.getString("journey_date"));
            }  else {
                JOptionPane.showMessageDialog(null, "Please enter correct PNR");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }      else {
            if (ae.getSource() == flight){
            String name = tfname.getText();
            String pnr = tfpnr.getText();
            String cancelno = cancellationno.getText();
            String fcode = lblfcode.getText();
            String date = lbldateoftrvel.getText();
            
            if (pnr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "PNR field cannot be empty. Please enter a valid PNR.");
            return; // exit the method without proceeding
        }

        
           try {
           Conn conn = new Conn();
    
            // Insert into cancel table
            String query = "INSERT INTO cancel (pnr, name, cancelno, fcode, ddate) " + "VALUES ('" + pnr + "', '" + name + "', '" + cancelno + "', '" + fcode + "', '" + date + "')";
            conn.s.executeUpdate(query);

            // Delete from reservations table
            conn.s.executeUpdate("DELETE FROM reservations WHERE PNR = '" + pnr + "'");

            // Delete from seat_selection table
            conn.s.executeUpdate("DELETE FROM seat_selection WHERE PNR = '" + pnr + "'");

            // Delete from payments table
            conn.s.executeUpdate("DELETE FROM payments WHERE PNR = '" + pnr + "'");

            JOptionPane.showMessageDialog(null, "Ticket cancelled successfully!\n70% of the amount of ticket will be refunded.\nPlease collect it from the office.");
            setVisible(false);
        }      
           catch (Exception e) {
           e.printStackTrace();
        }
    }        
        }                                      
}
    
    public static void main(String[] args) {
        new Cancel();
    }
}
