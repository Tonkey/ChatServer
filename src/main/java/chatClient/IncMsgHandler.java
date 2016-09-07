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
public class IncMsgHandler extends Message implements Runnable {

    Message msg;
    String username, message;

    ConcurrentLinkedQueue<Message> messageList = new ConcurrentLinkedQueue<Message>();

    public IncMsgHandler(String username, String message, ConcurrentLinkedQueue<Message> messageList) {
        super(username, message,messageList);
    }

    @Override
    public void run() {
      messageList.add(msg);
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
