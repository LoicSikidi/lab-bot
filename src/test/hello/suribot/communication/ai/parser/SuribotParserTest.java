package test.hello.suribot.communication.ai.parser;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import ai.api.AIServiceException;
import hello.suribot.communication.ai.AiController;
import hello.suribot.communication.ai.parser.SuribotParser;
import hello.suribot.utils.EnvVar;

public class SuribotParserTest {
	
	@Test
	public void parseApiAiTest(){
		SuribotParser parser = new SuribotParser();
		assertNull(parser.parseApiAi(null));
		try {
			AiController controller = new AiController();
			controller.callApiAi("", EnvVar.TOKENAPIAI.getValue(), null);
		} catch (AIServiceException e) {
			e.printStackTrace();
		} catch (IllegalStateException ise){
			assertTrue(true);
		}
	}
	
	@Test
	public void parseRecastTest(){
		SuribotParser parser = new SuribotParser();
		assertNull(parser.parseRecast(null));
		AiController controller = new AiController();
		JSONObject resp = controller.callRecast("", EnvVar.TOKENRECAST.getValue(), null);
		assertNull(parser.parseRecast(resp));
	}
	
}
