/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author nicolaicornelis
 */
public class ChatMessage {
    
    private final ChatMessageType messageType;
    private final String content;
    private final String[] receivers;
   
    public ChatMessage(ChatMessageType type, String content, String[] receivers) {
        
        this.messageType = type;
        this.content = content;
        this.receivers = receivers;
        
        
    }

    public ChatMessageType getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public String[] getReceivers() {
        return receivers;
    }
    
}
