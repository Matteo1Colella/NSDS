package com.exercises.ex1;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.counter.OtherMessage;
import com.counter.SimpleMessage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CounterCode {

    private static final int numThreads = 10;
    private static final int numMessages = 100;

    public static void main(String[] args) {

        final ActorSystem sys = ActorSystem.create("System");
        final ActorRef counter = sys.actorOf(CodeActor.props(), "counter");

        // Send messages from multiple threads in parallel
        final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numMessages; i++) {
            exec.submit(() -> counter.tell(new DataMessage(false), ActorRef.noSender()));
            exec.submit(() -> counter.tell(new DataMessage(true), ActorRef.noSender()));
        }

        // Wait for all messages to be sent and received
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exec.shutdown();
        sys.terminate();

    }

}