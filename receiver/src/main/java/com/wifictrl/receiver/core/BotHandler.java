package com.wifictrl.receiver.core;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;

public class BotHandler implements Handler{

	private final ObjectMapper mapper = new ObjectMapper();
	private final Logger log = LogManager.getLogger();
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final Robot bot;
	
	public BotHandler() throws AWTException{
		bot = new Robot();
	}
	
	@Override
	public void handle(byte[] stream) throws IOException{
		List<Info<?>> events = mapper.readValue(stream, new TypeReference<List<Info<?>>>() {});
		log.debug("Recived: "+events);
		for(Info<?> info : events){
			if(Constants.MOUSE_MOVE == info.getAction()){
				@SuppressWarnings("unchecked")
				List<Integer> xny = (List<Integer>)info.getData();
				int x = actualPosition(screenSize.width, xny.get(2), xny.get(0));
				int y = actualPosition(screenSize.height, xny.get(3), xny.get(1));
				bot.mouseMove(x, y);
			}else if(Constants.MOUSE_RELEASED == info.getAction()){
				Integer mouseButton = (Integer)info.getData();
				bot.mouseRelease(mouseButton);
			}else if(Constants.MOUSE_PRESSED == info.getAction()){
				Integer mouseButton = (Integer)info.getData();
				bot.mousePress(mouseButton);
			}else if(Constants.KEY_RELEASED == info.getAction()){
				Integer keyCode = (Integer)info.getData();
				bot.keyRelease(keyCode);
			}else if(Constants.MOUSE_WHEEL == info.getAction()){
				Integer scroll = (Integer)info.getData();
				bot.mouseWheel(scroll);
			}else{
				throw new RuntimeException(info.getAction()+" not supported in receiver");
			}
		}
	}
	
	private int actualPosition(int actualSize, int foreignSize, int foreignPosition){
		return foreignPosition * actualSize / foreignSize;
	}

}
