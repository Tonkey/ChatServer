/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Michael
 */
public class OutMsgHandler extends Message implements Runnable {

    public OutMsgHandler(String username, String message, ConcurrentLinkedQueue<Message> messageList) {
        super(username, message,messageList);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
