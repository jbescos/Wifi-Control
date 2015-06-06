package com.wifictrl.common.core;

import java.io.Serializable;

public class Info<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = -1439849130918355030L;
	private int action;
	private T data;

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Info [action=" + action + ", data=" + data + "]";
	}

}
