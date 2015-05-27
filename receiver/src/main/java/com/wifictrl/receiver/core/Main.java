package com.wifictrl.receiver.core;

public class Main {

	public static void main(String[] args) {
		Thread thread = new Thread(new Receiver(new BotHandler()));
		thread.start();
	}

}
