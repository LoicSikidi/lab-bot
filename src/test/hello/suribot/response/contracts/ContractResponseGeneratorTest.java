package test.hello.suribot.response.contracts;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import hello.suribot.response.contracts.ContractResponseGenerator;

public class ContractResponseGeneratorTest {
	
	@Test
	public void extractBillingsChoiceTest(){
		ContractResponseGenerator generator = new ContractResponseGenerator();
		String s = "[{ \"link\":null,\"links\":[{ \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-96268885\"}] },{ \"link\":null,\"links\":[{ \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-2355596664\"}]},{ \"link\":null,\"links\":[{ \"rel\":\"self\",\"href\":\"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-123987456\"}]}]";
		assertEquals("[ID-96268885, ID-2355596664, ID-123987456]", Arrays.toString(generator.extractBillingsChoice(s)));
	}
	
	@Test
	public void extractBillingInfoTest(){
		ContractResponseGenerator generator = new ContractResponseGenerator();
		String s = "{   \"methode\": \"cheque\",   \"amount\": 542.97,   \"identifiant\": \"123987456\",   \"frequency\": \"hebdomadaire\",   \"next_date\": \"2017-11-10\" }";
		assertEquals("[methode : cheque, amount : 542.97, identifiant : 123987456, frequency : hebdomadaire, next_date : 2017-11-10]", 
				Arrays.toString(generator.extractBillingInfos(s)));
	}
	
	@Test
	public void extractPartyRolesChoiceTest(){
		ContractResponseGenerator generator = new ContractResponseGenerator();
		String s = "[   {     \"link\": null,     \"links\": [{       \"rel\": \"self\",       \"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee78595\"     }]   },   {     \"link\": null,     \"links\": [{       \"rel\": \"self\",       \"href\": \"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee787634\"     }]   } ]";
		assertEquals("[ID-eee78595, ID-eee787634]", Arrays.toString(generator.extractPartyRolesChoice(s)));
	}
	
	@Test
	public void extractPartyRoleInfosTest(){
		ContractResponseGenerator generator = new ContractResponseGenerator();
		String s = "{   \"end_date\": \"2016-12-25\",   \"person\": {     \"client_number\": \"7596055\",     \"birth_date\": \"1994-12-05\",     \"last_name\": \"dupuit\",     \"postal_code\": \"75005\",     \"first_name\": \"eric\"   },   \"identifiant\": \"eee787634\",   \"type\": \"owner\" }";
		assertEquals("[end_date : 2016-12-25, identifiant : eee787634, type : owner, person_client_number : 7596055, person_birth_date : 1994-12-05, person_last_name : dupuit, person_postal_code : 75005, person_first_name : eric]", 
				Arrays.toString(generator.extractPartyRoleInfos(s)));
	}
	
	@Test
	public void extractRisksInfosTest(){
		ContractResponseGenerator generator = new ContractResponseGenerator();
		String s = "{   \"incendie\": false,   \"vandalisme\": true,   \"inondation\": true }";
		assertEquals("[incendie : false, vandalisme : true, inondation : true]", Arrays.toString(generator.extractRisksInfos(s)));
	}
	
}
