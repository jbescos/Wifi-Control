package com.wifictrl.sender.core;

public interface Sender {

	public void send(Info obj);
	
	public static class Info{
		private String action;
		private String data;
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
	}
	
}
