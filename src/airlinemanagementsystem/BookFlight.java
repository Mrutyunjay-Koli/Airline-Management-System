package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.*;

public class BookFlight extends JFrame implements ActionListener{
    
    JTextField tfaadhar;
    JLabel tfname, tfnationailty, tfaddress, labelgender, labelfname, labelfcode, lblOperatingDay, lbltimings;
    JButton bookflight, fetchUser, flight, addAnotherBooking;
    Choice source, destination;
    JDateChooser dcdate;
    String sessionId;
    
    public BookFlight() {
    setTitle("Book Flight");
    getContentPane().setBackground(Color.WHITE);
    setLayout(null);

    sessionId = "S" + System.currentTimeMillis();

    JLabel heading = new JLabel("Book Flight");
    heading.setBounds(420, 20, 500, 35);
    heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
    heading.setForeground(Color.BLUE);
    add(heading);

    JLabel lblaadhar = new JLabel("Aadhar Number");
    lblaadhar.setBounds(60, 80, 150, 25);
    lblaadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lblaadhar);

    tfaadhar = new JTextField();
    tfaadhar.setBounds(220, 80, 150, 25);
    add(tfaadhar);

    fetchUser = new JButton("Fetch User");
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
        
    JLabel lblnationationailty = new JLabel("Nationality");
    lblnationationailty.setBounds(60, 180, 150, 25);
    lblnationationailty.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lblnationationailty);
        
    tfnationailty = new JLabel();
    tfnationailty.setBounds(220, 180, 150, 25);
    add(tfnationailty);
        
        
    JLabel lbladdress = new JLabel("Address");
    lbladdress.setBounds(60, 230, 150, 25);
    lbladdress.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lbladdress);
        
    tfaddress = new JLabel();
    tfaddress.setBounds(220, 230, 400, 25);
    add(tfaddress);
        
    JLabel lblgender = new JLabel("Gender");
    lblgender.setBounds(60, 280, 150, 25);
    lblgender.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lblgender);
        
    labelgender = new JLabel();
    labelgender.setBounds(220, 280, 150, 25);
    add(labelgender);

    JLabel lblsource = new JLabel("Source");
    lblsource.setBounds(60, 330, 150, 25);
    lblsource.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lblsource);

    source = new Choice();
    source.setBounds(220, 330, 150, 25);
    add(source);

    JLabel lbldest = new JLabel("Destination");
    lbldest.setBounds(60, 380, 150, 25);
    lbldest.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lbldest);

    destination = new Choice();
    destination.setBounds(220, 380, 150, 25);
    add(destination);

    try {
        Conn c = new Conn();
        String query = "SELECT DISTINCT source, destination FROM flight";
        ResultSet rs = c.s.executeQuery(query);

        while (rs.next()) {
            source.add(rs.getString("source"));
            destination.add(rs.getString("destination"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    flight = new JButton("Fetch Flights");
    flight.setBackground(Color.BLACK);
    flight.setForeground(Color.WHITE);
    flight.setBounds(380, 378, 120, 25);
    flight.addActionListener(this);
    add(flight);

    JLabel lblfname = new JLabel("Flight Name");
    lblfname.setBounds(60, 430, 150, 25);
    lblfname.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lblfname);

    labelfname = new JLabel();
    labelfname.setBounds(220, 430, 150, 25);
    add(labelfname);

    JLabel OperatingDay = new JLabel("Operating Day");
    OperatingDay.setBounds(400, 430, 150, 25);
    OperatingDay.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(OperatingDay);

    lblOperatingDay = new JLabel();
    lblOperatingDay.setBounds(550, 430, 150, 25);
    add(lblOperatingDay);

    JLabel lblfcode = new JLabel("Flight Code");
    lblfcode.setBounds(60, 480, 150, 25);
    lblfcode.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lblfcode);

    labelfcode = new JLabel();
    labelfcode.setBounds(220, 480, 150, 25);
    add(labelfcode);

    JLabel lbltiming = new JLabel("Timings");
    lbltiming.setBounds(400, 480, 150, 25);
    lbltiming.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lbltiming);

    lbltimings = new JLabel();
    lbltimings.setBounds(550, 480, 200, 25);
    add(lbltimings);

    JLabel lbldate = new JLabel("Date of Travel");
    lbldate.setBounds(60, 530, 150, 25);
    lbldate.setFont(new Font("Tahoma", Font.PLAIN, 16));
    add(lbldate);

    dcdate = new JDateChooser();
    Calendar calendar = Calendar.getInstance();
    dcdate.setMinSelectableDate(calendar.getTime());
    dcdate.setBounds(220, 530, 150, 25);
    add(dcdate);

    // Move Image Higher
    ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/details.jpg"));
    Image i2 = i1.getImage().getScaledInstance(450, 300, Image.SCALE_DEFAULT);
    ImageIcon image = new ImageIcon(i2);
    JLabel lblimage = new JLabel(image);
    lblimage.setBounds(550, 75, 450, 300); // Adjusted position
    add(lblimage);

    bookflight = new JButton("Book Flight");
    bookflight.setBackground(Color.BLACK);
    bookflight.setForeground(Color.WHITE);
    bookflight.setBounds(220, 580, 150, 25);
    bookflight.addActionListener(this);
    add(bookflight);

    addAnotherBooking = new JButton("Add Another Booking");
    addAnotherBooking.setBackground(Color.BLACK);
    addAnotherBooking.setForeground(Color.WHITE);
    addAnotherBooking.setBounds(380, 580, 180, 25);
    addAnotherBooking.addActionListener(this);
    add(addAnotherBooking);

    setSize(1100, 700);
    setLocation(125, 10);
    setVisible(true);
}

    
   public void actionPerformed(ActionEvent ae){
    if (ae.getSource() == fetchUser){
        String aadhar = tfaadhar.getText();
        String gender = null;

        try {
            Conn conn = new Conn();
            String query = "select * from passenger where aadhar ='"+aadhar+"'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()){
                tfname.setText(rs.getString("name"));
                tfnationailty.setText(rs.getString("nationailty"));
                tfaddress.setText(rs.getString("address"));
                labelgender.setText(rs.getString("gender"));
            } else {
                JOptionPane.showMessageDialog(null, "Please enter correct aadhar");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    } else if (ae.getSource() == flight){
        String src = source.getSelectedItem();
        String dest = destination.getSelectedItem();

        try {
            Conn conn = new Conn();
            String query = "select * from flight where source = '"+src+"' and destination = '"+dest+"'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()){
                labelfname.setText(rs.getString("Flight_Name"));
                labelfcode.setText(rs.getString("Flight_Code"));
                lblOperatingDay.setText(rs.getString("Frequency")); // Fetch Operating Days
                lbltimings.setText(rs.getString("Timings")); // Fetch Timings

            } else {
                JOptionPane.showMessageDialog(null, "No Flights Found");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    } else { // Booking Flight
        Random random = new Random();
        
        // Fetching Values
        String aadhar = tfaadhar.getText();
        String name = tfname.getText(); 
        String nationality = tfnationailty.getText(); 
        String flightname = labelfname.getText();
        String flightcode = labelfcode.getText();
        String src = source.getSelectedItem();
        String dest = destination.getSelectedItem();
        String ddate = ((JTextField) dcdate.getDateEditor().getUiComponent()).getText();
        String operatingDay = lblOperatingDay.getText();
        String timing = lbltimings.getText();
        
        // **Validation: Ensure all fields are filled**
        if (aadhar.isEmpty() || name.isEmpty() || nationality.isEmpty() || flightname.isEmpty() ||
            flightcode.isEmpty() || src.isEmpty() || dest.isEmpty() || ddate.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled before booking a flight.");
            return; // Stop execution if any field is empty
        }
        
             // **Validation: Ensure date is not in the past**
        try {
        // Get the selected date from JDateChooser
        java.util.Date selectedDate = dcdate.getDate();

        if (selectedDate == null) {
        JOptionPane.showMessageDialog(null, "Please select a date.");
        return;
        }

        // Get today's date without time
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Convert selected date to Calendar and remove time part
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(selectedDate);
        selectedCal.set(Calendar.HOUR_OF_DAY, 0);
        selectedCal.set(Calendar.MINUTE, 0);
        selectedCal.set(Calendar.SECOND, 0);
        selectedCal.set(Calendar.MILLISECOND, 0);

        // Compare dates
        if (selectedCal.before(today)) {
        JOptionPane.showMessageDialog(null, "Invalid Date! You cannot select a past date.");
        return;
    }

}       catch (Exception e) {
        JOptionPane.showMessageDialog(null, "An error occurred while selecting the date.");
        return;
}


        try {
            Conn conn = new Conn();
            // Generate PNR and Ticket Number
            String pnr = "PNR-" + String.format("%05d", random.nextInt(100000));
            String ticketNumber = "TIC-" + random.nextInt(10000);

            // Insert data into reservations table
            String query = "INSERT INTO reservations VALUES('" + pnr + "', '" + ticketNumber + "', '" + aadhar + "', '" + name + "', '" + nationality + "', '" + flightname + "', '" + flightcode + "', '" + src + "', '" + dest + "', '" + ddate + "', '" + sessionId +"', '" + operatingDay + "', '" + timing + "')";
            conn.s.executeUpdate(query);

//            Show only the PNR number to the user
            JOptionPane.showMessageDialog(null, "Flight Booked Successfully\nYour PNR Number: " + pnr);
            
             int choice = JOptionPane.showConfirmDialog(null, "Do you want to add another passenger?", "Multiple Bookings", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    clearFields();
                } else {
                    setVisible(false);
                    new SeatSelection(sessionId);
                }

//            setVisible(false);
//            new SeatSelection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   if (ae.getSource() == addAnotherBooking) {
            clearFields();
        }
}
   
    private void clearFields() {
        tfaadhar.setText("");
        tfname.setText("");
        tfnationailty.setText("");
        tfaddress.setText("");
        labelgender.setText("");
        labelfname.setText("");
        labelfcode.setText("");
        dcdate.setDate(null);
    }
    

    
    public static void main(String[] args) {
        new BookFlight();
    }
}