package com.message.producer;

import java.util.Queue;

/**
 * This class is responsible for producing messages and adding them to a shared queue.
 */
public class MessageProducer implements Runnable{
    /** The shared queue where messages will be added */
    private Queue<String> queue;

    /** Constructor to initialize the MessageProducer with the shared queue. */
    public MessageProducer(Queue<String> queue) {
        this.queue = queue;
    }

    /**
     * The run method contains the logic for producing messages.
     * It runs in a separate thread and produces 10 messages, adding each to the queue.
     * After adding each message, it notifies any waiting consumer threads and sleeps for 1 second.
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                String message = "Message " + i;
                synchronized (queue) {
                    // Add the message to the queue
                    queue.add(message);
                    queue.notifyAll();
                    Thread.sleep(1000);
                }
                System.out.println("Produced: " + message);
            }
        } catch (InterruptedException e) {
            // If the thread is interrupted, throw a runtime exception
            throw new RuntimeException(e);
        }
    }
}
