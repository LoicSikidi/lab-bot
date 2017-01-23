package test.hello.suribot.communication.botConnector.rbc;

import static org.junit.Assert.assertFalse;

import org.json.JSONObject;
import org.junit.Test;

import hello.suribot.communication.botConnector.rbc.RecastBotConnectorSender;

public class RecastBotConnectorSenderTest {
	
	@Test
	public void sendMessageTest(){
		RecastBotConnectorSender sender = new RecastBotConnectorSender();
		assertFalse(sender.sendMessage(null, null));
		
		sender = new RecastBotConnectorSender();
		assertFalse(sender.sendMessage(new JSONObject(), null));
	}
	
}