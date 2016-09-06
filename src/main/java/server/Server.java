package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nickl
 */
public class Server {
    
    
    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    
    List<ClientHandler> clients = new ArrayList();
    
    public static void stopServer() {
        keepRunning = false;
    }

    public void send(String msg){
        clients.stream().forEach((client) -> {
            client.send(msg);
        });
    }
    
    public void removeHandler(ClientHandler ch){
        
        if (clients.remove(ch)) {
            String msg1 = "Client: " + ch.getName() + " disconnected";
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, msg1);
            String msg2 = "Remaining amount of clients connected: " + clients.size();
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, msg2);
        }
    }
    
    private void runServer() {

        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Starting the Server");
        
        try {
            
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("0.0.0.0", 1337));
            System.out.println("test");
            
            do {
                
                Socket socket = serverSocket.accept(); //Important Blocking call
                ClientHandler client = new ClientHandler(socket, this);
                
                clients.add(client);
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Client " + client.getName() + " connected to the server");
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Current amount of clients connected: " + clients.size());
               
                client.start();
                
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
