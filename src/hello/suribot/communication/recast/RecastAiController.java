package hello.suribot.communication.recast;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.JSONKey;
import hello.suribot.interfaces.IHttpSender;

/**
 * Classe controleur permettant d'écouter et envoyer des messages à Recast.ai
 */
public class RecastAiController implements IHttpSender{
	
	private IntentsAnalyzer nextStep;

	public RecastAiController() {
		this.nextStep = new IntentsAnalyzer();
	}
	
	/**
	 * Send message to recast.ai, and listen response
	 * @param json 
	 * @param message
	 */
	public void sendMessage(final JSONObject json, String message){
		System.out.println("RecastAiController sendMessage");
		try {
			//TODO : transferer contenu du message à Recast, et 
			// renvoyer response à S.S.3 pour analyse des intents de Recast.
			//String response = sendPostAndReturnResponse(recastURI, message);
			String intents = message;
			nextStep.analyzeRecastIntents(json, intents);
			
		} catch (Exception e) {
			System.out.println("RecastAiController : Message "+message+" not send... ("+e+")");
		}
	}
	
	public static JSONObject fakeRecast(String messageUser, String idUser){
		JSONObject js= new JSONObject();
		js.put(JSONKey.IDUSER.name(), idUser);
		if(messageUser.contains("contract")){
			js.put(JSONKey.CONTEXTE.name(), "demande");
			
			if(messageUser.contains("IDID")) js.put(JSONKey.IDENTIFICATION.name(), "ID-5935697");
			
			if(messageUser.contains("risk")) js.put(JSONKey.QUOI.name(), "risk");
			else if(messageUser.contains("billing")) js.put(JSONKey.QUOI.name(), "billings");
			else if(messageUser.contains("partyRole")) js.put(JSONKey.QUOI.name(), "role");
			
			if(messageUser.contains("IDC")) js.put(JSONKey.COMPLEMENT.name(), "ID-731119");
		}
		
		return js;
	}
	
}