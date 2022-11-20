package com.exercises.ex2;

import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithStash;
import akka.actor.Props;
import com.exercises.ex1.CodeActor;
import com.exercises.ex1.DataMessage;

public class CounterActor extends AbstractActorWithStash {
    private int counter;

    public CounterActor() {
        this.counter = 0;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(DataMessage.class, this::onDataMessage).build();
    }

    void onDataMessage(DataMessage msg) {

        if(msg.getCode()){
            counter++;
            unstashAll();
            System.out.println("counter: " + counter);
        }
        if (!msg.getCode() && counter > 0){
            counter --;
            System.out.println("counter: " + counter);
        }
        if (!msg.getCode() && counter == 0){
            stash();
        }


    }

    public static Props props() {
        return Props.create(CounterActor.class);
    }

}
