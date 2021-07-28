package teste;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
/**
 *
 * @author RaelH
 * java -jar h2-1.4.200.jar
 * create table hello(id bigint primary key, mensagem varchar(50), data varchar(50)); 
 */
public class CreateTablesUSER {
    // JDBC driver name and database URL 
   static final String JDBC_DRIVER = "org.h2.Driver";   
   static final String DB_URL = "jdbc:h2:~/test";  
//  Database credentials 
   static final String USER = "sa"; 
   static final String PASS = ""; 
  
   public static void main(String[] args) { 
      Connection conn = null; 
      Statement stmt = null; 
      try { 
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
             
         //STEP 2: Open a connection 
         System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         //STEP 3: Execute a query 
         System.out.println("Creating table in given database..."); 
         stmt = conn.createStatement(); 
         String sql =  "CREATE TABLE   USER " + 
            "(id INTEGER NOT NULL AUTO_INCREMENT, " + 
            " idade  INTEGER, " +  
            " peso   INTEGER, " +  
            " altura INTEGER, " + 
            " tamanho_nome INTEGER, " + 
            " nome VARCHAR(255), " +  
            " PRIMARY KEY ( id ))";  
         stmt.executeUpdate(sql);
         System.out.println("Created table in given database..."); 
         
         // STEP 4: Clean-up environment 
         stmt.close(); 
         conn.close(); 
      } catch(SQLException se) { 
         //Handle errors for JDBC 
         se.printStackTrace(); 
      } catch(Exception e) { 
         //Handle errors for Class.forName 
         e.printStackTrace(); 
      } finally { 
         //finally block used to close resources 
         try{ 
            if(stmt!=null) stmt.close(); 
         } catch(SQLException se2) { 
         } // nothing we can do 
         try { 
            if(conn!=null) conn.close(); 
         } catch(SQLException se){ 
            se.printStackTrace(); 
         } //end finally try 
      } //end try 
      System.out.println("Goodbye!");
   } 
}
