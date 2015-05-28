package com.wifictrl.receiver.core;

import java.awt.AWTException;

public class Main {

	public static void main(String[] args) throws AWTException {
		Thread thread = new Thread(new Receiver(new BotHandler()));
		thread.start();
	}

}
