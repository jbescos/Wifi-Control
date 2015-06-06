package com.wifictrl.sender.core;

import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.common.core.Info;
import com.wifictrl.common.core.SerializeData;

public class InmediateSender implements Sender{

	private final static Logger log = LogManager.getLogger();
	private final DatagramManager manager;
	
	public InmediateSender(String host) throws SocketException, UnknownHostException{
		this(new DatagramManager(host));
	}
	
	public InmediateSender(DatagramManager manager){
		this.manager = manager;
	}
	
	@Override
	public <T extends Serializable> void send(Info<T> obj) {
		try{
			manager.send(SerializeData.toBytes(obj));
		} catch (Exception e) {
			log.error("Error converting a json", e);
		}
	}

}
