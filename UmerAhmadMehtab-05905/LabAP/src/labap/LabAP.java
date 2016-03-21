/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;


public class LabAP 
{
    
    public static boolean CreateTab()
    {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost/cities";
        try
        {
             Connection conn = null;
             Statement stmt = null;
             Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection(DB_URL,"root","");
            String sql = "CREATE TABLE record (id INTEGER not null, country VARCHAR(30),region VARCHAR(30),city VARCHAR(30), postalCode VARCHAR(30), latitude DECIMAL(5), longitude DECIMAL(5),metroCode INT,areaCode INT,PRIMARY KEY(id)   )";
             
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(Exception sq)
        {
            sq.printStackTrace(System.out);
        }
        return true;  
    }
    
    public static boolean SetRecord()
    {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost/cities";
        try
        {
             Connection conn = null;
             Statement stmt = null;
             Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection(DB_URL,"root","");
             
            stmt = conn.createStatement();
            
            
            String csvFile = "GeoLiteCity-Location.csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            String sql;
            br = new BufferedReader(new FileReader(csvFile));
            int count = 0;

            
            while ((line = br.readLine()) != null &&  count < 10000) 
            {
                count++;
                if(count   <3)
                    continue;
                
                String [] cols = line.split(",");
                String id = cols[0];
                String country = cols[1].replace("\"", "");
                String region = cols[2].replace("\"", "");
                String city = cols[3].replace("\"", "");
                String postalCode = cols[4].replace("\"", "");
                String lat = cols[5];
                String lon = cols[6];
                sql = "INSERT into record(id, country, region, city, postalCode, latitude, longitude) VALUES (" + Integer.parseInt(id) + ",'" + country + "','" + region + "','" + city + "','" + postalCode + "'," + Float.parseFloat(lat) + "," + Float.parseFloat(lon) + ");";
                stmt.executeUpdate(sql);

                
            }
            
            
        }
        catch(Exception sq)
        {
            sq.printStackTrace(System.out);
        }
        return true;  
    }

    
    public static void main(String[] args) 
    {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost/cities";
        try
        {
             Connection conn = null;
             Statement stmt = null;
             Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection(DB_URL,"root","");
             
            stmt = conn.createStatement();
            
            boolean a = CreateTab();
            System.out.println("Table Created");
            boolean b = SetRecord();
             System.out.println("Records Done");
            
            
            float L1;
            float L2;
            
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Latitude");
            L1 = sc.nextFloat();
            System.out.println("Enter Longitude");
            L2 = sc.nextFloat();
            String city;
             city = sc.nextLine();
             
             PreparedStatement pst = null;
            pst = conn.prepareStatement("SELECT city, ( 3959 * acos( cos( radians(?) ) * cos( radians( ? ) ) * cos( radians( ? ) - radians(?) ) + sin( radians(?) ) * sin(radians(?)) ) ) AS distance FROM record HAVING distance < 25");
				pst.setFloat(1, L1);
				pst.setFloat(2, L1);
				pst.setFloat(3, L2);
				pst.setFloat(4, L2);
				pst.setFloat(5, L1);
				pst.setFloat(6, L1);
            ResultSet rs = pst.executeQuery();
            
            
            while(rs.next())
              {
                
                String first = rs.getString("city");
               
                System.out.println("City :  " + first);
                }
            
            rs.close();
            
             
            System.out.println("Enter city");
            city = sc.nextLine(); 
            PreparedStatement pst2  = conn.prepareStatement("SELECT latitude, longitude from record WHERE city =?;");
            pst2.setString(1,city);
            ResultSet rs1 = pst2.executeQuery();
             
            
            
            System.out.println("Latitude and Longitude of "+ city);
             while(rs1.next())
            {
             
              float first = rs1.getFloat(1);
              float second = rs1.getFloat(2);
              System.out.println("Latitude :  " + first + " Longitude : "+ second) ;
            }
              
        }
        catch(Exception sq)
        {
            sq.printStackTrace(System.out);
        }
        
    }
    
}
