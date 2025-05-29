package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddCustomer extends JFrame implements ActionListener {
    
    JTextField tfname, tfphone, tfaadhar, tfnationailty, tfaddress;
    JButton save, addAnotherCustomer;
    JRadioButton rbmale, rbfemale;
    
    public AddCustomer() {
        
        setTitle("Add Customer Details");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close window? Details will not be saved.", "Warning", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        
        JLabel heading = new JLabel("ADD CUSTOMER DETAILS");
        heading.setBounds(250, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        heading.setForeground(Color.BLUE);
        add(heading);
        
        JLabel lblname = new JLabel("Name");
        lblname.setBounds(60, 80, 150, 25);
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblname);
        
        tfname = new JTextField();
        tfname.setBounds(220, 80, 150, 25);
        add(tfname);
        
        JLabel lblnationationailty = new JLabel("Nationality");
        lblnationationailty.setBounds(60, 130, 150, 25);
        lblnationationailty.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblnationationailty);
        
        tfnationailty = new JTextField();
        tfnationailty.setBounds(220, 130, 150, 25);
        add(tfnationailty);
        
        JLabel lblaadhar = new JLabel("Aadhar Number");
        lblaadhar.setBounds(60, 180, 150, 25);
        lblaadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblaadhar);
        
        tfaadhar= new JTextField();
        tfaadhar.setBounds(220, 180, 150, 25);
        add(tfaadhar); 
        
        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(60, 230, 150, 25);
        lbladdress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbladdress);
        
        tfaddress = new JTextField();
        tfaddress.setBounds(220, 230, 150, 25);
        add(tfaddress);
        
        JLabel lblgender = new JLabel("Gender");
        lblgender.setBounds(60, 280, 150, 25);
        lblgender.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblgender);
        
        ButtonGroup gendergroup = new ButtonGroup();
        
        rbmale = new JRadioButton("Male");
        rbmale.setBounds(220, 280, 70, 25);
        rbmale.setBackground(Color.WHITE);
        add(rbmale);
        
        rbfemale = new JRadioButton("Female");
        rbfemale.setBounds(300, 280, 70, 25);
        rbfemale.setBackground(Color.WHITE);
        add(rbfemale);
        
        gendergroup.add(rbmale);
        gendergroup.add(rbfemale);
        
        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(60, 330, 150, 25);
        lblphone.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblphone);
        
        tfphone = new JTextField();
        tfphone.setBounds(220, 330, 150, 25);
        add(tfphone);
        
        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/emp.png"));
        JLabel lblimage = new JLabel(image);
        lblimage.setBounds(450, 80, 280, 400);
        add(lblimage);
        
        save = new JButton("SAVE");
        save.setBackground(Color.BLACK);
        save.setForeground(Color.WHITE);
        save.setBounds(220, 380, 150, 30);
        save.addActionListener(this);
        add(save);
        
        addAnotherCustomer = new JButton("Add Another Customer");
        addAnotherCustomer.setBackground(Color.BLACK);
        addAnotherCustomer.setForeground(Color.WHITE);
        addAnotherCustomer.setBounds(220, 430, 200, 30);  // Position changed to avoid overlap
        addAnotherCustomer.addActionListener(this);
        add(addAnotherCustomer);
        
        setSize(900,600);
        setLocation(200, 50);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == save) {
            String name = formatText(tfname.getText());
            String nationality = formatText(tfnationailty.getText());
            String phone = tfphone.getText();
            String address = tfaddress.getText();
            String aadhar = tfaadhar.getText();
            String gender = null;
        
            if (rbmale.isSelected()) {
                gender = "Male";
            } else if (rbfemale.isSelected()) {
                gender = "Female";
            }

            // **Validation Checks**
            if (name.isEmpty() || nationality.isEmpty() || phone.isEmpty() || address.isEmpty() || aadhar.isEmpty() || gender == null) {
                JOptionPane.showMessageDialog(null, "All fields are required!");
                return;
            }

            if (!name.matches("^[a-zA-Z ]+$")) {
                JOptionPane.showMessageDialog(null, "Name must contain only alphabets (no numbers or special characters).");
                return;
            }

            if (!name.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Please enter your full name (first and last).");
                return;
            }

            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Phone number must be exactly 10 digits.");
                return;
            }

            if (!aadhar.matches("\\d{12}")) {
                JOptionPane.showMessageDialog(null, "Aadhar number must be exactly 12 digits.");
                return;
            }
        
            try {
                Conn conn = new Conn();
                String query = "INSERT INTO passenger (Name, Nationailty, Phone, Address, Aadhar, Gender) VALUES ('"+name+"', '"+nationality+"', '"+phone+"', '"+address+"', '"+aadhar+"', '"+gender+"')";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Customer Details Added Successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
        else if (ae.getSource() == addAnotherCustomer) {
            clearFields();  // Clears all fields but keeps the frame open
        }
    }
    
    private String formatText(String text) {
        String[] words = text.trim().split("\\s+");
        StringBuilder formattedText = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedText.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase()).append(" ");
            }
        }
        return formattedText.toString().trim();
    }
    
    private void clearFields() {
        tfname.setText("");
        tfphone.setText("");
        tfaadhar.setText("");
        tfnationailty.setText("");
        tfaddress.setText("");
        rbmale.setSelected(false);
        rbfemale.setSelected(false);
    }

    public static void main(String[] args) {
        new AddCustomer();
    }
}
