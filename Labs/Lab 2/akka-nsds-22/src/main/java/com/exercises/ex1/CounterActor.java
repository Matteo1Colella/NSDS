package com.exercises.ex1;


import akka.actor.AbstractActor;
import akka.actor.Props;
import com.counter.OtherMessage;
import com.counter.SimpleMessage;

public class CounterActor extends AbstractActor {

    private int counter;

    public CounterActor() {
        this.counter = 0;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SimpleMessage.class, this::onMessage).match(OtherMessage.class, this::onOtherMessage).build();
    }

    void onMessage(SimpleMessage msg) {
        ++counter;
        System.out.println("Counter increased to " + counter);
    }

    void onOtherMessage(OtherMessage msg) {
        --counter;
        System.out.println("Counter decreased to " + counter);
    }

    public static Props props() {
        return Props.create(CounterActor.class);
    }

}
