package hello.suribot.response.contracts;

import java.util.Arrays;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContractResponseGenerator {
	
	public String generateBillingsChoiceResponse(String params) {
		String response = "De quel paiement parlez-vous ? : \n\n";
		for(String str : extractBillingsChoice(params)){
			response += str+"\n\n";
		}
		return response;
	}

	public String generateBillingInfosResponse(String params) {
		String response = "Voici les informations sur le paiement : \n\n";
		for(String str : extractBillingInfos(params)){
			response += str+"\n\n";
		}
		return response;
	}
	
	public String generatePartyRolesChoiceResponse(String params) {
		String response = "De quel personne parlez-vous ? : \n\n";
		for(String str : extractPartyRolesChoice(params)){
			response += str+"\n\n";
		}
		return response;
	}

	public String generatePartyRoleInfosResponse(String params) {
		String response = "Voici les informations sur la personne : \n\n";
		for(String str : extractPartyRoleInfos(params)){
			response += str+"\n\n";
		}
		return response;
	}
	
	public String generateRisksChoiceResponse(String params) {
		String response = "De quel couverture parlez-vous ? : \n\n";
		for(String str : extractRisksChoice(params)){
			response += str+"\n\n";
		}
		return response;
	}
	
	public String generateRisksInfosResponse(String params) {
		String response = "Voici les informations sur la couverture de l'objet : \n\n";
		for(String str : extractRisksInfos(params)){
			response += str+"\n\n";
		}
		return response;
	}

	
	/**	[
	*	    { "rel":"self","href":"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-96268885"},
	*		{ "rel":"self","href":"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-2355596664"},
	*		{ "rel":"self","href":"http://localhost:12347/insurance/contract/ID-5935697/billings/ID-123987456"}
	*	] 
	**/	
	public String[] extractBillingsChoice(String choices) throws JSONException{
		JSONArray array = new JSONArray(choices);
		String[] results = new String[array.length()];
		
		for(int i = 0; i<array.length(); i++){
			results[i] = array.getJSONObject(i).get("href").toString().split("/")[7].replace("ID-", "prélèvement ");
		}
		
		return results;
	}
	
	/**
	 * { "methode": "cheque",   
	 * "amount": 542.97,   
	 * "identifiant": "123987456",   
	 * "frequency": "hebdomadaire",  
	 * "next_date": "2017-11-10" }
	 */
	public String[] extractBillingInfos(String billing) throws JSONException{
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
	* [
    * 	{"rel":"self","href":"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee78595"},
    * 	{"rel":"self","href":"http://localhost:12347/insurance/contract/ID-5935697/partyRoles/ID-eee787634"}
	* ]
	*/
	public String[] extractPartyRolesChoice(String choices) throws JSONException{
		JSONArray array = new JSONArray(choices);
		String[] results = new String[array.length()];
		
		for(int i = 0; i<array.length(); i++){
			results[i] = array.getJSONObject(i).get("href").toString().split("/")[7].replace("ID-", "personne ");
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
	public String[] extractPartyRoleInfos(String role) throws JSONException{
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
	 * [
	 * 		{"rel":"self","href":"http://localhost:12347/insurance/contract/ID-64767/risk/ID-kockeo/couverture"},
	 * 		{"rel":"self","href":"http://localhost:12347/insurance/contract/ID-64767/risk/ID-kockeo/couverture"},
	 * 		{"rel":"self","href":"http://localhost:12347/insurance/contract/ID-64767/risk/ID-52665236/couverture"}
	 * ]
	 */
	public String[] extractRisksChoice(String choices) throws JSONException{
		JSONArray array = new JSONArray(choices);
		String[] results = new String[array.length()];
		
		for(int i = 0; i<array.length(); i++){
			results[i] = array.getJSONObject(i).get("href").toString().split("/")[7].replace("ID-", "objet ");
		}
		
		return results;
	}
	
	/**
	 * {   
	 * "incendie": false,   
	 * "vandalisme": true,   
	 * "inondation": true 
	 * }
	 */
	public String[] extractRisksInfos(String risks) throws JSONException{
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
