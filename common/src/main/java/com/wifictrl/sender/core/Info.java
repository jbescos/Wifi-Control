package com.wifictrl.sender.core;

public class Info<T> {
	
	private String action;
	private T data;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
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

	public static class XnY {
		private int x;
		private int y;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		@Override
		public String toString() {
			return "XnY [x=" + x + ", y=" + y + "]";
		}
		
	}
}
