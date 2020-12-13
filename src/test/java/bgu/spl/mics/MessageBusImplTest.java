package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    ExampleEvent e1;
    MessageBusImpl messageBus;
    ExampleBroadcast b1;
    ExampleBroadcastListenerService m1;
    ExampleBroadcastListenerService m2;
    NonBroadcastListener m3;
    ExampleEventHandlerService m4;


    @BeforeEach
    void setUp() {
        e1 = new ExampleEvent("attack");
        messageBus = MessageBusImpl.getInstance();
        b1 = new ExampleBroadcast("hi to everyone how subscribe to me");
    }

    @Test
    void complete() {
        m4 = new ExampleEventHandlerService("noach");
        messageBus.register(m4);
        m4.initialize();
        Future<String> f = messageBus.sendEvent(e1);
        assertFalse(f.isDone());
        String toCheck = "hi";
        messageBus.complete(e1,toCheck);
        assertTrue(f.isDone());
        assertTrue(toCheck.equals(f.get()));
    }

    @Test
    void sendBroadcast() {
        m1 = new ExampleBroadcastListenerService("tamir");
        m2 = new ExampleBroadcastListenerService("ofer");
        m4 = new ExampleEventHandlerService("noach");
        messageBus.register(m1);
        messageBus.register(m2);
        messageBus.register(m4);
        m1.initialize();
        m2.initialize();
        m4.initialize();
        messageBus.sendBroadcast(b1);
        messageBus.sendEvent(e1);
        try {
            assertEquals(b1,  messageBus.awaitMessage(m1));
            assertEquals(b1, messageBus.awaitMessage(m2));
            assertNotEquals(b1, messageBus.awaitMessage(m4));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendEvent() {
        m4 = new ExampleEventHandlerService("omer");
        m4.initialize();
        messageBus.sendEvent(e1);
        try {
            assertEquals(e1,messageBus.awaitMessage(m4));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void awaitMessage() {
        m4 = new ExampleEventHandlerService("omer");
        try {
            assertFalse(messageBus.awaitMessage(m4) instanceof ExampleEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m4.initialize();
        try {
            assertTrue(messageBus.awaitMessage(m4)instanceof ExampleEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}