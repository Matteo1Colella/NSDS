package com.exercises.ex3;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.exercises.ex1.DataMessage;

import java.util.HashMap;
import java.util.Map;

public class ServerActor extends AbstractActor {

    private Map<String, String> contactList;

    public ServerActor() {
        contactList = new HashMap<>();
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(PutMsg.class, this::onPutMessage).match(GetMsg.class, this::onGetMessage).build();
    }

    void onPutMessage(PutMsg msg) {
        String rName = msg.getName();
        String rEmail = msg.getEmail();
        contactList.put(rName, rEmail);
        System.out.println("Added Contact " + rName + ", Email address: " +  rEmail);
    }

    void onGetMessage(GetMsg msg) {
        String rName = msg.getName();
        String value = contactList.get(rName);
        System.out.println("Found Contact " + rName + ", Email address: " + value);
        contactList.get(rName);
    }

    public static Props props() {
        return Props.create(ServerActor.class);
    }

}
