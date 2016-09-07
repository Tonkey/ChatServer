/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;


/**
 *
 * @author Michael
 */
public class MessageList implements Runnable {

    String[] userList;
    String users;
    chatGUI gui;
    

    public MessageList(String users,  String[] userString, chatGUI gui) {
        this.users = users;
        this.userList = userList;
        this.gui = gui;
    }

    public void run() {

        userList = users.split(",");
        gui.setUserList(userList);       
    }

}
