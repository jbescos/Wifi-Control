package com.wifictrl.sender.core;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

public class BufferSender implements Sender{

	private final List<Info<?>> queue = new Vector<>();
	private volatile boolean flag;
	private final ObjectMapper mapper = new ObjectMapper();
	private final static Logger log = LogManager.getLogger();
	private final DatagramManager manager;
	private final Thread thread;
	private CountDownLatch latch;
	private long totalSent;
	
	public BufferSender() throws SocketException, UnknownHostException{
		this(new DatagramManager(), 2L);
	}
	
	public BufferSender(final DatagramManager manager, final long READ_MILLIS){
		this.manager = manager;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(flag){
					try {
						Thread.sleep(READ_MILLIS);
						synchronized (queue) {
							if(queue.size() != 0){
								totalSent = totalSent + queue.size();
								log.debug("Sending "+queue);
								manager.send(mapper.writeValueAsBytes(queue));
								queue.clear();
							}
						}
					} catch (Exception e) {
						log.error("Unexpected error", e);
					}
				}
				latch.countDown();
			}
		});
	}
	
	List<Info<?>> getQueue(){
		return queue;
	}
	
	public void start() throws SocketException, UnknownHostException, IOException{
		totalSent = 0;
		latch = new CountDownLatch(1);
		flag = true;
		thread.start();
		
	}
	
	public void stop() throws InterruptedException{
		flag = false;
		latch.await();
		log.info("Total elements sent "+totalSent);
	}

	@Override
	public <T> void send(Info<T> obj) {
		queue.add(obj);
	}
	
}
