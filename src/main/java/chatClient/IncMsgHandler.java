/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Michael
 */
public class IncMsgHandler implements Runnable {

    Message msg;
    String username, message;
    chatGUI gui;

    ConcurrentLinkedQueue<Message> messageList = new ConcurrentLinkedQueue<Message>();

    public IncMsgHandler(ConcurrentLinkedQueue<Message> messageList, chatGUI gui) {
        this.messageList = messageList;
        this.gui = gui;
    }

    @Override
    public void run() {
        
        while(messageList.peek() != null) {
            printMessage(messageList.poll());
        }
        
    }
    
    private void printMessage(Message message) {
        
//        gui.writeMessage(timestamp() + message.getUsername() + " :\t" + message.getMessage());
        
    }

    
    /**
     * 
     * @return returning timestamp "##:## AM/PM CEST :" 
     */
    private static String timestamp() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatted = new SimpleDateFormat("HH:mm:ssa z");
        return formatted.format(calendar.getTime()) + ": ";
    }
    
}
