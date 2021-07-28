/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.RegistrationDAO;
import DAO.UserDAO;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import model.Registration;
import model.User;

/**
 *
 * @author RaelH
 */
public class ProcessFrame {
    
    public ProcessFrame(){
    
    }
    
    public void sendAck(OutputStream  outputStream){
      
        try {
        byte[] arr = {
       (byte) 0x0A,(byte)0x00,(byte)0x00, 0x00,                    // INT INIT
       (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,                // INT BYTES
       (byte)0xA0,(byte)0x00,(byte)0x00,(byte)0x00,                // INT FRAME
       (byte)0x28,(byte)0x00,(byte)0x00,(byte)0x00,                // INT CRC
       (byte)0x0D,(byte)0x00,(byte)0x00,(byte)0x00};               //INT END 

        // sending data to server
        outputStream.write(arr);
        outputStream.flush();
        
    } catch (IOException i) {
        System.out.println(i);
     }
    }
    
    public void process( int frame, byte[] data, OutputStream  outputStream){
        switch (frame){
            case 161: // A1
                RegistrationDAO rdao = new RegistrationDAO();
                Registration    r    = new Registration();
                
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                System.out.println("yyyy/MM/dd HH:mm:ss-> "+ dtf.format(LocalDateTime.now()));
           
                String dataHora = dtf.format(LocalDateTime.now());
                String mensagem = new String(data, StandardCharsets.UTF_8);
                r.setMensagem(mensagem);
                r.setData(dataHora);
 
                if(rdao.add_registor(r)){
                    System.out.println("registro cadastrado com sucesso");
                    sendAck(outputStream);
                }else{
                    System.out.println("Erro ao cadastrar novo registro");
                }
                
                break;
                
            case 162: // A2
                
                // ----------------------------------IDADE---------------------------------
                byte[] byteidade   = new byte[] { (byte)data[0], (byte)data[1], (byte)data[2], (byte)data[3]};
                ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(byteidade).position(0);
                int     idade = buffer.getInt();
                String hexaIdade = Integer.toHexString(idade).toUpperCase();
                
                // ---------------------------------- PESO  ---------------------------------
                byte[] bytepeso    = new byte[] { (byte)data[4], (byte)data[5], (byte)data[6], (byte)data[7]};
                buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(bytepeso).position(0);
                int     peso = buffer.getInt();
                String hexaPeso = Integer.toHexString(peso).toUpperCase();
                
                // ---------------------------------- ALTURA ---------------------------------
                byte[] alturabyte  = new byte[] { (byte)data[8], (byte)data[9], (byte)data[10], (byte)data[11]};    
                buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(alturabyte).position(0);
                int     altura = buffer.getInt();
                String hexaAltura = Integer.toHexString(altura).toUpperCase();
                
                // ---------------------------------- Tamanho Nome ---------------------------------
                byte[] tamanhobyte = new byte[] { (byte)data[12], (byte)data[13], (byte)data[14], (byte)data[15]};
                buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(tamanhobyte).position(0);
                int     tamanho = buffer.getInt();
                String hexaTamanho = Integer.toHexString(tamanho).toUpperCase();
                
                
                // ---------------------------------- Nome ---------------------------------
                byte[] byteNome = new byte[tamanho];
                for(int i = 0; i < tamanho; i++){
                    byteNome[i] = data[i + 16];
                }
                String nome = new String(byteNome, StandardCharsets.UTF_8);
                
                System.out.println(nome);
                
                //---------------- INSERT BANCO --------------------------------
                UserDAO udao = new UserDAO();
                User    u    = new User();
       
                u.setIdade(idade);
                u.setPeso(peso);
                u.setAltura(altura);
                u.setTamanhoNome(tamanho);
                u.setNome(nome);
       
                if(udao.add_user(u)){
                    System.out.println("Usuario cadastrado com sucesso");
                    sendAck(outputStream);
                }else{
                    System.out.println("Erro ao cadastrar usuario");
                }
                
                break;
                           
            case 163: // A3
                break;               
        }
    }
}
