package com.wifictrl.common.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeData {

	public static byte[] toBytes(Serializable obj) throws IOException{
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos);){
			out.writeObject(obj);
			return bos.toByteArray();
		}
	}
	
	public static <T> T toObject(byte[] serial) throws ClassNotFoundException, IOException{
		try(ByteArrayInputStream bis = new ByteArrayInputStream(serial); ObjectInput in = new ObjectInputStream(bis);){
			@SuppressWarnings("unchecked")
			T obj = (T) in.readObject(); 
			return obj;
		}
	}
	
}
