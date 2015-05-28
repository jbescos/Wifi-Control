package com.wifictrl.sender.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.wifictrl.common.core.Constants;

public class DatagramManager implements Closeable{

	private final DatagramSocket clientSocket;
	private final InetAddress address;
	
	public DatagramManager(String host) throws SocketException, UnknownHostException{
		clientSocket = new DatagramSocket(); 
		address = InetAddress.getByName(host);
	}
	
	public void send(byte[] data) throws IOException{
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, Constants.PORT);
	    clientSocket.send(sendPacket); 
	}
	
	@Override
	public void close() throws IOException {
		clientSocket.close();
	}

}
