package com.wifictrl.sender.core;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

public class BufferSender implements Sender{

	private final List<Info> queue = new ArrayList<>();
	private final long READ_MILLIS = 5L;
	private volatile boolean flag;
	private final ObjectMapper mapper = new ObjectMapper();
	private final static Logger log = LogManager.getLogger();
	private final DatagramManager manager;
	private final Thread thread;
	
	public BufferSender() throws SocketException, UnknownHostException{
		this(new DatagramManager());
	}
	
	public BufferSender(DatagramManager manager){
		this.manager = manager;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(flag){
					try {
						Thread.sleep(READ_MILLIS);
						synchronized (queue) {
							BufferSender.this.manager.send(mapper.writeValueAsBytes(queue));
							queue.clear();
						}
					} catch (Exception e) {
						log.error("Unexpected error", e);
					}
				}
			}
		});
	}
	
	public void start() throws SocketException, UnknownHostException, IOException{
		flag = true;
		thread.start();
		
	}
	
	public void stop(){
		flag = false;
	}

	@Override
	public void send(Info obj) {
		queue.add(obj);
	}
	
}
