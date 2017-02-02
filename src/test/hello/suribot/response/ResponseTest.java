package test.hello.suribot.response;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import hello.suribot.response.Response;

public class ResponseTest {
	
	private final String message = "message";
	private final String url = "message";
	private final List<String> choices = initChoices();

	@Test
	public void getMessageTest() {
		Response response = new Response(message, url, choices);
		assertEquals(response.getMessage(), message);
		
		response = new Response(message, url);
		assertEquals(response.getMessage(), message);
		
		response = new Response(message);
		assertEquals(response.getMessage(), message);
	}

	@Test
	public void setMessageTest() {
		Response response = new Response(message, url, choices);
		response.setMessage("test");
		assertEquals(response.getMessage(), "test");
		
		response = new Response(message, url);
		response.setMessage("test");
		assertEquals(response.getMessage(), "test");
		
		response = new Response(message);
		response.setMessage("test");
		assertEquals(response.getMessage(), "test");
	}
	
	@Test
	public void concatMessageAtTheBeginTest(){
		Response response = new Response(message, url, choices);
		response.concatMessageAtTheBegin("test");
		assertEquals(response.getMessage(), "test"+message);
		
		response = new Response(message, url);
		response.concatMessageAtTheBegin("test");
		assertEquals(response.getMessage(), "test"+message);
		
		response = new Response(message);
		response.concatMessageAtTheBegin("test");
		assertEquals(response.getMessage(), "test"+message);
		
	}
	
	@Test
	public void concatMessageAtTheEndTest(){
		Response response = new Response(message, url, choices);
		response.concatMessageAtTheEnd("test");
		assertEquals(response.getMessage(), message+"test");
		
		response = new Response(message, url);
		response.concatMessageAtTheEnd("test");
		assertEquals(response.getMessage(), message+"test");
		
		response = new Response(message);
		response.concatMessageAtTheEnd("test");
		assertEquals(response.getMessage(), message+"test");
	}

	@Test
	public void getUrlImageTest() {
		Response response = new Response(message, url, choices);
		assertEquals(response.getUrlImage(), url);
		
		response = new Response(message, url);
		assertEquals(response.getUrlImage(), url);
		
		response = new Response(message);
		assertNull(response.getUrlImage());
	}

	@Test
	public void setUrlImageTest() {
		Response response = new Response(message, url, choices);
		response.setUrlImage("test");
		assertEquals(response.getUrlImage(), "test");
		
		response = new Response(message, url);
		response.setUrlImage("test");
		assertEquals(response.getUrlImage(), "test");
		
		response = new Response(message);
		response.setUrlImage("test");
		assertEquals(response.getUrlImage(), "test");
	}

	@Test
	public void getListChoiceTest() {
		Response response = new Response(message, url, choices);
		assertEquals(response.getListChoice(), choices);
		
		response = new Response(message, url);
		assertNull(response.getListChoice());
		response.setListChoice(choices);
		assertEquals(response.getListChoice(), choices);
		
		response = new Response(message);
		assertNull(response.getListChoice());
	}

	@Test
	public void setListChoiceTest() {
		List<String> choicesTest = Arrays.asList(new String[]{"test1", "test2"});
		Response response = new Response(message, url, choices);
		assertEquals(response.getListChoice(), choices);
		response.setListChoice(choicesTest);
		assertEquals(response.getListChoice(), choicesTest);
		
		response = new Response(message, url);
		assertNull(response.getListChoice());
		response.setListChoice(choicesTest);
		assertEquals(response.getListChoice(), choicesTest);
		
		response = new Response(message);
		assertNull(response.getListChoice());
		response.setListChoice(choicesTest);
		assertEquals(response.getListChoice(), choicesTest);
	}
	
	@Test
	public void addChoiceTest(){
		Response response = new Response(message, url, choices);
		int sizeBefore = choices.size();
		assertEquals(response.getListChoice(), choices);
		assertEquals(response.getListChoice().size(), sizeBefore);
		response.addChoice("addChoice");
		assertEquals(response.getListChoice().size(), sizeBefore+1);
		
		response = new Response(message, url);
		assertNull(response.getListChoice());
		response.addChoice("addChoice");
		assertEquals(response.getListChoice().size(), 1);
		
		response = new Response(message);
		assertNull(response.getListChoice());
		response.addChoice("addChoice");
		assertEquals(response.getListChoice().size(), 1);
	}
	
	private List<String> initChoices() {
		List<String> choices = new ArrayList<>();
		choices.add("choices1");
		choices.add("choices2");
		return choices;
	}
}
