package com.wifictrl.sender.core;

import java.io.Serializable;

import com.wifictrl.common.core.Info;

public interface Sender {

	public <T extends Serializable> void send(Info<T> obj);
	
}
