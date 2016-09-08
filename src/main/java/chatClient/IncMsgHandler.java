/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class IncMsgHandler implements Runnable {

    private Socket socket;
    private Scanner input;
    private client client;
    private String useInTest;

    public IncMsgHandler(Socket socket, Scanner input, client client) {
        this.socket = socket;
        this.input = input;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            input = new Scanner(socket.getInputStream());
            while (client.keeprunning) {
                if (input.hasNext()) {
                    String protocol = input.nextLine();
//                    System.out.println("INSIDE MSGHANDLER:   " + protocol);
                    client.setLastMsgRecieved(protocol);

                    String[] protocolPart = protocol.split(":");

                    switch (protocolPart[0]) {
                        case "CLIENTLIST":

                            String[] listOfUsers = protocolPart[1].split(",");
                            client.notifyObserver(listOfUsers);
                            break;
                            
                        case "MSGRES":
                            String someMSg = timestamp() + protocolPart[1] + " : " + protocolPart[2] + "\n";
                            client.notifyObserver(someMSg);
                            break;
                        default:
                            break;
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(IncMsgHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public String getProtocol() {
        return useInTest;
    }

}
