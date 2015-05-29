package com.wifictrl.receiver.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	private final static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		try {
			Thread thread = new Thread(new Receiver(new BotHandler()));
			thread.start();
		} catch (Exception e) {
			log.error("Unexpected error", e);
		}
		
	}

}
