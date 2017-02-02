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
		String s = "[{ \"rel\":\"self\",\"href\":\"http://localhost:PORT/insurance/contract/ID-5935697/billings/ID-96268885\"},"
				+ "	 { \"rel\":\"self\",\"href\":\"http://localhost:PORT/insurance/contract/ID-5935697/billings/ID-2355596664\"},"
				+ "  { \"rel\":\"self\",\"href\":\"http://localhost:PORT/insurance/contract/ID-5935697/billings/ID-123987456\"}]";
		assertEquals("[prélèvement n°96268885, prélèvement n°2355596664, prélèvement n°123987456]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.billingsChoice, s)));
	}
	
	@Test
	public void extractPartyRolesChoiceTest(){
		ResourceBundle messages = new ResponseGenerator().getMessages();
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:PORT/insurance/contract/ID-891135/partyRoles/ID-78595:::Thomas%20Dupont\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12346/insurance/contract/ID-891135/partyRoles/ID-787634:::Mikael%20Gibert\"}]";
		assertEquals("[personne n°78595:::Thomas%20Dupont, personne n°787634:::Mikael%20Gibert]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.partyRolesChoice, s)));
	}
	
	@Test
	public void extractRiskChoiceTest(){
		ResourceBundle messages = new ResponseGenerator().getMessages();
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:PORT/insurance/contract/ID-5935697/risk/ID-02453620\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:PORT/insurance/contract/ID-5935697/risk/ID-8944710\"}]";
		assertEquals("[objet n°02453620, objet n°8944710]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.risksChoice, s)));
	}
	
}
