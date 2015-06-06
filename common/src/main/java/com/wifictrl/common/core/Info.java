package com.wifictrl.common.core;


public class Info<T>{

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
