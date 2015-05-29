package com.wifictrl.sender.core;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.wifictrl.common.core.Info;

public class InmediateSender implements Sender{

	private final static Logger log = LogManager.getLogger();
	private final ObjectMapper mapper = new ObjectMapper();
	private final DatagramManager manager;
	
	public InmediateSender(String host) throws SocketException, UnknownHostException{
		this(new DatagramManager(host));
	}
	
	public InmediateSender(DatagramManager manager){
		this.manager = manager;
	}
	
	@Override
	public <T> void send(Info<T> obj) {
		List<Info<T>> list = Arrays.asList(obj);
		try {
			manager.send(mapper.writeValueAsBytes(list));
		} catch (Exception e) {
			log.error("Error converting a json", e);
		}
	}

}
