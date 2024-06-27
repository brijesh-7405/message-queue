package com.message.consumer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import java.util.LinkedList;
import java.util.Queue;

public class MessageConsumerTest {

    private Queue<String> queue;
    private MessageConsumer consumer;

    @Before
    public void setUp() {
        queue = new LinkedList<>();
        consumer = new MessageConsumer(queue);
    }

    @Test
    public void testMessageProcessing() throws InterruptedException {
        queue.add("Message 1");
        queue.add("Message 2");
        queue.add("Message 3");

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        consumer.stop();

        consumerThread.join();

        assertEquals(3, consumer.getProcessedCount());
        assertEquals(0, consumer.getErrorCount());
    }

    @Test
    public void testErrorProcessing() throws InterruptedException {
        queue.add("Message 1");
        queue.add("Message 5");
        queue.add("Message 3");

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        consumer.stop();

        consumerThread.join();

        assertEquals(2, consumer.getProcessedCount());
        assertEquals(1, consumer.getErrorCount());
    }

    @Test
    public void testStopConsumer() throws InterruptedException {
        queue.add("Message 1");
        queue.add("Message 2");

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        consumer.stop();

        consumerThread.join();

        int processed = consumer.getProcessedCount();
        assertTrue(processed >= 0 && processed <= 2);
    }

    @Test
    public void testEmptyQueue() throws InterruptedException {
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumer.stop();

        consumerThread.join();

        assertEquals(0, consumer.getProcessedCount());
        assertEquals(0, consumer.getErrorCount());
    }
}
