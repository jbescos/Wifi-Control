package com.wifictrl.receiver.core;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;
import com.wifictrl.common.core.SerializeData;

public class BotHandler implements Handler{
	
	private final Logger log = LogManager.getLogger();
	private final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width -1;
	private final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height -1;
	private final Robot bot;
	
	public BotHandler() throws AWTException{
		bot = new Robot();
	}
	
	@Override
	public void handle(byte[] stream) throws IOException{
		Info<?> info = SerializeData.toObject(stream, Info.class);
		if(Constants.MOUSE_MOVE == info.getAction()){
			@SuppressWarnings("unchecked")
			List<Integer> xny = (List<Integer>) info.getData();
			int x = actualPosition(WIDTH, xny.get(2), xny.get(0));
			int y = actualPosition(HEIGHT, xny.get(3), xny.get(1));
			log.debug("Mouse move ("+x+","+y+")");
			bot.mouseMove(x, y);
		}else if(Constants.MOUSE_RELEASED == info.getAction()){
			Integer mouseButton = (Integer)info.getData();
			log.debug("Mouse "+mouseButton+" released");
			bot.mouseRelease(mouseButton);
		}else if(Constants.MOUSE_PRESSED == info.getAction()){
			Integer mouseButton = (Integer)info.getData();
			log.debug("Mouse "+mouseButton+" pressed");
			bot.mousePress(mouseButton);
		}else if(Constants.KEY_RELEASED == info.getAction()){
			Integer keyCode = (Integer)info.getData();
			log.debug("Key "+keyCode+" released");
			bot.keyRelease(keyCode);
		}else if(Constants.KEY_PRESSED == info.getAction()){
			Integer keyCode = (Integer)info.getData();
			log.debug("Key "+keyCode+" pressed");
			bot.keyPress(keyCode);
		}else if(Constants.MOUSE_WHEEL == info.getAction()){
			Integer scroll = (Integer)info.getData();
			log.debug("Wheel "+scroll);
			bot.mouseWheel(scroll);
		}else{
			throw new RuntimeException(info.getAction()+" not supported in receiver");
		}
	}
	
	private int actualPosition(int actualSize, int foreignSize, int foreignPosition){
		return foreignPosition * actualSize / foreignSize;
	}

}
