package com.wifictrl.receiver.core;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;
import com.wifictrl.common.core.SerializeData;

public class BotHandler implements Handler{
	
	private final Logger log = LogManager.getLogger();
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final Robot bot;
	
	public BotHandler() throws AWTException{
		bot = new Robot();
	}
	
	@Override
	public void handle(byte[] stream) throws IOException, ClassNotFoundException{
		Info<?> info = SerializeData.toObject(stream);
		if(Constants.MOUSE_MOVE == info.getAction()){
			Integer[] xny = (Integer[]) info.getData();
			int x = actualPosition(screenSize.width, xny[2], xny[0]);
			int y = actualPosition(screenSize.height, xny[3], xny[1]);
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
