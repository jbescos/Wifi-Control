package com.wifictrl.receiver.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.common.core.Constants;

public class Receiver implements Runnable{
	
	private final Handler handler;
	private final Logger log = LogManager.getLogger();

	public Receiver(Handler handler){
		this.handler = handler;
	}

	@Override
	public void run() {
		try(DatagramSocket serverSocket = new DatagramSocket(Constants.PORT);){
			byte[] receiveData = new byte[Constants.PACKET_SIZE];
			while(true){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	            try {
					serverSocket.receive(receivePacket);
					handler.handle(receivePacket.getData());
				} catch (IOException e) {
					log.error("Can not receive", e);
				}
			}
		} catch (SocketException e) {
			log.error("Can not open connection, exiting program", e);
		}
	}
	
}
