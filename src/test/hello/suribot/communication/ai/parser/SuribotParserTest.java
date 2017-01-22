package test.hello.suribot.communication.ai.parser;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import hello.suribot.communication.ai.parser.SuribotParser;

public class SuribotParserTest {
	
	@Test
	public void parseApiAiTest(){
		SuribotParser parser = new SuribotParser();
		assertNull(parser.parseApiAi(null));
//		TODO: trouver un moyen de faire connaitre à Travis les variables d'environnement pour les tests
//		try {
//			AiController controller = new AiController();
//			controller.callApiAi("", EnvVar.TOKENAPIAI.getValue(), null);
//		} catch (AIServiceException | IllegalStateException ise){
//			assertTrue(true);
//		}
	}
	
	@Test
	public void parseRecastTest(){
		SuribotParser parser = new SuribotParser();
		assertNull(parser.parseRecast(null));
//		TODO: trouver un moyen de faire connaitre à Travis les variables d'environnement pour les tests
//		AiController controller = new AiController();
//		JSONObject resp = controller.callRecast("", EnvVar.TOKENRECAST.getValue(), null);
//		assertNull(parser.parseRecast(resp));
	}
	
}
