package hello.suribot.response;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.interfaces.IResponseGenerator;
import hello.suribot.response.contracts.ContractResponseGenerator;
import hello.suribot.response.contracts.IContractResponseGenerator;
import test.hello.suribot.response.contracts.ContractResponseGeneratorTest;

/**
 * Classe permettant de générer des réponses (à S.S.1 communication MBC) suivant les données fournies.
 */
public class ResponseGenerator implements IResponseGenerator{
	
	private ResourceBundle messages;
	

	/** public static pour les tests JUnit ({@link ContractResponseGeneratorTest}) */
	public static final String bundleFile = "message.MessagesBundle"; 
	
	public ResponseGenerator() {
		messages = ResourceBundle.getBundle(bundleFile);
	}
	
	public ResponseGenerator(String langue) {
		Locale locale = new Locale(langue);
		messages = ResourceBundle.getBundle(bundleFile, locale);
	}

	/* (non-Javadoc)
	 * @see hello.suribot.response.IResponseGenerator#generateContractUnderstoodMessage(hello.suribot.analyze.contracts.ContractParams, boolean, java.lang.String)
	 */
	@Override
	public String generateUnderstoodMessage(String context, String calledMethod, boolean choice, String params) {
		if(params==null || params.isEmpty()) return generateInternalErrorMessage();
		
		if(context != null && context.equals(IntentsAnalyzer.CONTRAT)){
			try {
				IContractResponseGenerator gen = new ContractResponseGenerator(messages);
				return gen.generateContractMessage(calledMethod, choice, params);
			} catch (Exception e){
				return generateInternalErrorMessage();
			}
		} else { // Par sécurité
			try {
				return new JSONObject(params).toString(2);
			} catch (JSONException e){ /* not a json object */ }
			
			try {
				return new JSONArray(params).toString(2);
			} catch (JSONException e){ /* not a json array */ }
		}
		
		return params;
	}
	
	/* (non-Javadoc)
	 * @see hello.suribot.response.IResponseGenerator#generateInternalErrorMessage()
	 */
	@Override
	public String generateInternalErrorMessage() {
		return messages.getString("internalErrorMessage");
	}

	/* (non-Javadoc)
	 * @see hello.suribot.response.IResponseGenerator#generateNotUnderstoodMessage()
	 */
	@Override
	public String generateNotUnderstoodMessage() {
		return messages.getString("notUnderstoodMessage");
	}
	
	/* (non-Javadoc)
	 * @see hello.suribot.response.IResponseGenerator#generateMessageButMissOneArg(hello.suribot.response.MessagesResponses)
	 */
	@Override
	public String generateMessageButMissOneArg(MessagesResponses key) {
		if(key==null) return generateNotUnderstoodMessage();
		if(!messages.containsKey(key.toString())) return generateInternalErrorMessage();
		return messages.getString("missOneArg")+messages.getString(key.toString());
	}
	
	/* (non-Javadoc)
	 * @see hello.suribot.response.IResponseGenerator#generateMessageButMissArgs(java.util.List)
	 */
	@Override
	public String generateMessageButMissArgs(List<MessagesResponses> keys) {
		if(keys==null || keys.size()==0) return generateNotUnderstoodMessage();
		else if (keys.size()==1) return generateMessageButMissOneArg(keys.get(0));
		
		String response = messages.getString("missArgs") + "\n"; // TODO: changer toutes les mises en forme par une méthode s'adaptant au channel appelant
		for(MessagesResponses key : keys){
			if(key!=null && messages.containsKey(key.toString())) response += messages.getString(key.toString())+"\n";
		}
		return response;
	}
	
}