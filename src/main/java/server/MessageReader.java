/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicolaicornelis
 */
public class MessageReader implements Runnable {
    

    Server server;
    boolean stop;
    
    public MessageReader(Server server) {
       
        this.server = server;
        this.stop = false;
        
    }
    
    public void stopServer() {
        
        this.stop = true;
        
    }
    
    @Override
    public void run() {
        
        while (stop == false) {
            
            try {
                
                ChatMessage msg = (ChatMessage)server.getMessageQueue().poll(100, TimeUnit.MILLISECONDS);
                
                if (msg != null) {
                    
                    server.sendMessage(msg);
                    
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
}
