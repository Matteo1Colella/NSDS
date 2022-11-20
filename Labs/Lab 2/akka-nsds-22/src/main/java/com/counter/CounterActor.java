package com.counter;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class CounterActor extends AbstractActor {

	private int counter;

	public CounterActor() {
		this.counter = 0;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SimpleMessage.class, this::onMessage).match(OtherMessage.class, this::onOtherMessage).build();
	}

	void onOtherMessage(OtherMessage message){
		System.out.println("Received one message");
	}

	void onMessage(SimpleMessage msg) {
		++counter;
		System.out.println("Counter increased to " + counter);
	}

	static Props props() {
		return Props.create(CounterActor.class);
	}

}
