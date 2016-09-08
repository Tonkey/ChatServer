package chatClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Executors;

/**
 *
 * @author Michael
 */
public class client implements ObserveableInterface, Runnable {

    static List<ObserverInterface> observerList;

    static Socket socket;
    static private int port;
    static private InetAddress serverAddress;
    static private String ip;

    StringBuilder stringB = new StringBuilder();

    static private Scanner input;
    static private PrintWriter output;

    static String[] userList;
    static ConcurrentLinkedQueue<Message> messageList = new ConcurrentLinkedQueue<Message>();

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void connect(String address, int port) throws UnknownHostException, IOException {

        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        observerList = new ArrayList();
    }

    private void addNewUser(String username) {
        String LoginProtocol = "LOGIN:" + username;
        output.println(LoginProtocol);
    }

    public void print(String msg) {
//        receiveMSG(msg);
    }

    public void closeConnection() {
        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recieveProtocol(String protocol) {

        String[] protocolPart = protocol.split(":");

        switch (protocolPart[0]) {
            case "CLIENTLIST":

            case "MSGRES":
                executorService.execute(new IncMsgHandler(socket));

        }

    }

    private void sendMSG(String msg) {

        try {

            output = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

    }

    @Override
    public void addObserver(ObserverInterface o) {
        observerList.add(o);
    }

    @Override
    public void removeObserver(ObserverInterface o) {
        // not used in this case
    }

    @Override
    public void notifyObserver(String msg) {

        for (ObserverInterface o : observerList) {
            o.update(msg);
        }

    }

    @Override
    public void notifyObserver(String[] userList) {
        for (ObserverInterface o : observerList) {
            o.update(userList);
        }
    }

    boolean keeprunning = true;
    
    @Override
    public void run() {
        
        do{
            if(input.hasNextLine()){
                Thread read = new Thread(new IncMsgHandler(socket));
                read.start();
            }
        } while(keeprunning);
        
    }

    public void sendMessage(String msg) {
        output.println(msg);
    }

}
