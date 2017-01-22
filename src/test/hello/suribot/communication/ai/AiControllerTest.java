package test.hello.suribot.communication.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

import ai.api.AIServiceException;
import hello.suribot.communication.ai.AiController;

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
			controller.callApiAi(null, null, null);
		} catch (IllegalArgumentException | AIServiceException e) {
			assertTrue(true);
		}
//		TODO: trouver un moyen de faire connaitre à Travis les variables d'environnement pour les tests
//		try {
//			controller.callApiAi("message", EnvVar.TOKENAPIAI.getValue(), "fr");
//		} catch (AIServiceException e) {
//			assertTrue(true);
//		}
	}
	
	@Test
	public void callRecastTest() {
		AiController controller = new AiController();
		assertEquals(controller.callRecast(null, null, null).toString(), new JSONObject().toString());
//		TODO: trouver un moyen de faire connaitre à Travis les variables d'environnement pour les tests
//		assertEquals(controller.callRecast("", EnvVar.TOKENRECAST.getValue(), "fr").toString(), new JSONObject().toString());
//		assertNotEquals(controller.callRecast("message", EnvVar.TOKENRECAST.getValue().toString(), "fr"), new JSONObject().toString());
	}
	
}