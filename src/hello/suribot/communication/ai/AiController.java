package hello.suribot.communication.ai;


import org.json.JSONException;
import org.json.JSONObject;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant de transmettre des messages utilisateur à un moteur d'intelligence (Recast.ai, API.ai, ...),
 * et d'analyser la réponse obtenue.
 */
public class AiController extends AbstractHttpSender{

	private IntentsAnalyzer nextStep;

	public AiController() {
		this.nextStep = new IntentsAnalyzer();
	}

	/**
	 * Send message to Recast or API.ai, parse the response, and transfer it to {@link IntentsAnalyzer}
	 * @param json 
	 * @param message
	 * @param idUser 
	 */
	public void sendMessage(final JSONObject json, String message, String idUser){
		try {
			JSONObject intents = null;
 			if(message.toLowerCase().contains("api")){ // call API.ai
				AIResponse response = callApiAi(message, EnvVar.TOKENAPIAI, "fr");
				intents = new SuribotParser().parseApiAi(response);
			} else { // call Recast.ai
				intents = callRecast(message, EnvVar.TOKENRECAST, "fr");
				intents = new SuribotParser().parseRecast(intents);
			}
			nextStep.analyzeIntents(json, intents, idUser, true);

		} catch (Exception e) {
			// TODO: Log System ?
			System.out.println("AiController : Message \""+message+"\" not send... ("+e+")");
		}
	}

	private AIResponse callApiAi(String message, String tokenapiai, String langue) throws AIServiceException {
		AIConfiguration ai = new AIConfiguration(tokenapiai);
		return new AIDataService(ai).request(new AIRequest(message));
	}

	private JSONObject callRecast(String text, String token, String language) throws JSONException{
		String response = null;
		try {
			response = sendPost("https://api.recast.ai/v2/request", text, "Authorization", "Token "+token, "language="+language);
		} catch (Exception e) {}
		
		if(response != null) return new JSONObject(response);
		return new JSONObject();
	}

}