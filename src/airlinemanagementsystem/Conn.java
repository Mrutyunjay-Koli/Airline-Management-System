package airlinemanagementsystem;

import java.sql.*;

public class Conn {
    
    Connection c;
    Statement s;
    
    public Conn(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //Driver Registered
            c = DriverManager.getConnection("jdbc:mysql:///airlinemanagementsystem", "root", "200421"); //create connection string
            s = c.createStatement(); //create the statement
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
