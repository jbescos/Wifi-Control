package com.wifictrl.common.core;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class SerializeData {

	private final static ObjectMapper mapper = new ObjectMapper();
	
	public static byte[] toBytes(Object obj) throws IOException{
		return mapper.writeValueAsBytes(obj);
	}
	
	public static <T> T toObject(byte[] serial, Class<T> clazz) throws IOException{
		return mapper.readValue(serial, clazz);
	}
	
}
