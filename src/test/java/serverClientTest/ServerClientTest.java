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
                String[] args = new String[2];
                
                Server.main(args);
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
    public void testSingleConnection() throws IOException, InterruptedException{
        System.out.println("Testing Single Connection!!!!!");
        
        client c = new client();
        c.connect("localhost", 1337);
        assertTrue(c.getSocket().isBound());
        
        c.closeConnection();
        
        System.out.println("Testing Single Connection Is Done!!!!!!!");
        Thread.sleep(2000);
    }
    
    @Test
    public void testMultipleConnections() throws IOException, InterruptedException{
        System.out.println("Testing Multiple Connections!!!!!!");
        int amount = 20;
        List<client> clientList = new ArrayList();
        
        System.out.println("Making " + amount + " connection(s)");
        for(int i = 0; i < amount; i++){
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
