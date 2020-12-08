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
        Future<String> a = messageBus.sendEvent(e1);
        String toCheck = "hi";
        assertFalse(a.isDone());
        messageBus.complete(e1,toCheck);
        assertTrue(a.isDone());
        assertTrue(toCheck.equals(a.get()));
    }

    @Test
    void sendBroadcast() {
        m1 = new ExampleBroadcastListenerService("tamir");
        m2 = new ExampleBroadcastListenerService("ofer");
        m3 = new NonBroadcastListener("yair");
        m1.initialize();
        m2.initialize();
        m3.initialize();
        messageBus.sendBroadcast(b1);
        try {
            assertEquals(b1,  messageBus.awaitMessage(m1));
            assertEquals(b1, messageBus.awaitMessage(m2));
            assertNotEquals(b1, messageBus.awaitMessage(m3));
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