package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author nickl
 */
public class ClientHandler implements Runnable {

    private Scanner input;
    private PrintWriter writer;
    private final Socket socket;
    private Pattern msgPattern;
    private Pattern loginPattern;
    private Pattern msgAllPattern;
    private Pattern logoutPattern;

    private ConnectedUser connectedUser;

    private final Server server;

    public ClientHandler(Socket socket, Server server) {

        msgPattern = Pattern.compile("MSG:[^:]+:.{1,255}");
        msgAllPattern = Pattern.compile("MSG::+.{1,255}");
        loginPattern = Pattern.compile("LOGIN:[^:,\\s]{1,25}");
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

            return new ChatMessage(ChatMessageType.MESSAGE, parts[2], parts[1].split(","), connectedUser.getUserName());

        } else if (msgAllPattern.matcher(message).matches()) {

            String[] parts = message.split("::");

            return new ChatMessage(ChatMessageType.MESSAGE, parts[1], null, connectedUser.getUserName());

        } else if (loginPattern.matcher(message).matches()) {

            return new ChatMessage(ChatMessageType.LOGIN, message.split(":")[1], null, null);

        } else if (logoutPattern.matcher(message).matches()) {

            return new ChatMessage(ChatMessageType.LOGOUT, "", null, null);

        }

        return null;

    }

    public void processMessage(ChatMessage message) {

        switch (message.getMessageType()) {

            case LOGIN:
                
                synchronized(connectedUser) {
                    connectedUser = new ConnectedUser(message.getContent());
                }

                server.queueMessage(
                        new ChatMessage(
                                ChatMessageType.MESSAGE,
                                connectedUser.getUserName() + " joined",
                                null,
                                "SERVER"
                        ));

                server.queueMessage(
                        new ChatMessage(
                                ChatMessageType.MESSAGE,
                                "Welcome to the server, " + connectedUser.getUserName(),
                                new String[]{connectedUser.getUserName()},
                                "SERVER"
                        ));
                
                server.updateClientList();

                break;

            case MESSAGE:

                server.queueMessage(message);
                break;

            case LOGOUT:

                server.queueMessage(
                        new ChatMessage(
                                ChatMessageType.MESSAGE,
                                connectedUser.getUserName() + " disconnected",
                                null,
                                "SERVER"
                        ));

                server.removeHandler(this);
                break;

        }

    }

    @Override
    public void run() {

        try {

            //writer.println("Welcome to the server . Please enter your username:");
            while (true) {

                try {
                    // This will block until client sends something
                    ChatMessage msg = readMessage();

                    if (msg == null) {

                        // Directly answer client from same thread if invalid request and re-run while loop
                        send("Invalid Command", "SERVER");
                        continue;

                    }

                    // If not, queue it
                    processMessage(msg);

                } catch (NoSuchElementException | IllegalStateException e) {

                    server.removeHandler(this);

                }

            }

        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void updateClientList(ChatMessage msg) {
        
        writer.println("CLIENTLIST:" + msg.getContent());
        
    }

    public void send(String message, String sender) {
        writer.println("MSGRES:" + sender + ":" + message);
    }

    public ConnectedUser getConnectedUser() {
        synchronized(connectedUser) {
            return connectedUser;
        }
    }

    public void closeConnection() {

        try {
            writer.close();
            input.close();
            socket.close();
        } catch (IOException ex) {
            //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
