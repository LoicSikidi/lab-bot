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
	
}