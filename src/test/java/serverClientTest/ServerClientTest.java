/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverClientTest;

import chatClient.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import server.Server;

/**
 *
 * @author nickl
 */
public class ServerClientTest {

    public ServerClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Server.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        Server.stopServer();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testClientServerInteraction() throws InterruptedException, IOException {
        
        int sleepTime = 500;
        
        client c = new client();
        Thread ct = new Thread(c);
        c.connect("localhost", 1337);
        ct.start();

        c.sendMessage("LOGIN:TestClient");

        Thread.sleep(sleepTime);

        assertEquals("CLIENTLIST:TestClient", c.getlastMsgRecieved());
        System.out.println("FIRST TEST PASSED!!!!!");
        client c2 = new client();
        Thread ct2 = new Thread(c2);
        c2.connect("localhost", 1337);
        ct2.start();

        c2.sendMessage("LOGIN:TestClient2");

        Thread.sleep(sleepTime);

        c2.sendMessage("MSG:TestClient:Testing Single reciever");

        Thread.sleep(sleepTime);

        assertEquals("MSGRES:TestClient2:Testing Single reciever", c.getlastMsgRecieved());
        System.out.println("SECOND TEST PASSED");
        
        c.sendMessage("MSG::Testing send to all");
        
        Thread.sleep(sleepTime);
        
        assertEquals("MSGRES:TestClient:Testing send to all", c2.getlastMsgRecieved());
        System.out.println("THIRD TEST PASSED");
        
        client c3 = new client();
        Thread ct3 = new Thread(c3);
        c3.connect("localhost", 1337);
        ct3.start();
        c3.sendMessage("LOGIN:TestClient3");
        
        Thread.sleep(sleepTime);
        
        c3.sendMessage("MSG:TestClient,TestClient2:Testing multiple chosen recievers");
        
        Thread.sleep(sleepTime);
        
        assertEquals("MSGRES:TestClient3:Testing multiple chosen recievers", c.getlastMsgRecieved());
        assertEquals("MSGRES:TestClient3:Testing multiple chosen recievers", c2.getlastMsgRecieved());
        System.out.println("FOURTH TEST PASSED");
        
        c3.sendMessage("LOGOUT:");
        
        Thread.sleep(sleepTime);
        
        assertEquals("CLIENTLIST:TestClient,TestClient2", c.getlastMsgRecieved());
        assertEquals("CLIENTLIST:TestClient,TestClient2", c2.getlastMsgRecieved());
        System.out.println("FIFTH TEST PASSED");
        System.out.println("DOES WORK!!!!");
        
        //Logout on the remaining clients!
        c.sendMessage("LOGOUT:");
        c2.sendMessage("LOGOUT:");
        
    }

    @Test
    public void testSingleConnection() throws IOException, InterruptedException {
        System.out.println("Testing Single Connection!!!!!");

        client c = new client();
        c.connect("localhost", 1337);
        assertTrue(c.getSocket().isBound());

        c.closeConnection();

        System.out.println("Testing Single Connection Is Done!!!!!!!");
        Thread.sleep(1000);
    }

    @Test
    public void testMultipleConnections() throws IOException, InterruptedException {
        System.out.println("Testing Multiple Connections!!!!!!");
        int amount = 20;
        List<client> clientList = new ArrayList();

        System.out.println("Making " + amount + " connection(s)");
        for (int i = 0; i < amount; i++) {
            client c = new client();
            c.connect("localhost", 1337);
            clientList.add(c);
            Thread.sleep(500);
        }
        System.out.println(clientList.size() + " Connection(s) Created");
        System.out.println("Testing connection(s)!");

        clientList.stream().forEach((c) -> {
            assertTrue(c.getSocket().isBound());
        });

        System.out.println("Testing Multiple Connections Is Done!!!!!!!");
    }

}
