package com.wifictrl.receiver.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;
import com.wifictrl.common.core.SerializeData;
import com.wifictrl.sender.core.DatagramManager;

public class BotHandlerTest {

	@Mock
	private Handler handler;
	private final Logger log = LogManager.getLogger();
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void sample() throws IOException, InterruptedException, ClassNotFoundException{
		Info<Integer[]> i1 = new Info<>();
		i1.setAction(1);
		i1.setData(new Integer[]{45,78,564,7876,334});
		final byte[] data = new byte[Constants.PACKET_SIZE];
		final byte[] src = SerializeData.toBytes(i1);
		System.arraycopy(src, 0, data, 0, src.length);
		final int ACTIONS = 10;
		final CountDownLatch actions = new CountDownLatch(ACTIONS);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				try{
					byte[] received = (byte[]) invocation.getArguments()[0];
					log.debug("Received: "+Arrays.toString(received));
					assertEquals(Arrays.toString(data), Arrays.toString(received));
				}finally{
					actions.countDown();
				}
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
