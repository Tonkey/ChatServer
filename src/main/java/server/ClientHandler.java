package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nickl
 */
public class ClientHandler extends Thread{
    
    private Scanner input;
    private PrintWriter writer;
    private Socket socket;
    
    private Server server;
    
    ClientHandler(Socket socket, Server server) {
        try{
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e){
            
        }
        this.socket = socket;
        this.server = server;
        
    }
    
    @Override
    public void run(){
        
        try {
        send("Welcome to the server " + this.getName());
        String message = input.nextLine(); //IMPORTANT blocking call
        System.out.println(String.format("Received the message: %1$S ", message));
        while (!message.equals("STOP")) {
            send(message);
            System.out.println(String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
            writer.println("STOP");
            
            writer.close();
            input.close();
            socket.close();
            server.removeHandler(this);
        System.out.println("Closed a Connection");
        } catch (IOException ex) {
            System.out.println("something went wrong while closing thread:" + this.getName());
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void send(String message){
        String preMsg = "MSGRES:";
        String msgSender = this.getName() + ":";
        
        writer.println(preMsg+msgSender+message);
    }
    
}
