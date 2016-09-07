package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nickl
 */
public class Server {

    private boolean keepRunning;
    private ServerSocket serverSocket;
    private MessageReader messageReader;

    private final List<ClientHandler> clients;
    private final LinkedBlockingQueue messageQueue;

    public Server() {
        this.keepRunning = true;
        this.clients = new ArrayList();
        this.messageQueue = new LinkedBlockingQueue();
    }

    public void stopServer() {
        keepRunning = false;
    }

    public LinkedBlockingQueue getMessageQueue() {
        return messageQueue;
    }

    /**
     * Does not need synchronized because it uses a BlockingQueue. Multiple
     * threads trying to queue messages at the same time will wait.
     *
     * @param message
     */
    public void queueMessage(ChatMessage message) {

        messageQueue.add(message);

    }

    public void updateClientList() {

        messageQueue.add(new ChatMessage(
                ChatMessageType.CLIENTLIST, getUserNamesAsCsv(), null, null));

    }

    private String getUserNamesAsCsv() {

        StringBuilder output = new StringBuilder(110);

        synchronized (clients) {

            clients.stream().forEach((ClientHandler client) -> {

                ConnectedUser u = client.getConnectedUser();

                if (u != null) {
                    output.append(client.getConnectedUser().getUserName()).append(",");;
                }

            });

        }

        return output.toString().replaceAll(",$", "");

    }

    public boolean userNameAvailable(String userName) {

        synchronized (clients) {

            for (ClientHandler client : clients) {

                ConnectedUser user = client.getConnectedUser();

                if (user != null) {

                    if (userName.equalsIgnoreCase(user.getUserName())) {
                        return false;
                    }

                }

            };
        }

        return true;

    }

    /**
     * Only called by the single thread (MessageReader) polling the messageQueue
     *
     * @param msg
     */
    public void sendMessage(ChatMessage msg) {

        synchronized (clients) {

            clients.stream().forEach((ClientHandler client) -> {

                ConnectedUser user = client.getConnectedUser();

                // Only write to users that have logged in (not just connected)
                if (user != null) {

                    if (msg.getMessageType() == ChatMessageType.CLIENTLIST) {

                        client.updateClientList(msg);

                    } else {

                        String userName = user.getUserName();
                        // Don't send to self
                        if (!userName.equalsIgnoreCase(msg.getSender())) {

                            // Get all receivers (will be null if message is for all connected users)
                            String[] receivers = msg.getReceivers();
                            // Loop all recipients of the message (if sender != self)
                            if (receivers != null) {
                                for (String recipientName : receivers) {

                                    // If recipient is part of the threads user, write to the message to that thread
                                    if (recipientName.equalsIgnoreCase(userName)) {

                                        client.send(msg.getContent(), msg.getSender());

                                    }

                                }

                            } else {

                                client.send(msg.getContent(), msg.getSender());

                            }

                        }
                    }
                }
            });
        }
    }

    /**
     * Synchronized because multiple threads may attempt to remove their handler
     * from the list at the same time. The method is called by the
     * ClientHandlers themselves.
     *
     * @param ch
     */
    public void removeHandler(ClientHandler ch) {

        synchronized (clients) {
            if (clients.remove(ch)) {

                ch.closeConnection();

                updateClientList();

                String msg1 = "Client disconnected";
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, msg1);
                String msg2 = "Remaining amount of clients connected: " + clients.size();
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, msg2);
            }
        }
    }

    private void runServer() {

        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Starting the Server");

        this.messageReader = new MessageReader(this);
        new Thread(this.messageReader).start();

        try {

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("0.0.0.0", 1337));

            do {

                Socket socket = serverSocket.accept(); //Important Blocking call

                ClientHandler handler = new ClientHandler(socket, this);
                new Thread(handler).start();

                clients.add(handler);

                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Current amount of clients connected: " + clients.size());

            } while (keepRunning);

        } catch (IOException ex) {
            Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, null, ex);
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        try {
            Log.setLogFile("logFile.txt", "ServerLog");
            new Server().runServer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Log.closeLogger();
        }
    }

}
