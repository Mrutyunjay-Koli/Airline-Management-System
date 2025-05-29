package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class SeatSelection extends JFrame implements ActionListener {
    String sessionId;
    JButton[][] seats;
    JButton confirmSeat;
    ArrayList<String> prnList = new ArrayList<>();
    int currentPrnIndex = 0;
    String selectedSeat = "";
    Set<String> bookedSeats = new HashSet<>();
    JLabel lblPrice;

    public SeatSelection(String sessionId) {
        this.sessionId = sessionId;
        setTitle("Airplane Seat Selection");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Select Your Seat");
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        heading.setBounds(200, 10, 400, 30);
        add(heading);

        lblPrice = new JLabel("Seat Price: ");
        lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPrice.setBounds(250, 40, 250, 30);
        add(lblPrice);

        fetchBookedSeats();
        fetchPRNs();

        if (bookedSeats.size() >= 70) {
            JOptionPane.showMessageDialog(this, "No Seats Available! Flight is fully booked.");
            setVisible(false);
            return;
        }

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(10, 7, 10, 10));
        seatPanel.setBounds(50, 70, 600, 400);
        seatPanel.setBackground(Color.LIGHT_GRAY);
        add(seatPanel);

        String[][] seatNames = new String[10][7];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (j == 3) {
                    seatNames[i][j] = ""; // Aisle
                } else {
                    char rowLetter = (char) ('A' + j - (j > 3 ? 1 : 0));
                    seatNames[i][j] = rowLetter + String.valueOf(i + 1);
                }
            }
        }

        seats = new JButton[10][7];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (seatNames[i][j].equals("")) {
                    seatPanel.add(new JLabel("")); // Aisle Space
                } else {
                    seats[i][j] = new JButton(seatNames[i][j]);
                    seats[i][j].setFont(new Font("Tahoma", Font.BOLD, 14));
                    seats[i][j].setFocusPainted(false);
                    seats[i][j].setToolTipText("Price: ₹" + getSeatPrice(seatNames[i][j]));

                    if (bookedSeats.contains(seatNames[i][j])) {
                        seats[i][j].setBackground(Color.RED); // Booked seat
                        seats[i][j].setEnabled(false);
                    } else {
                        seats[i][j].setBackground(new Color(173, 216, 230)); // Available seat
                        seats[i][j].addActionListener(this);
                        seats[i][j].addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                JButton seatButton = (JButton) e.getSource();
                                lblPrice.setText("Seat Price: ₹" + seatButton.getToolTipText().replace("Price: ₹", ""));
                            }
                        });
                    }
                    seatPanel.add(seats[i][j]);
                }
            }
        }

        confirmSeat = new JButton("Confirm Seat");
        confirmSeat.setBounds(250, 500, 150, 30);
        confirmSeat.addActionListener(this);
        confirmSeat.setEnabled(false);
        add(confirmSeat);

        setSize(750, 600);
        setLocation(300, 100);
        setVisible(true);

        showNextPRNDialog(); // Show PRN dialog after GUI loads
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

    private void fetchBookedSeats() {
        try {
            Conn conn = new Conn();
            String query = "SELECT Seat_Number FROM seat_selection";
            ResultSet rs = conn.s.executeQuery(query);

            while (rs.next()) {
                bookedSeats.add(rs.getString("Seat_Number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchPRNs() {
        try {
            Conn conn = new Conn();
            String query = "SELECT PNR FROM reservations WHERE Session_ID = '" + sessionId + "'";
            ResultSet rs = conn.s.executeQuery(query);

            while (rs.next()) {
                prnList.add(rs.getString("PNR"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNextPRNDialog() {
        if (currentPrnIndex < prnList.size()) {
            JOptionPane.showMessageDialog(this, "Select seat for PNR: " + prnList.get(currentPrnIndex));
            enableAvailableSeats(); // Ensure seats become clickable after PRN selection
        } else {
            JOptionPane.showMessageDialog(this, "All seats selected! Redirecting to Payment.");
            setVisible(false);
            new PaymentSystem(sessionId);
        }
    }

    private void enableAvailableSeats() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (seats[i][j] != null && !bookedSeats.contains(seats[i][j].getText())) {
                    seats[i][j].setEnabled(true);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (ae.getSource() == seats[i][j]) {
                    selectedSeat = seats[i][j].getText();
                    confirmSeat.setEnabled(true);
                }
            }
        }

        if (ae.getSource() == confirmSeat) {
            if (selectedSeat.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a seat before confirming!");
                return;
            }

            try {
                Conn conn = new Conn();
                String insertQuery = "INSERT INTO seat_selection (PNR, Seat_Number) VALUES ('" + prnList.get(currentPrnIndex) + "', '" + selectedSeat + "')";
                conn.s.executeUpdate(insertQuery);

                bookedSeats.add(selectedSeat);
                confirmSeat.setEnabled(false);
                selectedSeat = "";
                currentPrnIndex++;

                if (currentPrnIndex < prnList.size()) {
                    showNextPRNDialog();
                } else {
                    JOptionPane.showMessageDialog(null, "All seats selected! Redirecting to Payment.");
                    setVisible(false);
                    new PaymentSystem(sessionId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SeatSelection("");
    }
}
