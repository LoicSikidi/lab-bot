package test.hello.suribot.communication.botConnector.mbc;

import static org.junit.Assert.assertFalse;

import org.json.JSONObject;
import org.junit.Test;

import hello.suribot.communication.botConnector.mbc.NodeJsMBCSender;
import hello.suribot.response.Response;

public class NodeJsMBCSenderTest {
	
	@Test
	public void sendMessageTest(){
		NodeJsMBCSender sender = new NodeJsMBCSender();
		assertFalse(sender.sendMessage(null, null));
		
		sender = new NodeJsMBCSender();
		assertFalse(sender.sendMessage(new JSONObject(), new Response("")));
	}
	

}