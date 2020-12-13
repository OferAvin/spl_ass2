package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    ExampleEvent e1;
    MessageBusImpl messageBus;
    ExampleBroadcast b1;
    ExampleBroadcastListenerService m1;
    ExampleBroadcastListenerService m2;
    ExampleEventHandlerService m3;
    ExampleEventHandlerService m4;



    @BeforeEach
    void setUp() {
        e1 = new ExampleEvent("attack");
        messageBus = MessageBusImpl.getInstance();
        b1 = new ExampleBroadcast("hi to everyone how subscribe to me");
        m1 = new ExampleBroadcastListenerService("tamir");
        m2 = new ExampleBroadcastListenerService("ofer");
        m3 = new ExampleEventHandlerService("Noach");
        m4 = new ExampleEventHandlerService("Moses");
    }

    @AfterEach
    void clearSetUp(){
        messageBus.unregister(m1);
        messageBus.unregister(m2);
        messageBus.unregister(m3);
        messageBus.unregister(m4);
    }

    @Test
    void complete() {
        messageBus.register(m3);
        m3.initialize();
        Future<String> f = messageBus.sendEvent(e1);
        assertFalse(f.isDone());
        String toCheck = "hi";
        messageBus.complete(e1,toCheck);
        assertTrue(f.isDone());
        assertTrue(toCheck.equals(f.get()));
    }

    @Test
    void awaitMessage() {
        try {
            assertFalse(messageBus.awaitMessage(m3) instanceof ExampleEvent);

        } catch (Exception e) {
            assertEquals(e.getMessage(), "this microservice is not registered");
        }
        messageBus.register(m3);
        m3.initialize();
        messageBus.sendEvent(e1);
        try {
            assertTrue(messageBus.awaitMessage(m3)instanceof ExampleEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendEvent() {
        ExampleEvent e2 = new ExampleEvent("defence");
        ExampleEvent e3 = new ExampleEvent("relax there is no need to fight");
        messageBus.register(m3);
        messageBus.register(m4);
        m3.initialize();
        m4.initialize();
        messageBus.sendEvent(e1);
        messageBus.sendEvent(e2);
        messageBus.sendEvent(e3);
        //check robin manner, this was not tested in the first application
        try {
            assertEquals(e1,messageBus.awaitMessage(m3));
            assertEquals(e2,messageBus.awaitMessage(m4));
            assertEquals(e3,messageBus.awaitMessage(m3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendBroadcast() {
        messageBus.register(m1);
        messageBus.register(m2);
        messageBus.register(m3);
        m1.initialize();
        m2.initialize();
        m3.initialize();
        messageBus.sendBroadcast(b1);
        messageBus.sendEvent(e1);
        try {
            assertEquals(b1,  messageBus.awaitMessage(m1));
            assertEquals(b1, messageBus.awaitMessage(m2));
            assertNotEquals(b1, messageBus.awaitMessage(m3));// check that only broadcast subscribers gets the broadcast message
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}