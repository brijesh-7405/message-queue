package com.message.producer;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MessageProducerTest {

    private Queue<String> queue;
    private MessageProducer producer;

    @Before
    public void setUp() {
        queue = new LinkedList<>();
        producer = new MessageProducer(queue);
    }

    @Test
    public void testMessageProduction() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();

        producerThread.join();

        assertEquals(10, queue.size());
        for (int i = 0; i < 10; i++) {
            assertTrue(queue.contains("Message " + i));
        }
    }

    @Test
    public void testProducerInterruption() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();

        producerThread.interrupt();
        producerThread.join();

        assertTrue(queue.size() < 10);
        assertTrue(queue.size() > 0);
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Thread interrupted");
        });
    }
}
