package chatClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;
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
public class client implements ObserveableInterface{

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
        port = 80;
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
//                executorService.execute(new MessageList(protocolPart[1], userList));

            case "MSGRES":
                executorService.execute(new IncMsgHandler(socket));

        }

    }

    private void sendMSG(String msg) {

        try {

//            String protocol = "MSG:" + gui.getReceivers() + ":" + msg;
            output = new PrintWriter(socket.getOutputStream(), true);
//            output.print(protocol);

        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        
    }
    
//    private static void startChat() {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>    
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new chatGUI().setVisible(true);
//            }
//        });
//    }
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

    
    public void run() {

        Thread incoming = new Thread(new IncMsgHandler(socket));
        incoming.start();

    }
    
    public void sendMessage(String msg) {
        output.write(msg);
    }

}
