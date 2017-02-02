package test.hello.suribot.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.Test;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.MissingAnalyzerParam;
import hello.suribot.response.ResponseGenerator;

public class ResponseGeneratorTest{
	
	@Test
	public void getMessagesTest() {
		ResponseGenerator gen = new ResponseGenerator();
		assertNotNull(gen.getMessages());
	}

	@Test
	public void generateUnderstoodMessageTest() {
		ResponseGenerator gen = new ResponseGenerator();
		ResourceBundle messages = gen.getMessages();
		String message = messages.getString("internalErrorMessage");
		assertEquals(message, gen.generateUnderstoodMessage(null, null, false, null).getMessage());
		assertEquals(message, gen.generateUnderstoodMessage(IntentsAnalyzer.CONTRAT, null, false, null).getMessage());
	}
	
	@Test
	public void generateInternalErrorMessageTest() {
		ResponseGenerator gen = new ResponseGenerator();
		ResourceBundle messages = gen.getMessages();
		String message = messages.getString("internalErrorMessage");
		assertEquals(message, gen.generateInternalErrorMessage().getMessage());
	}

	@Test
	public void generateNotUnderstoodMessageTest() {
		ResponseGenerator gen = new ResponseGenerator();
		ResourceBundle messages = gen.getMessages();
		String message = messages.getString("notUnderstoodMessage");
		assertEquals(message, gen.generateNotUnderstoodMessage().getMessage());
	}
	
	@Test
	public void generateMessageButMissOneArgTest() {
		ResponseGenerator gen = new ResponseGenerator();
		ResourceBundle messages = gen.getMessages();
		assertEquals(messages.getString("notUnderstoodMessage"), gen.generateMessageButMissOneArg(null).getMessage());
		
		String message = messages.getString("missOneArg");
		assertTrue(gen.generateMessageButMissOneArg(MissingAnalyzerParam.billing).getMessage().startsWith(message));
	}
	
	@Test
	public void generateMessageButMissArgsTest() {
		ResponseGenerator gen = new ResponseGenerator();
		ResourceBundle messages = gen.getMessages();
		assertEquals(messages.getString("notUnderstoodMessage"), gen.generateMessageButMissArgs(null).getMessage());
		
		String message = messages.getString("missArgs");
		List<MissingAnalyzerParam> list = new ArrayList<>(Arrays.asList(
				new MissingAnalyzerParam[]{MissingAnalyzerParam.billing, MissingAnalyzerParam.couverture}));
		assertTrue(gen.generateMessageButMissArgs(list).getMessage().startsWith(message));
	}
	
}