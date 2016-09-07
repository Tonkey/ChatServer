package chatClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class client implements ObserveableInterface, Runnable {

    static List<ObserverInterface> observerList;

    static Socket socket;
    static private int port;
    static private InetAddress serverAddress;
    static private Scanner input;
    static private PrintWriter output;

    static String[] userList;

    public static void connect(String address, int port) throws UnknownHostException, IOException {

        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        observerList = new ArrayList();
    }

    public void closeConnection() {
        try {
            keeprunning = false;
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addObserver(ObserverInterface o) {
        observerList.add(o);
    }

    @Override
    public void removeObserver(ObserverInterface o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObserver(String msg) {

        observerList.stream().forEach((o) -> {
            o.update(msg);
        });

    }

    @Override
    public synchronized void notifyObserver(String[] userList) {
        observerList.stream().forEach((o) -> {
            o.update(userList);
        });
    }

    boolean keeprunning = true;
    
    @Override
    public void run() {
        
        Thread t1 = new Thread(new IncMsgHandler(socket,input));
        t1.start();
        
    }

    public void sendMessage(String msg) {
        output.println(msg);
    }

}
