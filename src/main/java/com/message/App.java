package com.message;

import com.message.consumer.MessageConsumer;
import com.message.producer.MessageProducer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The App class is the entry point of the message-queue application.
 * It sets up the shared queue, creates producer and consumer instances, and starts their threads.
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Program Started: " );

        /** Create a shared queue for communication between producer and consumer. */
        Queue<String> queue = new LinkedList<>();

        /** Create a MessageProducer instance with the shared queue */
        MessageProducer producer = new MessageProducer(queue);

        /** Create a MessageConsumer instance with the shared queue */
        MessageConsumer consumer = new MessageConsumer(queue);

        /** Create threads for the producer and consumer */
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        /**  Start the producer and consumer threads */
        producerThread.start();
        consumerThread.start();

        try {
            // Wait for the producer thread to finish
            producerThread.join();

            // Signal the consumer to stop after the producer has finished
            consumer.stop();

            // Wait for the consumer thread to finish
            consumerThread.join();
        } catch (InterruptedException e) {
            // Handle interruption during thread joining
            e.printStackTrace();
        }

        System.out.println("Total messages processed successfully: " + consumer.getProcessedCount());
        System.out.println("Total errors encountered: " + consumer.getErrorCount());
    }
}
