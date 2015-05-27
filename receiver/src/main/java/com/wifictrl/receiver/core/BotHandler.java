package com.wifictrl.receiver.core;

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
	
	@Override
	public void handle(byte[] stream) throws IOException{
		List<Info<?>> events = mapper.readValue(stream, new TypeReference<List<Info<?>>>() {});
		log.debug("Recived: "+events);
		for(Info<?> info : events){
			if(Constants.MOUSE_MOVE.equals(info.getAction())){
				@SuppressWarnings("unchecked")
				List<Integer> xny = (List<Integer>)info.getData();
			}else if(Constants.MOUSE_RELEASED.equals(info.getAction())){
				Integer mouseButton = (Integer)info.getData();
			}else if(Constants.KEY_RELEASED.equals(info.getAction())){
				Integer keyCode = (Integer)info.getData();
			}else{
				throw new RuntimeException(info.getAction()+" not supported in receiver");
			}
		}
	}

}
