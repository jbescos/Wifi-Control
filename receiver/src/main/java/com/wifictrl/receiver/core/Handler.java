package com.wifictrl.receiver.core;

import java.io.IOException;

public interface Handler {

	void handle(byte[] stream) throws IOException, ClassNotFoundException;
	
}
