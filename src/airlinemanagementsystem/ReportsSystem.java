package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class ReportsSystem extends JFrame implements ActionListener {

    JTabbedPane tabbedPane;
    JTable customerTable, bookingTable, paymentTable, flightTable;
    JButton exportPDF, printReport, close;
    JPanel chartPanel;

    public ReportsSystem() {
        setTitle("Overall Reports");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10, 10, 990, 250);
        add(tabbedPane);

        customerTable = new JTable();
        bookingTable = new JTable();
        paymentTable = new JTable();
        flightTable = new JTable();

        tabbedPane.add("Customer Reports", new JScrollPane(customerTable));
        tabbedPane.add("Booking Reports", new JScrollPane(bookingTable));
        tabbedPane.add("Payment Reports", new JScrollPane(paymentTable));
        tabbedPane.add("Flight Reports", new JScrollPane(flightTable));

        chartPanel = new JPanel();
        chartPanel.setBounds(10, 270, 990, 250);
        add(chartPanel);

        int btnY = 530;

        JButton loadCustomers = new JButton("Load Customers");
        loadCustomers.setBounds(40, btnY, 150, 30);
        loadCustomers.addActionListener(this);
        add(loadCustomers);

        JButton loadBookings = new JButton("Load Bookings");
        loadBookings.setBounds(210, btnY, 150, 30);
        loadBookings.addActionListener(this);
        add(loadBookings);

        JButton loadPayments = new JButton("Load Payments");
        loadPayments.setBounds(380, btnY, 150, 30);
        loadPayments.addActionListener(this);
        add(loadPayments);

        JButton loadFlights = new JButton("Load Flights");
        loadFlights.setBounds(550, btnY, 150, 30);
        loadFlights.addActionListener(this);
        add(loadFlights);

        JButton loadCharts = new JButton("Load Charts");
        loadCharts.setBounds(720, btnY, 150, 30);
        loadCharts.addActionListener(this);
        add(loadCharts);

        int bottomY = 580;

        exportPDF = new JButton("Export to PDF");
        exportPDF.setBounds(180, bottomY, 150, 30);
        exportPDF.addActionListener(this);
        add(exportPDF);

        printReport = new JButton("Print Report");
        printReport.setBounds(370, bottomY, 150, 30);
        printReport.addActionListener(this);
        add(printReport);

        close = new JButton("Close");
        close.setBounds(560, bottomY, 150, 30);
        close.addActionListener(this);
        add(close);

        setSize(1020, 650);  // Final frame size
        setLocation(200, 30); // Center it a bit higher on screen
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            Conn conn = new Conn();
            String query = "";
            ResultSet rs;

            if (ae.getActionCommand().equals("Load Customers")) {
                query = "SELECT * FROM passenger";
                rs = conn.s.executeQuery(query);
                customerTable.setModel(DbUtils.resultSetToTableModel(rs));
            } else if (ae.getActionCommand().equals("Load Bookings")) {
                query = "SELECT * FROM reservations";
                rs = conn.s.executeQuery(query);
                bookingTable.setModel(DbUtils.resultSetToTableModel(rs));
            } else if (ae.getActionCommand().equals("Load Payments")) {
                query = "SELECT * FROM payments";
                rs = conn.s.executeQuery(query);
                paymentTable.setModel(DbUtils.resultSetToTableModel(rs));
            } else if (ae.getActionCommand().equals("Load Flights")) {
                query = "SELECT * FROM flight";
                rs = conn.s.executeQuery(query);
                flightTable.setModel(DbUtils.resultSetToTableModel(rs));
            } else if (ae.getActionCommand().equals("Load Charts")) {
                loadCharts(conn);
            } else if (ae.getActionCommand().equals("Export to PDF")) {
                JOptionPane.showMessageDialog(null, "PDF Export coming soon!");
            } else if (ae.getActionCommand().equals("Print Report")) {
                bookingTable.print();
            } else if (ae.getActionCommand().equals("Close")) {
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCharts(Conn conn) {
        try {
            int bookedCount = 0;
            int canceledCount = 0;
            int economySeats = 0;
            int businessSeats = 0;
            int firstClassSeats = 0;
            double totalRevenue = 0;

            ResultSet rsBooked = conn.s.executeQuery("SELECT COUNT(*) AS bookedCount FROM reservations");
            if (rsBooked.next()) bookedCount = rsBooked.getInt("bookedCount");

            ResultSet rsCanceled = conn.s.executeQuery("SELECT COUNT(*) AS canceledCount FROM cancel");
            if (rsCanceled.next()) canceledCount = rsCanceled.getInt("canceledCount");

            rsBooked = conn.s.executeQuery("SELECT Seat_Number FROM seat_selection");
            while (rsBooked.next()) {
                String seat = rsBooked.getString("Seat_Number");
                if (seat.startsWith("C") || seat.startsWith("D")) economySeats++;
                else if (seat.startsWith("B") || seat.startsWith("E")) businessSeats++;
                else if (seat.startsWith("A") || seat.startsWith("F")) firstClassSeats++;
            }

            ResultSet rsRevenue = conn.s.executeQuery("SELECT SUM(amount) AS totalRevenue FROM payments");
            if (rsRevenue.next()) totalRevenue = rsRevenue.getDouble("totalRevenue");

            DefaultPieDataset pieDataset = new DefaultPieDataset();
            pieDataset.setValue("Booked", bookedCount);
            pieDataset.setValue("Canceled", canceledCount);
            JFreeChart pieChart = ChartFactory.createPieChart("Ticket Booking Statistics", pieDataset, true, true, false);
            ChartPanel piePanel = new ChartPanel(pieChart);

            DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
            barDataset.addValue(economySeats, "Seats", "Economy");
            barDataset.addValue(businessSeats, "Seats", "Business");
            barDataset.addValue(firstClassSeats, "Seats", "First Class");
            JFreeChart barChart = ChartFactory.createBarChart("Seat Category Statistics", "Category", "Count", barDataset, PlotOrientation.VERTICAL, true, true, false);
            ChartPanel barPanel = new ChartPanel(barChart);

            // Bar Chart for Top 5 Most Booked Flights
            DefaultCategoryDataset flightDataset = new DefaultCategoryDataset();
            ResultSet rsTopFlights = conn.s.executeQuery(
            "SELECT Flight_Code, COUNT(*) AS total " +
            "FROM reservations " +
            "GROUP BY Flight_Code " +
            "ORDER BY total DESC " +
            "LIMIT 5"
            );
            while (rsTopFlights.next()) {
            String flight = rsTopFlights.getString("Flight_Code");
            int total = rsTopFlights.getInt("total");
            flightDataset.addValue(total, "Bookings", flight);
            }

            JFreeChart topFlightChart = ChartFactory.createBarChart(
            "Top 5 Most Booked Flights", "Flight_Code", "Total Bookings",
            flightDataset, PlotOrientation.VERTICAL, true, true, false);
            ChartPanel topFlightPanel = new ChartPanel(topFlightChart);


            chartPanel.removeAll();
            chartPanel.setLayout(new GridLayout(1, 3));
            chartPanel.add(piePanel);
            chartPanel.add(barPanel);
            chartPanel.add(topFlightPanel);
            chartPanel.revalidate();
            chartPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ReportsSystem();
    }
}
