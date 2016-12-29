package hello.suribot.response;

import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

import hello.suribot.analyze.contracts.ContractParams;
import hello.suribot.response.contracts.ContractResponseGenerator;

/**
 * Classe permettant de générer des réponses (à S.S.1 communication MBC) suivant les données fournies.
 */
public class ResponseGenerator{
	
	ResourceBundle messages;
	
	public ResponseGenerator() {
		messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle");
	}
	
	public ResponseGenerator(String langue) {
		Locale locale = new Locale(langue,"");
		messages = ResourceBundle.getBundle("hello.suribot.response.message.MessagesBundle", locale);
	}

	public String generateContractUnderstoodMessage(ContractParams method, boolean choice, String params) {
		if(params==null || params.isEmpty()) return generateNotUnderstoodMessage();
		
		if(method != null){
			ContractResponseGenerator contract_gen = new ContractResponseGenerator(messages);
			try {
				switch (method) {
				case risk:
					if(choice) return contract_gen.generateRisksChoiceResponse(params);
					return contract_gen.generateRisksInfosResponse(params);
					
				case role:
					if(choice) return contract_gen.generatePartyRolesChoiceResponse(params);
					return contract_gen.generatePartyRoleInfosResponse(params);
					
				case prelevement:
					if(choice) return contract_gen.generateBillingsChoiceResponse(params);
					return contract_gen.generateBillingInfosResponse(params);

				default:
					return generateNotUnderstoodMessage();
				}
			} catch (Exception e){
				return generateInternalErrorMessage();
			}
		} else {
			try {
				return new JSONObject(params).toString(2);
			} catch (JSONException e){ /* not a json object */ }
			
			try {
				return new JSONArray(params).toString(2);
			} catch (JSONException e){ /* not a json array */ }
		}
		
		return params;
	}
	
	private String generateInternalErrorMessage() {
		return messages.getString("internalErrorMessage");
	}

	public String generateNotUnderstoodMessage() {
		return messages.getString("notUnderstoodMessage");
	}
	
	public String generateMessageButMissOneArg(String argName) {
		if(argName==null || argName.isEmpty()) return generateNotUnderstoodMessage();
		return messages.getString("missOneArg")+argName;
	}
	
	public String generateMessageButMissArgs(List<String> args) {
		if(args==null || args.size()==0) return generateNotUnderstoodMessage();
		else if (args.size()==1) return generateMessageButMissOneArg(args.get(0));
		
		String response = messages.getString("missArgs");
		for(String arg : args){
			if(arg!=null && !arg.isEmpty()) response+=arg+"\n";
		}
		return response;
	}
	
}