package com.wifictrl.sender.core;

public interface Sender {

	public <T> void send(Info<T> obj);
	
}
