package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class PaymentSystem extends JFrame implements ActionListener {
    private JLabel lblDetails, lblTotalPrice, lblPaymentMode, lblScanner, lblCash;
    private JTextField tfUpi;
    private JButton btnPay;
    private JComboBox<String> paymentModeDropdown;
    private String sessionId;
    private int totalPrice = 0;
    private ArrayList<String> prnList = new ArrayList<>();
    private ArrayList<String> seatList = new ArrayList<>();
    private ArrayList<String> ticketList = new ArrayList<>();

    public PaymentSystem(String sessionId) {
        this.sessionId = sessionId;
        setTitle("Payment System");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel heading = new JLabel("Payment Details");
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        heading.setBounds(200, 10, 200, 30);
        add(heading);

        lblDetails = new JLabel("Fetching booking details...");
        lblDetails.setBounds(50, 50, 500, 100);
        add(lblDetails);

        lblTotalPrice = new JLabel("Total Price: Calculating...");
        lblTotalPrice.setBounds(50, 160, 300, 25);
        add(lblTotalPrice);

        // Payment Mode Dropdown
        lblPaymentMode = new JLabel("Select Payment Mode:");
        lblPaymentMode.setBounds(50, 190, 200, 25);
        add(lblPaymentMode);

        String[] paymentOptions = {"Select Payment Mode", "UPI Payment", "Cash Payment"};
        paymentModeDropdown = new JComboBox<>(paymentOptions);
        paymentModeDropdown.setBounds(250, 190, 200, 25);
        paymentModeDropdown.addActionListener(this);
        add(paymentModeDropdown);

        // UPI Payment Field
        JLabel lblUpi = new JLabel("Enter UPI ID:");
        lblUpi.setBounds(50, 230, 200, 25);
        lblUpi.setVisible(false);
        add(lblUpi);

        tfUpi = new JTextField();
        tfUpi.setBounds(250, 230, 200, 25);
        tfUpi.setToolTipText("Enter UPI ID (e.g., xyz@upi)");
        tfUpi.setVisible(false);
        add(tfUpi);

        // QR Scanner Image
        lblScanner = new JLabel();
        lblScanner.setBounds(450, 220, 100, 100);
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/QR.png"));
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        lblScanner.setIcon(new ImageIcon(img));
        lblScanner.setVisible(false);
        add(lblScanner);

        // Cash Payment Label (No input needed)
        lblCash = new JLabel("Cash Payment Selected");
        lblCash.setBounds(250, 230, 200, 25);
        lblCash.setVisible(false);
        add(lblCash);

        btnPay = new JButton("Make Payment");
        btnPay.setBounds(200, 350, 200, 30);
        btnPay.addActionListener(this);
        add(btnPay);

        fetchBookingDetails();
        fetchTicketNumbers();

        setVisible(true);
    }

    private void fetchBookingDetails() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql:///airlinemanagementsystem", "root", "200421");
             PreparedStatement pst = conn.prepareStatement("SELECT PNR, Seat_Number FROM seat_selection WHERE PNR IN (SELECT PNR FROM reservations WHERE Session_ID = ?)")) {
            pst.setString(1, sessionId);
            ResultSet rs = pst.executeQuery();

            StringBuilder details = new StringBuilder("<html>");
            while (rs.next()) {
                String prn = rs.getString("PNR");
                String seat = rs.getString("Seat_Number");
                prnList.add(prn);
                seatList.add(seat);
                int price = getSeatPrice(seat);
                totalPrice += price;
                details.append("PNR: ").append(prn).append(" | Seat: ").append(seat).append(" | Price: ₹").append(price).append("<br>");
            }
            details.append("</html>");

            lblDetails.setText(details.toString());
            lblTotalPrice.setText("Total Price: ₹" + totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSeatPrice(String seat) {
        char seatClass = seat.charAt(0);
        return switch (seatClass) {
            case 'A', 'F' -> 6000;
            case 'B', 'E' -> 5000;
            case 'C', 'D' -> 4000;
            default -> 0;
        };
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == paymentModeDropdown) {
            String selectedMode = (String) paymentModeDropdown.getSelectedItem();
            
            if ("UPI Payment".equals(selectedMode)) {
                tfUpi.setVisible(true);
                lblScanner.setVisible(true);
                lblCash.setVisible(false);
            } 
            else if ("Cash Payment".equals(selectedMode)) {
                tfUpi.setVisible(false);
                lblScanner.setVisible(false);
                lblCash.setVisible(true);
            } 
            else {
                tfUpi.setVisible(false);
                lblScanner.setVisible(false);
                lblCash.setVisible(false);
            }
        }

        if (e.getSource() == btnPay) {
            String selectedMode = (String) paymentModeDropdown.getSelectedItem();
            String paymentDetails = "";

            if ("UPI Payment".equals(selectedMode)) {
                paymentDetails = tfUpi.getText().trim();
                if (paymentDetails.isEmpty()) {
                    paymentDetails = "Scan Payment";
                }
            } 
            else if ("Cash Payment".equals(selectedMode)) {
                paymentDetails = "Cash Payment";
            } 
            else {
                JOptionPane.showMessageDialog(null, "Please select a valid payment mode.");
                return;
            }

            storePaymentDetails(selectedMode, paymentDetails);

            StringBuilder ticketMessage = new StringBuilder("Payment Successful!\nTotal Amount Paid: ₹")
            .append(totalPrice).append("\nTickets:\n");
            for (int i = 0; i < prnList.size(); i++) {
                String ticketNum = (i < ticketList.size()) ? ticketList.get(i) : "Not Found";
                ticketMessage.append("PNR: ").append(prnList.get(i)).append(" | Ticket: ").append(ticketNum).append("\n");
            }
            ticketMessage.append("Session ID: ").append(sessionId).append(" (Unique ID for fetching multiple PNR details)\n\n");
            JOptionPane.showMessageDialog(null, ticketMessage.toString());
            dispose();
        }
    }

    private void storePaymentDetails(String paymentMode, String paymentDetails) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql:///airlinemanagementsystem", "root", "200421");
             PreparedStatement pst = conn.prepareStatement("INSERT INTO payments (PNR, Seat, Amount, Payment_Mode, Payment_Details) VALUES (?, ?, ?, ?, ?)")) {
            for (int i = 0; i < prnList.size(); i++) {
                pst.setString(1, prnList.get(i));
                pst.setString(2, seatList.get(i));
                pst.setInt(3, getSeatPrice(seatList.get(i)));
                pst.setString(4, paymentMode);
                pst.setString(5, paymentDetails);
                pst.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchTicketNumbers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql:///airlinemanagementsystem", "root", "200421");
             PreparedStatement pst = conn.prepareStatement("SELECT PNR, TICKET FROM reservations WHERE Session_ID = ?")) {
            pst.setString(1, sessionId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ticketList.add(rs.getString("TICKET"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PaymentSystem("");
    }
}
