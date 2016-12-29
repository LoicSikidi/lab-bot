package test.hello.suribot.response.contracts;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import hello.suribot.response.contracts.ContractResponseGenerator;

public class ContractResponseGeneratorTest {
	
	@Test
	public void extractBillingsChoiceTest(){
		Locale aLocale = new Locale("fr","");
		ResourceBundle messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", aLocale);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{ \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-96268885\"},"
				+ "	 { \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-2355596664\"},"
				+ "  { \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-123987456\"}]";
		assertEquals("[prélèvement 96268885, prélèvement 2355596664, prélèvement 123987456]", Arrays.toString(generator.extractBillingsChoice(s)));
	}
	
	@Test
	public void extractBillingInfoTest(){
		Locale aLocale = new Locale("fr","");
		ResourceBundle messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", aLocale);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"methode\": \"cheque\",   \"amount\": 542.97,   \"identifiant\": \"123987456\",   \"frequency\": \"hebdomadaire\",   \"next_date\": \"2017-11-10\" }";
		assertEquals("[methode : cheque, amount : 542.97, identifiant : 123987456, frequency : hebdomadaire, next_date : 2017-11-10]", 
				Arrays.toString(generator.extractBillingInfos(s)));
	}
	
	@Test
	public void extractPartyRolesChoiceTest(){
		Locale aLocale = new Locale("fr","");
		ResourceBundle messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", aLocale);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee78595\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee787634\"}]";
		assertEquals("[personne eee78595, personne eee787634]", Arrays.toString(generator.extractPartyRolesChoice(s)));
	}
	
	@Test
	public void extractPartyRoleInfosTest(){
		Locale aLocale = new Locale("fr","");
		ResourceBundle messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", aLocale);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"end_date\": \"2016-12-25\",   \"person\": {     \"client_number\": \"7596055\",     \"birth_date\": \"1994-12-05\",     \"last_name\": \"dupuit\",     \"postal_code\": \"75005\",     \"first_name\": \"eric\"   },   \"identifiant\": \"eee787634\",   \"type\": \"owner\" }";
		assertEquals("[end_date : 2016-12-25, identifiant : eee787634, type : owner, person_client_number : 7596055, person_birth_date : 1994-12-05, person_last_name : dupuit, person_postal_code : 75005, person_first_name : eric]", 
				Arrays.toString(generator.extractPartyRoleInfos(s)));
	}
	
	@Test
	public void extractRiskChoiceTest(){
		Locale aLocale = new Locale("fr","");
		ResourceBundle messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", aLocale);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "[{\"rel\": \"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/risk/ID-02453620\"},"
				+ "  {\"rel\":\"self\",\"href\": \"http://localhost:12347/insurance/contract/ID-5935697/risk/ID-8944710\"}]";
		assertEquals("[personne 02453620, personne 8944710]", Arrays.toString(generator.extractPartyRolesChoice(s)));
	}
	
	@Test
	public void extractRisksInfosTest(){
		Locale aLocale = new Locale("fr","");
		ResourceBundle messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", aLocale);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"incendie\": false,   \"vandalisme\": true,   \"inondation\": true }";
		assertEquals("[incendie : false, vandalisme : true, inondation : true]", Arrays.toString(generator.extractRisksInfos(s)));
	}
	
}
