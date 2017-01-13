package hello.suribot.response.contracts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.contracts.ContractParams;
import hello.suribot.response.MessagesResponses;
import hello.suribot.response.Response;

/**
 * Classe permettant la génération de message à l'utilisateur.
 */
public class ContractResponseGenerator implements IContractResponseGenerator {

	ResourceBundle messages;

	public ContractResponseGenerator(ResourceBundle messages){
		this.messages = messages;
	}
	
	public Response generateContractMessage(String calledMethod, boolean choice, String params) throws IllegalArgumentException {
		ContractParams param = ContractParams.valueOf(calledMethod);
		
		switch (param) {
		case risk:
			if(choice) return generateChoiceResponse(MessagesResponses.risksChoiceResponse, params);
			return generateInfosResponse(MessagesResponses.risksInfosResponse, params);
			
		case role:
			if(choice) return generateChoiceResponse(MessagesResponses.partyRolesChoiceResponse, params);
			return generateInfosResponse(MessagesResponses.partyRoleInfosResponse, params);
			
		case prelevement:
			if(choice) return generateChoiceResponse(MessagesResponses.billingsChoiceResponse, params);
			return generateInfosResponse(MessagesResponses.billingInfosResponse, params);
		
		default:
			throw new IllegalArgumentException(calledMethod + "not a "+ContractParams.class.getName()) ;
		}
		
	}

	/* (non-Javadoc)
	 * @see hello.suribot.response.contracts.IContractResponseGenerator#generateChoiceResponse(hello.suribot.response.MessagesResponses, java.lang.String)
	 */
	public Response generateChoiceResponse(MessagesResponses key, String params) throws MissingResourceException {
		if(key == null || params == null || params.isEmpty()) return null;
		String response = messages.getString(key.toString());
		
		switch (key) {
		case billingsChoiceResponse:
			key = MessagesResponses.billingsChoice;
			break;
			
		case risksChoiceResponse:
			key = MessagesResponses.risksChoice;
			break;
			
		case partyRolesChoiceResponse:
			key = MessagesResponses.partyRolesChoice;
			break;

		default:
			return null;
		}
		
		List<String> listChoice = new ArrayList<>();
		for(String str : extractChoice(key, params)){
			listChoice.add(str);
		}
		return new Response(response, listChoice);
	}

	/* (non-Javadoc)
	 * @see hello.suribot.response.contracts.IContractResponseGenerator#generateInfosResponse(hello.suribot.response.MessagesResponses, java.lang.String)
	 */
	public Response generateInfosResponse(MessagesResponses key, String params) throws MissingResourceException {
		if(key == null || params == null || params.isEmpty()) return null;
		String response = messages.getString(key.toString());
		
		for(String str : extractInfos(key, params)){
			response += str+"\n";
		}
		return new Response(response);
	}

	
	/**	Pour billings : 
	 *<br>
	 * [
	 *	    { "rel":"self","href":"http://localhost:PORT/insurance/contract/ID-5935697/billings/ID-96268885"},
	 *		{ "rel":"self","href":"http://localhost:PORT/insurance/contract/ID-5935697/billings/ID-2355596664"},
	 *		{ "rel":"self","href":"http://localhost:PORT/insurance/contract/ID-5935697/billings/ID-123987456"}
	 *	] 
	 *<br>
	 *  Pour partyRoles : 
	 *<br>
	 *  [
	 * 	{"rel":"self","href":"http://localhost:PORT/insurance/contract/ID-5935697/partyRoles/ID-eee78595"},
	 * 	{"rel":"self","href":"http://localhost:PORT/insurance/contract/ID-5935697/partyRoles/ID-eee787634"}
	 *  ]
	 *<br>  
	 *  Pour risks :
	 *<br>	
	 *  [
	 * 		{"rel":"self","href":"http://localhost:PORT/insurance/contract/ID-64767/risk/ID-kockeo/couverture"},
	 * 		{"rel":"self","href":"http://localhost:PORT/insurance/contract/ID-64767/risk/ID-kockeo/couverture"},
	 * 		{"rel":"self","href":"http://localhost:PORT/insurance/contract/ID-64767/risk/ID-52665236/couverture"}
	 * ]
	 *  
	 **/	
	public String[] extractChoice(MessagesResponses key, String choices) throws JSONException, MissingResourceException{
		if(key == null || choices == null || choices.isEmpty()) return null;
		JSONArray array = new JSONArray(choices);
		String[] results = new String[array.length()];

		for(int i = 0; i<array.length(); i++){
			results[i] = array.getJSONObject(i).get("href").toString().split("/")[7].replace("ID-", messages.getString(key.toString()));
		}

		return results;
	}
	
	
	private String[] extractInfos(MessagesResponses key, String params) throws JSONException, MissingResourceException {
		if(key == null) return null;

		switch (key) {
		case billingInfosResponse:
			return extractBillingInfos(params);
		case partyRoleInfosResponse:
			return extractPartyRoleInfos(params);
		case risksInfosResponse:
			return extractRisksInfos(params);

		default:
			return null;
		}
	}
	
	
	/**
	 * { "methode": "cheque",   
	 * "amount": 542.97,   
	 * "identifiant": "123987456",   
	 * "frequency": "hebdomadaire",  
	 * "next_date": "2017-11-10" }
	 */
	public String[] extractBillingInfos(String billing) throws JSONException {
		if(billing == null || billing.isEmpty()) return null;
		JSONObject obj = new JSONObject(billing);
		String[] results = new String[obj.length()];

		int cpt = 0;
		for(String key : obj.keySet()){
			results[cpt] = key + " : " +obj.get(key).toString();
			cpt++;
		}

		return results;
	}


	/**
	 * {
	 * 	"end_date":"2016-12-25",
	 * 	"person":{ "client_number":"7596055","birth_date":"1994-12-05","last_name":"dupuit","postal_code":"75005","first_name":"eric"},
	 *   "identifiant":"eee787634",
	 *   "type":"owner"
	 * }
	 */
	public String[] extractPartyRoleInfos(String role) throws JSONException {
		if(role == null || role.isEmpty()) return null;
		JSONObject obj = new JSONObject(role);
		String[] results = new String[obj.length()-1];
		String[] person_results = null;

		int cpt = 0;
		for(String key : obj.keySet()){

			if(key.equals("person")){
				JSONObject person = obj.getJSONObject(key);
				person_results = new String[person.length()];
				int cpt_person = 0;
				for(String person_key : person.keySet()){
					person_results[cpt_person] = "person_"+person_key+" : "+person.get(person_key).toString();
					cpt_person++;
				}
			} else {
				results[cpt] = key + " : " +obj.get(key).toString();
				cpt++;
			}
		}
		if(person_results==null) return results;
		return Stream.concat(Arrays.stream(results), Arrays.stream(person_results)) .toArray(String[]::new);
	}

	/**
	 * {   
	 * "incendie": false,   
	 * "vandalisme": true,   
	 * "inondation": true 
	 * }
	 */
	public String[] extractRisksInfos(String risks) throws JSONException {
		if(risks == null || risks.isEmpty()) return null;
		JSONObject obj = new JSONObject(risks);
		String[] results = new String[obj.length()];

		int cpt = 0;
		for(String key : obj.keySet()){
			results[cpt] = key + " : " +obj.get(key).toString();
			cpt++;
		}

		return results;
	}

}
