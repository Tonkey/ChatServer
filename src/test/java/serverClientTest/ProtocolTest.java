/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverClientTest;

import chatClient.client;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Server;

/**
 *
 * @author nickl
 */
public class ProtocolTest {
    
    
    public ProtocolTest() {
        
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
    public void testClientServerInteraction() throws InterruptedException, IOException{
        client c = new client();
        Thread ct = new Thread(c);
        c.connect("localhost", 1337);
        ct.start();
        
        
        c.sendMessage("LOGIN:TestClient");
        
        Thread.sleep(1500);
        
        assertEquals("CLIENTLIST:TestClient", c.getlastMsgRecieved());
        System.out.println("FIRST TEST PASSED!!!!!");
        client c2 = new client();
        Thread ct2 = new Thread(c2);
        c2.connect("localhost", 1337);
        ct2.start();
        
        c2.sendMessage("LOGIN:TestClient2");
        
        Thread.sleep(1500);
        
        c2.sendMessage("MSG:TestClient:Testing");
        
        Thread.sleep(2000);
        
        assertEquals("TestClient2 : Testing\n", c.getlastMsgRecieved());
    }
    
    
    
}
