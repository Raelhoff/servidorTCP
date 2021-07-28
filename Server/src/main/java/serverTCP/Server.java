/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTCP;

/**
 *
 * @author RaelH
 */
// A Java program for a Server 
import java.net.*;
import java.util.Arrays;
import java.io.*; 
import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import javax.net.*;
import java.io.*;
import java.net.*;
public class Server 
{ 
 //initialize socket and input stream 
 private Socket   socket = null; 

 private InputStream in  = null; 
 private OutputStream out = null;

 // constructor with port 
 public Server(int port) throws IOException 
 { 
     ServerSocketFactory serverSocketFactory =
     ServerSocketFactory.getDefault();
     ServerSocket serverSocket = null;
  // starts server and waits for a connection 
  try
  { 
         serverSocket =
        serverSocketFactory.createServerSocket(port);
   } catch (IOException ignored) {
      System.err.println("Unable to create server");
      System.exit(-1);
    }
   System.out.println("Server started"); 

   System.out.println("Waiting for a client ..."); 

   while (true) {
      Socket socket = null;
      try {
        socket = serverSocket.accept();
        
        // takes input from the client socket 
        in = new DataInputStream(socket.getInputStream()); 
        
        //writes on client socket
        out = new DataOutputStream(socket.getOutputStream());

        // Receiving data from client
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        baos.write(buffer, 0 , in.read(buffer));
   
        byte result[] = baos.toByteArray();

        String res = Arrays.toString(result);
        System.out.println("Recieved from client : "+res); 
        
      } catch (IOException exception) {
        // Just handle next request.
      } finally {
        if (socket != null) {
          try {
            socket.close();
          } catch (IOException ignored) {
          }
     
        }
      }
   }
 }

 public static void main(String args[]) throws IOException 
 { 
  new Server(5002); 
 } 
}