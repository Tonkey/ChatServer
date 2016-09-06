package chatClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class client {

    static Socket socket;
    static private int port;
    static private InetAddress serverAddress;
    static private String ip;

    StringBuilder stringB = new StringBuilder();

    static private Scanner input;
    static private PrintWriter output;

    static List<String> userList = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        connect(ip, port);
        startChat();
    }

    public static void connect(String address, int port) throws UnknownHostException, IOException {
        port = 80;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    }

    private void addNewUser(String username) {

    }

    private void Login() throws IOException {

        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);

        int count = 0;
        while (true) {

            output.println("Please write your username...\n");
            String newInput = input.nextLine();
            while (userList.contains(newInput)) {
                while (count < 4) {
                    count++;
                    output.println("sorry user is already in use, please try another...");
                    if (count > 3) {
                        output.println("too many illegal tries ...");
                        socket.close();
                    }
                }
            }
            addNewUser(input.nextLine());
        }
    }

    private void Logout() {
        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recieveProtocol(String protocol) {
        
        String[] protocolParts = protocol.split(":");

        switch(protocolParts[0]) {
            case "CLIENTLIST":
            
            case "MSGRES":
                recieveMSG(timestamp() + protocolParts[1] + ": " + protocolParts[2]);
        }
        
      

    }
    
    public void recieveMSG(String msg) {
        
    }

    private String sendMSG(String msg) {

        String users = String.join(",", userList);
        String protocol = "MSG:" + users + ":" + msg;

        return protocol;
    }

    private static void startChat() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new chatGUI().setVisible(true);
            }
        });
    }

    private static String timestamp() {
        Calendar calendar = new GregorianCalendar();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int SECOND = calendar.get(Calendar.SECOND);

        return HOUR + ":" + MINUTE + ":" + SECOND + ": ";

    }
}
