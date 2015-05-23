package com.wifictrl.sender.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BufferSender implements Sender{

	private final List<Info> queue = new ArrayList<>();
	private final long READ_MILLIS = 5L;
	
	public BufferSender() throws IOException{
		try(final DatagramManager manager = new DatagramManager()){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(READ_MILLIS);
							synchronized (queue) {
								// TODO
							}
						} catch (InterruptedException e) {}
					}
				}
			});
			t.start();
		}
	}

	@Override
	public void send(Info obj) {
		queue.add(obj);
	}
	
}
