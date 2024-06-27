package com.message.consumer;

import java.util.Queue;

/**
 This class implements a message consumer that reads messages from a queue and processes them.
 It provides methods to stop the consumer and retrieve processing statistics.
 */
public class MessageConsumer implements Runnable{
    /** The queue containing messages to be consumed.*/
     private Queue<String> queue;

     /** Counter for successfully processed messages. */
    private int processedCount = 0;

    /** Counter for messages that failed processing.*/
    private int errorCount = 0;

    /** Flag to control the consumer's running state. Volatile ensures visibility across threads. */
    private volatile boolean running = true;

    /** Initializes the consumer with the message queue. */
    public MessageConsumer(Queue<String> queue) {
        this.queue = queue;
    }

    /** Overrides the run() method from the Runnable interface.*/
    @Override
    public void run() {
        // Loop continues as long as the consumer is running or there are messages in the queue.
        while (running || !queue.isEmpty()) {
            try {
                String message;
                // Synchronized block to ensure thread-safe access to the queue.
                synchronized (queue) {
                    message = queue.poll();
                }
                // Retrieves a message from the queue (if available)
                if (message != null) {
                    processMessage(message);  // Processes the message only if it's not null.
                    processedCount++;   //Increments the processed message counter.
                    System.out.println("Consumed: " + message);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                errorCount++;
                System.err.println("Error consuming message: " + e.getMessage());
            }
        }
    }

    /** Sets the running flag to false to signal the consumer to stop. */
    public void stop() {
        running = false;
        // Synchronizes access to the queue for thread safety.
        synchronized (queue) {
            queue.notifyAll(); // Notifies the consumer thread if it's waiting on the queue.
        }
    }

    /** This method defines the specific logic for processing a message. */
    private void processMessage(String message) {
        // The current example throws an exception for messages containing "5" for error count.
        if (message.contains("5")) {
            throw new RuntimeException("Error processing message");
        }
    }

    /** Returns the number of successfully processed messages. */
    public int getProcessedCount() {
        return processedCount;
    }

    /** Returns the number of messages that failed processing. */
    public int getErrorCount() {
        return errorCount;
    }
}
