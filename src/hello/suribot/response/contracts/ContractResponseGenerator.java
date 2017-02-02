package hello.suribot.response.contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
	
	/* (non-Javadoc)
	 * @see hello.suribot.response.contracts.IContractResponseGenerator#generateContractMessage(java.lang.String, boolean, java.lang.String)
	 */
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
			
		case rib:
			return generateInfosResponse(MessagesResponses.ribInfosResponse, params);
		
		default:
			throw new IllegalArgumentException(calledMethod + "not a "+ContractParams.class.getName()) ;
		}
		
	}

	/**
	 * Retourne la réponse concernant un choix à fournir à partir de la {@link MessagesResponses} et des paramètres à associés.
	 * @param key
	 * @param params
	 * @return la {@link Response}
	 * @throws MissingResourceException
	 */
	private Response generateChoiceResponse(MessagesResponses key, String params) throws MissingResourceException {
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

	/**
	 * Retourne la réponse concernant une information à fournir à partir de la {@link MessagesResponses} et des paramètres à associés.
	 * @param key
	 * @param params
	 * @return la {@link Response}
	 * @throws MissingResourceException
	 */
	private Response generateInfosResponse(MessagesResponses key, String params) throws MissingResourceException {
		if(key == null || params == null || params.isEmpty()) return null;
		String mess = messages.getString(key.toString());
		
		Response response = extractInfos(key, params);
		response.concatMessageAtTheBegin(mess);
		return response;
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
	
	/**
	 * Appelle les bonnes méthodes d'extractions suivant la clé {@link MessagesResponses}, et retourne leur résultat, null sinon.
	 * @param key
	 * @param params
	 * @return
	 * @throws JSONException
	 * @throws MissingResourceException
	 */
	private Response extractInfos(MessagesResponses key, String params) throws JSONException, MissingResourceException {
		if(key == null) return null;

		switch (key) {
		case billingInfosResponse:
			return extractBillingInfos(params);
		case partyRoleInfosResponse:
			return extractPartyRoleInfos(params);
		case risksInfosResponse:
			return extractRisksInfos(params);
		case ribInfosResponse:
			return extractRibInfos(params);

		default:
			return null;
		}
	}
	
	/** { 
	 * "methode": "cheque",   
	 * "amount": 542.97,   
	 * "identifiant": "123987456",   
	 * "frequency": "hebdomadaire",  
	 * "next_date": "2017-11-10" 
	 * } */
	private Response extractBillingInfos(String billing) throws JSONException {
		if(billing == null || billing.isEmpty()) return null;
		JSONObject obj = new JSONObject(billing);
		String response ="";
		for(String key : obj.keySet()){
			response += adaptInfo(key, obj.get(key).toString());
		}
		return new Response(response);
	}

	/** {
	 * 	"end_date":"2016-12-25",
	 *  "image":"http://www.suricats-consulting.com/wp-content/uploads/2016/05/A1-01-150x150.png",
	 * 	"person":{ "client_number":"7596055","birth_date":"1994-12-05","last_name":"dupuit","postal_code":"75005","first_name":"eric"},
	 *  "identifiant":"eee787634",
	 *  "type":"owner"
	 * } */
	private Response extractPartyRoleInfos(String role){
		if(role == null || role.isEmpty()) return null;
		String response ="";
		String urlImage ="";
		JSONObject obj = new JSONObject(role);

		for(String key : obj.keySet()){
			if(key.equals("person")){
				JSONObject person = obj.getJSONObject(key);
				for(String person_key : person.keySet()){
					response += adaptInfo(person_key, person.get(person_key).toString());
				}
			} else if(key.equalsIgnoreCase("image")){
				urlImage = obj.get(key).toString();
			}else{
				response += adaptInfo(key, obj.get(key).toString());
			}
		}
		return new Response(response, urlImage);
	}
	
	/** {   
	 * "incendie": false,   
	 * "vandalisme": true,   
	 * "inondation": true 
	 * } */
	private Response extractRisksInfos(String risks) throws JSONException {
		if(risks == null || risks.isEmpty()) return null;
		JSONObject obj = new JSONObject(risks);
		String response = "";
		for(String key : obj.keySet()){
			response += adaptInfo(key, obj.get(key).toString()+"_risk");
		}
		return new Response(response);
	}
	
	/** {
	 * "image":"https://mabanque.bnpparibas/rsc/contrib/image/particuliers/gabarits-libres/rib.jpg"
	 * } */
	private Response extractRibInfos(String risks) throws JSONException {
		if(risks == null || risks.isEmpty()) return null;
		JSONObject obj = new JSONObject(risks);
		String response = "";
		String urlImage = "";
		for(String key : obj.keySet()){
			if(key.equalsIgnoreCase("image")){
				urlImage = obj.get(key).toString();
			}
		}
		return new Response(response,urlImage);
	}
	
	/**
	 * Essaye de récupérer des fichiers properties une valeur de langue de la clé et de la valeur associée.
	 * @param key
	 * @param value
	 * @return une chaîne contenant la clé et la valeur.
	 */
	private String adaptInfo(String key, String value){
		try{
			key = messages.getString(key);
		} catch (MissingResourceException e){} // la ressource ne contient pas ce String
		try {
			value = messages.getString(value);
		} catch (MissingResourceException e){} // la ressource ne contient pas ce String
		return key + " : " +value+"\n";
	}
}
