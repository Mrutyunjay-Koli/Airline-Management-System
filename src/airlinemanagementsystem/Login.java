package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton submit, reset, close;
    JTextField tfusername;
    JPasswordField tfpassword;

    public Login() {
        
        setTitle("Login");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(20, 20, 100, 20);
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(130, 22, 200, 20);
        add(tfusername);

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(20, 60, 100, 20);
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(130, 60, 200, 20);
        add(tfpassword);

        reset = new JButton("Reset");
        reset.setBounds(40, 120, 120, 20);
        reset.addActionListener(this);
        add(reset);

        submit = new JButton("Submit");
        submit.setBounds(190, 120, 120, 20);
        submit.addActionListener(this);
        add(submit);

        close = new JButton("Close");
        close.setBounds(120, 160, 120, 20);
        close.addActionListener(this);
        add(close);

        // Move focus from username to password field on pressing "Enter"
        tfusername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tfpassword.requestFocus();
                }
            }
        });

        // Submit the form automatically when "Enter" is pressed in the password field
        tfpassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitForm();  // Simulate form submission
                }
            }
        });

        setSize(400, 250);
        setLocation(450, 150);
        setVisible(true);
    }

    // Method to handle form submission logic
    private void submitForm() {
        String username = tfusername.getText();
        String password = tfpassword.getText();

        try {
            Conn c = new Conn();

            String query = "select * from login where username = '" + username + "' and password = '" + password + "'";

            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                new Home();  // Open new frame on successful login
                setVisible(false);  // Close the login window
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            submitForm();  // Call form submission logic
        } else if (ae.getSource() == close) {
            setVisible(false);  // Close the login window
        } else if (ae.getSource() == reset) {
            tfusername.setText("");
            tfpassword.setText("");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
