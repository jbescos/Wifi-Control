package com.wifictrl.sender.core;

import com.wifictrl.common.core.Info;

public interface Sender {

	public <T> void send(Info<T> obj);
	
}
