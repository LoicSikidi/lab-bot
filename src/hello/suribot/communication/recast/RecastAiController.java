package hello.suribot.communication.recast;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
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
	 * @param idUser 
	 */
	public void sendMessage(final JSONObject json, String message, String idUser){
		System.out.println("RecastAiController sendMessage");
		try {
			//TODO : transferer contenu du message à Recast, et 
			// renvoyer response à S.S.3 pour analyse des intents de Recast.
			//String response = sendPostAndReturnResponse(recastURI, message);
			JSONObject intents = fakeRecast(message, idUser);
			nextStep.analyzeRecastIntents(json, intents);
			
		} catch (Exception e) {
			System.out.println("RecastAiController : Message "+message+" not send... ("+e+")");
		}
	}
	
	// TODO : à retirer une fois Recast OK
	public static JSONObject fakeRecast(String messageUser, String idUser){
		JSONObject js= new JSONObject();
		js.put(FakeRecastKeys.IDUSER.name(), idUser);
		if(messageUser.contains("contract")){
			js.put(FakeRecastKeys.CONTEXTE.name(), "demande");
			
			if(messageUser.contains("IDID")) js.put(FakeRecastKeys.IDENTIFICATION.name(), "ID-5935697");
			
			if(messageUser.contains("risk")) js.put(FakeRecastKeys.QUOI.name(), "risk");
			else if(messageUser.contains("billing")) js.put(FakeRecastKeys.QUOI.name(), "billings");
			else if(messageUser.contains("partyRole")) js.put(FakeRecastKeys.QUOI.name(), "role");
			
			if(messageUser.contains("IDC")) js.put(FakeRecastKeys.COMPLEMENT.name(), "ID-731119");
		}
		
		return js;
	}
	
}