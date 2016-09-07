/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Michael
 */
public class MessageList implements Runnable {

    static List<String> userList = new LinkedList<>();
    String[] userString;
    String users;
    chatGUI gui;
    

    public MessageList(String users, List<String> userList, String[] userString, chatGUI gui) {
        this.users = users;
        this.userList = userList;
        this.userString = userString;
        this.gui = gui;
    }

    public void run() {

        userString = users.split(",");
               
        for (String s : userString) {
            userList.add(s);
        }
        
        gui.setUserList(userList);
        
    }

}
