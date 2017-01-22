package test.hello.suribot.communication.ai;

import static org.junit.Assert.*;
import org.json.JSONObject;
import org.junit.Test;

import ai.api.AIServiceException;
import hello.suribot.communication.ai.AiController;
import hello.suribot.utils.EnvVar;

public class AiControllerTest {
	
	@Test
	public void sendMessagedTest(){
		AiController controller = new AiController();
		assertFalse(controller.sendMessage(null, null, null));
	}
	
	@Test
	public void callApiAiTest() {
		AiController controller = new AiController();
		try {
			assertNull(controller.callApiAi(null, null, null));
		} catch (IllegalArgumentException | AIServiceException e) {
			assertTrue(true);
		}
		try {
			assertNotNull(controller.callApiAi("message", EnvVar.TOKENAPIAI.getValue(), "fr"));
		} catch (AIServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void callRecastTest() {
		AiController controller = new AiController();
		assertEquals(controller.callRecast(null, null, null).toString(), new JSONObject().toString());
		assertEquals(controller.callRecast("", EnvVar.TOKENRECAST.getValue(), "fr").toString(), new JSONObject().toString());
		assertNotEquals(controller.callRecast("message", EnvVar.TOKENRECAST.getValue(), "fr").toString(), new JSONObject().toString());
	}
	
}