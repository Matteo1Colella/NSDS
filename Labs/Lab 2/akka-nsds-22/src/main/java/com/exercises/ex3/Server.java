package com.exercises.ex3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;


import java.io.IOException;

public class Server {
    private static final int numThreads = 10;
    private static final int numMessages = 100;

    public static void main(String[] args) {

        final ActorSystem sys = ActorSystem.create("System");
        final ActorRef server = sys.actorOf(ServerActor.props(), "server");

        // Send messages from multiple threads in parallel
        //final ExecutorService exec = Executors.newFixedThreadPool(numThreads);

        // for (int i = 0; i < numMessages; i++) {
        //     exec.submit(() -> counter.tell(new DataMessage(false), ActorRef.noSender()));
        //     exec.submit(() -> counter.tell(new DataMessage(true), ActorRef.noSender()));
        // }

        server.tell(new PutMsg("Nikolas", "nikolas@gmail.com"), ActorRef.noSender());
        server.tell(new PutMsg("Maria", "mmm@gmail.com"), ActorRef.noSender());
        server.tell(new PutMsg("Kevin", "xxx@gmail.com"), ActorRef.noSender());
        server.tell(new GetMsg("Nikolas", "nikolas@gmail.com"), ActorRef.noSender());

        // Wait for all messages to be sent and received
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sys.terminate();

    }
}
