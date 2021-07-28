/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTCP;
import controller.ProcessBuffer;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.System.in;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RaelH
 */
public class Server_X_Client {
    
    
public static void main(String args[]){


    Socket s=null;
    ServerSocket ss2=null;
    System.out.println("Server Listening......");
    try{
        ss2 = new ServerSocket(4447); // can also use static final PORT_NUM , when defined

    }
    catch(IOException e){
    e.printStackTrace();
    System.out.println("Server error");

    }

    while(true){
        try{
            s= ss2.accept();
            System.out.println("connection Established");
            ServerThread st=new ServerThread(s);
            st.start();

        }

    catch(Exception e){
        e.printStackTrace();
        System.out.println("Connection Error");

    }
    }

}

}



class ServerThread extends Thread{  

    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;

    public ServerThread(Socket s){
        this.s=s;
    }

    public void run() {
        InputStream inputStream  = null; 
        OutputStream outputStream = null;
    try{
        //reader on client socket
        inputStream = new DataInputStream(s.getInputStream()); 
              
        //writes on client socket
         outputStream=new ObjectOutputStream(s.getOutputStream());
        // outputStream.flush();
         //os=new PrintWriter(s.getOutputStream());
        
    }catch(IOException e){
        System.out.println("IO error in server thread");
    }

    try {
        while(true){
            // Receiving data from client
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];
            baos.write(buffer, 0 , in.read(buffer));
            ProcessBuffer process = new ProcessBuffer();
            int frame = -1;
            
            byte result[] = baos.toByteArray();
            
            frame = process.ProcessBufferTCP(result);
           
            String res = Arrays.toString(result);
            System.out.println("Recieved from client : "+res);
            
          //  String inputString = "Hello World!";
           // byte[] byteArrray = inputString.getBytes();
           // out.write(byteArrray);
           
           if(frame == 10){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            System.out.println("yyyy/MM/dd HH:mm:ss-> "+dtf.format(LocalDateTime.now()));
           
            String data = dtf.format(LocalDateTime.now());
            os.println(data + " - A mensagem foi recebida e armazenada em uma tabela");
            os.flush();
           }else{
           
           }
           */
           int datagramSize = inputStream.available();
           if(datagramSize >= 4){
               //----------------- INIT -----------------------//
               ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
               byte[] bytes = new byte[buffer.limit()];
               inputStream.read(bytes);
               buffer.put(bytes).position(0);
               int rpcCodeInit = buffer.getInt();
                
               String hexaRPC = Integer.toHexString(rpcCodeInit).toUpperCase();
               System.out.println(hexaRPC);
               
               if(rpcCodeInit == 10){ ///0x0A
                   ProcessBuffer process = new ProcessBuffer();
                   process.ProcessBufferTCP(inputStream, s.getOutputStream());
               }else{
                   byte[] bytesDescarte = new byte[inputStream.available()];
                   System.out.println("limpando o inputstream, total bytes descartados ");
               }
              
           } 
           
        }
    } catch (IOException e) {

        line=this.getName(); //reused String line for getting thread name
        System.out.println("IO Error/ Client "+line+" terminated abruptly");
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client "+line+" Closed");
    }

    finally{    
    try{
        System.out.println("Connection Closing..");
        if (is!=null){
            is.close(); 
            System.out.println(" Socket Input Stream Closed");
        }

        if(os!=null){
            os.close();
            System.out.println("Socket Out Closed");
        }
        if (s!=null){
        s.close();
        System.out.println("Socket Closed");
        }

        }
    catch(IOException ie){
        System.out.println("Socket Close Error");
    }
    }//end finally
    }
    
    public static byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(out);
    os.writeObject(obj);
    return out.toByteArray();
}
public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
}
}
