package com.wifictrl.sender.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DatagramManager implements Closeable{

	private final DatagramSocket clientSocket;
	private final String HOST = "localhost";
	private final int PORT = 9876;
	private final InetAddress address;
	
	public DatagramManager() throws SocketException, UnknownHostException{
		clientSocket = new DatagramSocket(); 
		address = InetAddress.getByName(HOST);
	}
	
	public void send(byte[] data) throws IOException{
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, PORT);
	    clientSocket.send(sendPacket); 
	}
	
	@Override
	public void close() throws IOException {
		clientSocket.close();
	}

}
