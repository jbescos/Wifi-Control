package com.wifictrl.common.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class SerializeDataTest {

	@Test
	public void serializeDeserialize() throws IOException {
		Info<Integer[]> infoSrc = new Info<Integer[]>();
		infoSrc.setAction(1);
		infoSrc.setData(new Integer[]{2,3});
		byte[] data = SerializeData.toBytes(infoSrc);
		@SuppressWarnings("unchecked")
		Info<List<Integer>> infoTrg = SerializeData.toObject(data, Info.class);
		assertEquals(infoSrc.getAction(), infoTrg.getAction());
		assertEquals(infoSrc.getData()[0], infoTrg.getData().get(0));
		assertEquals(infoSrc.getData()[1], infoTrg.getData().get(1));
	}
	
}
