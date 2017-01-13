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
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{ \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-96268885\"},"
				+ "	 { \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-2355596664\"},"
				+ "  { \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-123987456\"}]";
		assertEquals("[prélèvement 96268885, prélèvement 2355596664, prélèvement 123987456]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.billingsChoice, s)));
	}
	
	@Test
	public void extractBillingInfoTest(){
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"methode\": \"cheque\",   \"amount\": 542.97,   \"identifiant\": \"123987456\",   \"frequency\": \"hebdomadaire\",   \"next_date\": \"2017-11-10\" }";
		assertEquals("methode : cheque\namount : 542.97\nidentifiant : 123987456\nfrequency : hebdomadaire\nnext_date : 2017-11-10\n", 
				(generator.extractBillingInfos(s).getMessage()));
	}
	
	@Test
	public void extractPartyRolesChoiceTest(){
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee78595\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee787634\"}]";
		assertEquals("[personne eee78595, personne eee787634]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.partyRolesChoice, s)));
	}
	
	@Test
	public void extractPartyRoleInfosTest(){
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"end_date\": \"2016-12-25\",   \"person\": {     \"client_number\": \"7596055\",     \"birth_date\": \"1994-12-05\",     \"last_name\": \"dupuit\",     \"postal_code\": \"75005\",     \"first_name\": \"eric\"   },   \"identifiant\": \"eee787634\",   \"type\": \"owner\" }";
		assertEquals("end_date : 2016-12-25\nperson_client_number : 7596055\nperson_birth_date : 1994-12-05\nperson_last_name : dupuit\nperson_postal_code : 75005\nperson_first_name : eric\nidentifiant : eee787634\ntype : owner\n", 
				generator.extractPartyRoleInfos(s).getMessage());
	}
	
	@Test
	public void extractRiskChoiceTest(){
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/risk/ID-02453620\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/risk/ID-8944710\"}]";
		assertEquals("[objet 02453620, objet 8944710]", 
				Arrays.toString(generator.extractChoice(MessagesResponses.risksChoice, s)));
	}
	
	@Test
	public void extractRisksInfosTest(){
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"incendie\": false,   \"vandalisme\": true,   \"inondation\": true }";
		assertEquals("incendie : false\nvandalisme : true\ninondation : true\n", generator.extractRisksInfos(s).getMessage());
	}
	
}
