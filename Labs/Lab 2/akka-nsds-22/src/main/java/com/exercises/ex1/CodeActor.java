package com.exercises.ex1;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.counter.OtherMessage;
import com.counter.SimpleMessage;

public class CodeActor extends AbstractActor{

    private int counter;

    public CodeActor() {
        this.counter = 0;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(DataMessage.class, this::onDataMessage).build();
    }

    void onDataMessage(DataMessage msg) {

        if(msg.getCode()){
            counter++;
        } else counter --;
        System.out.println("counter: " + counter);
    }

    public static Props props() {
        return Props.create(CodeActor.class);
    }

}
