package com.wifictrl.sender.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LoggerTest {

	private final Logger log = LogManager.getLogger();
	
	@Test
	public void logSomething(){
		log.debug("hello");
	}
	
}
