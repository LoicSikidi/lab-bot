package test.hello.suribot.communication.mbc;

import static org.junit.Assert.assertFalse;

import org.json.JSONObject;
import org.junit.Test;

import hello.suribot.communication.mbc.NodeJsMBCSender;

public class NodeJsMBCSenderTest {
	
	@Test
	public void sendMessageTest(){
		NodeJsMBCSender sender = new NodeJsMBCSender();
		assertFalse(sender.sendMessage(null, null));
		
		sender = new NodeJsMBCSender();
		assertFalse(sender.sendMessage(new JSONObject(), ""));
	}
	

}