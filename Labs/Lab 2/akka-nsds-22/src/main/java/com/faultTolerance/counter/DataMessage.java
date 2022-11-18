package com.faultTolerance.counter;

//action taken depends on the code of the message

public class DataMessage {

	private int code;
	
	public int getCode() {
		return code;
	}

	public DataMessage(int code) {
		this.code = code;
	}
	
}
