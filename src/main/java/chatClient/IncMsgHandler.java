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

    Socket socket;
    Scanner input;
    client client = new client();

    public IncMsgHandler(Socket socket, Scanner input) {
        this.socket = socket;
        this.input = input;
    }

    @Override
    public void run() {

        try {
            input = new Scanner(socket.getInputStream());
            while (client.keeprunning) {
                if (input.hasNextLine()) {
                    String protocol = input.nextLine();

                    String[] protocolPart = protocol.split(":");

                    switch (protocolPart[0]) {
                        case "CLIENTLIST":
                            String[] listOfUsers = protocolPart[1].split(",");
                            client.notifyObserver(listOfUsers);
                            continue;
                        case "MSGRES":
                            client.notifyObserver(timestamp() + protocolPart[1] + " : " + protocolPart[2] + "\n");
                            continue;
                        default:
                            break;
                    }

                }
            }

        } catch (IOException ex) {
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

}
