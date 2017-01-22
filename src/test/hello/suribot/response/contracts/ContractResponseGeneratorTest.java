package test.hello.suribot.response.contracts;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.ResourceBundle;

import org.junit.Test;

import hello.suribot.response.MessagesResponses;
import hello.suribot.response.ResponseGenerator;
import hello.suribot.response.contracts.ContractResponseGenerator;

public class ContractResponseGeneratorTest {
	
	@Test
	public void extractBillingsChoiceTest(){
		ResourceBundle messages = new ResponseGenerator().getMessages();
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{ \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-96268885\"},"
				+ "	 { \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-2355596664\"},"
				+ "  { \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-123987456\"}]";
		assertEquals("[prélèvement 96268885, prélèvement 2355596664, prélèvement 123987456]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.billingsChoice, s)));
	}
	
	@Test
	public void extractPartyRolesChoiceTest(){
		ResourceBundle messages = new ResponseGenerator().getMessages();
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee78595\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee787634\"}]";
		assertEquals("[personne eee78595, personne eee787634]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.partyRolesChoice, s)));
	}
	
	@Test
	public void extractRiskChoiceTest(){
		ResourceBundle messages = new ResponseGenerator().getMessages();
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/risk/ID-02453620\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/risk/ID-8944710\"}]";
		assertEquals("[objet 02453620, objet 8944710]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.risksChoice, s)));
	}
	
}
