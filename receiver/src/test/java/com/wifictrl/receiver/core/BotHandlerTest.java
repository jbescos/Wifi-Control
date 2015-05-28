package com.wifictrl.receiver.core;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;
import com.wifictrl.sender.core.DatagramManager;

public class BotHandlerTest {

	@Mock
	private Handler handler;
	private final ObjectMapper mapper = new ObjectMapper();
	private List<Info<?>> dataSample;
	private final Logger log = LogManager.getLogger();
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		dataSample = new ArrayList<Info<?>>();
		Info<Integer[]> i1 = new Info<>();
		i1.setAction("test1");
		i1.setData(new Integer[]{45,78,564,7876,334});
		dataSample.add(i1);
		Info<String> i2 = new Info<>();
		i2.setAction("test2");
		i2.setData("any content");
		dataSample.add(i2);
	}
	
	@Test
	public void sample() throws IOException, InterruptedException{
		final byte[] data = new byte[Constants.PACKET_SIZE];
		final byte[] src = mapper.writeValueAsBytes(dataSample);
		System.arraycopy(src, 0, data, 0, src.length);
		final int ACTIONS = 10;
		final CountDownLatch actions = new CountDownLatch(ACTIONS);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				byte[] received = (byte[]) invocation.getArguments()[0];
				log.debug("Received: "+Arrays.toString(received));
				Arrays.equals(data, received);
				List<Info<?>> fromJson = mapper.readValue(received, new TypeReference<List<Info<?>>>() {});
				Arrays.equals(dataSample.toArray(new Info<?>[0]), fromJson.toArray(new Info<?>[0]));
				actions.countDown();
				return null;
			}
		}).when(handler).handle((byte[]) anyObject());
		new Thread(new Receiver(handler), "test-thread").start();
		try(DatagramManager manager = new DatagramManager("localhost");){
			for(int i=0;i<ACTIONS;i++){
				manager.send(data);
				log.debug("Sended");
			}
		}
		actions.await();
		verify(handler, times(ACTIONS)).handle(data);
	}
	
}
