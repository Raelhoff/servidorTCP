/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 *
 * @author RaelH
 */
public class ProcessBuffer {
    
    public ProcessBuffer(){
    }
    
    public byte[] intToBytes(int value) {
    return new byte[] {
        (byte) (0xff & (value >> 24)),
        (byte) (0xff & (value >> 16)),
        (byte) (0xff & (value >>  8)),
        (byte) (0xff & value)
    };
}

public  int bytesToInt(byte[] bytes) {
    return ((bytes[0] & 0xff) << 24)
         | ((bytes[1] & 0xff) << 16)
         | ((bytes[2] & 0xff) << 8)
         | (bytes[3] & 0xff);
}
    
    public void  ProcessBufferTCP(InputStream inputStream, OutputStream  outputStream ) throws IOException{
           //----------------- BYTES -----------------------//
               ByteBuffer buffer2 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
               byte[] bytes2 = new byte[buffer2.limit()];
               inputStream.read(bytes2);
                
                buffer2.put(bytes2).position(0);
		int tamanhoBytes = buffer2.getInt();
                
                String hexatamanhoBytes = Integer.toHexString(tamanhoBytes).toUpperCase();
                System.out.println(hexatamanhoBytes);
                
            //----------------- FRAME -----------------------//
                ByteBuffer buffer3 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                byte[] bytes3 = new byte[buffer2.limit()];
                inputStream.read(bytes3);
                
                buffer3.put(bytes3).position(0);
		int frame = buffer3.getInt();
                
                String hexaFrame = Integer.toHexString(frame).toUpperCase();
                System.out.println(hexaFrame);
                
            //----------------- DATA -----------------------//
             ByteBuffer buffer4 = ByteBuffer.allocate(tamanhoBytes).order(ByteOrder.LITTLE_ENDIAN);
             byte[] data = new byte[buffer4.limit()];
             inputStream.read(data);
            
          
             System.out.println(data);
             
             //----------------- CRC -----------------------//
             ByteBuffer buffer5 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
             byte[] bytes5 = new byte[buffer5.limit()];
             inputStream.read(bytes5);
                
             buffer5.put(bytes5).position(0);
             int CRC = buffer5.getInt();
                
             String hexaCRC = Integer.toHexString(CRC).toUpperCase();
             System.out.println(hexaCRC);    
             
             //----------------- END -----------------------//
             ByteBuffer buffer6 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
             byte[] bytes6 = new byte[buffer6.limit()];
             inputStream.read(bytes6);
                
             buffer6.put(bytes6).position(0);
             int end = buffer6.getInt();
                
             String hexaEnd = Integer.toHexString(end).toUpperCase();
             System.out.println(hexaEnd); 
             
             if(validaCRC() && validaEND()){
                 ProcessFrame pFrame = new ProcessFrame();
                 pFrame.process(frame, data, outputStream);
             }
    }
    
   public Boolean validaCRC(){
       return true;
   }
   
   public Boolean validaEND(){
       return true;
   }

    private ProcessFrame ProcessFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
   
   
}
