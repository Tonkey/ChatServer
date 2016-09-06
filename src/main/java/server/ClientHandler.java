package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author nickl
 */
public class ClientHandler extends Thread {

    private Scanner input;
    private PrintWriter writer;
    private Socket socket;
    private Pattern msgPattern;
    private Pattern loginPattern;

    
    private Pattern logoutPattern;

    private ConnectedUser connectedUser;

    private Server server;

    public ClientHandler(Socket socket, Server server) {

        msgPattern = Pattern.compile("MSG:.+:.+");
        loginPattern = Pattern.compile("LOGIN:.+");
        logoutPattern = Pattern.compile("LOGOUT:");

        try {
            input = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
            
        } catch (IOException e) {

        }

        this.socket = socket;
        this.server = server;

    }

    private ChatMessage readMessage() {

        String message = input.nextLine(); //IMPORTANT blocking call

        if (msgPattern.matcher(message).matches()) {

            String[] parts = message.split(":");

            if (parts[1].equalsIgnoreCase("")) {

                return new ChatMessage(ChatMessageType.MESSAGEALL, parts[2], null);

            } else {

                return new ChatMessage(ChatMessageType.MESSAGE, parts[2], parts[1].split(","));

            }

        } else if (loginPattern.matcher(message).matches()) {

            return new ChatMessage(ChatMessageType.LOGIN, message.split(":")[1], null);

        } else if (logoutPattern.matcher(message).matches()) {

            return new ChatMessage(ChatMessageType.LOGOUT, "", null);

        }

        return null;

    }

    @Override
    public void run() {

        try {
            
            send("Welcome to the server " + this.getName());

            while (true) {
                ChatMessage msg = this.readMessage();

                if (msg == null) {

                    writer.println("MSGRES:SERVER:Invalid command.");
                    continue;

                }

                switch (msg.getMessageType()) {

                    case LOGIN:
                        
                        if (this.connectedUser == null) {
                            this.connectedUser = new ConnectedUser(msg.getContent());
                        }
                        continue;
                        
                    case MESSAGE:
                        

                }

            }
/*
            System.out.println(String.format("Received the message: %1$S ", message));
            while (!message.equals("STOP")) {
                send(message);
                System.out.println(String.format("Received the message: %1$S ", message.toUpperCase()));
                message = input.nextLine(); //IMPORTANT blocking call
            }
            writer.println("STOP");

            writer.close();
            input.close();
            socket.close();

            server.removeHandler(this);
            System.out.println("Closed a Connection");
*/
        } catch (Exception ex) {
            System.out.println("something went wrong while closing thread:" + this.getName());
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void send(String message) {
        String preMsg = "MSGRES:";
        String msgSender = this.getName() + ":";

        writer.println(preMsg + msgSender + message);
    }
    
    public ConnectedUser getConnectedUser() {
        return connectedUser;
    }

}
