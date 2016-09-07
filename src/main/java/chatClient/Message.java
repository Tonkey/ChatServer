/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Michael
 */
public class Message {
    private String username , message;
    ConcurrentLinkedQueue<Message> messageList = new ConcurrentLinkedQueue<Message>();
    
    public Message(String username, String message, ConcurrentLinkedQueue messageList) {
        this.username = username;
        this.message = message;
        this.messageList = messageList;
    }
}
