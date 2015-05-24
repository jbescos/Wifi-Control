package com.wifictrl.sender.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

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

import com.wifictrl.sender.core.Info;

public class BufferSenderTest {

	private final Logger log = LogManager.getLogger();
	private final ObjectMapper mapper = new ObjectMapper();
	private BufferSender sender;
	@Mock
	private DatagramManager manager;
	private volatile int elementsSent;
	
	@Before
	public void before() throws IOException{
		elementsSent = 0;
		MockitoAnnotations.initMocks(this);
		sender = new BufferSender(manager, 5L);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				byte[] content = (byte[]) invocation.getArguments()[0];
				List<Info<?>> sent = mapper.readValue(content, new TypeReference<List<Info<?>>>() {});
				elementsSent = elementsSent + sent.size();
				return null;
			}
		}).when(manager).send((byte[]) anyObject());
	}
	
	@Test
	public void concurrence() throws IOException, InterruptedException{
		sender.start();
		Info<?> info = new Info<>();
		final int TASKS = 1000;
		for(int i=0; i<TASKS ;i++){
			sender.send(info);
			sender.send(info);
			sender.send(info);
			sender.send(info);
			Thread.sleep(1L);
		}
		sender.stop();
		verify(manager, atLeast(1)).send((byte[]) anyObject());
		log.debug("Sent: "+elementsSent);
		assertEquals(TASKS*4, elementsSent);
	}
	
}
