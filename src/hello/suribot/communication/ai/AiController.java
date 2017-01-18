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
import hello.suribot.communication.ai.parser.SuribotParser;
import hello.suribot.interfaces.IAiController;
import hello.suribot.interfaces.IIntentsAnalyzer;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant de transmettre des messages utilisateur à un moteur d'intelligence (Recast.ai, API.ai, ...),
 * et d'analyser la réponse obtenue.
 */
public class AiController extends AbstractHttpSender implements IAiController{

	private IIntentsAnalyzer nextStep;

	public AiController() {
		this.nextStep = new IntentsAnalyzer();
	}

	/* (non-Javadoc)
	 * @see hello.suribot.communication.ai.IAiController#sendMessage(org.json.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(final JSONObject json, String message, String idUser){
		try {
			String language = "fr";
			JSONObject intents = null;
 			if(message.toLowerCase().contains("api")){ // call API.ai
				AIResponse response = callApiAi(message, EnvVar.TOKENAPIAI.getValue(), language);
				intents = new SuribotParser().parseApiAi(response);
			} else { // call Recast.ai
				intents = callRecast(message, EnvVar.TOKENRECAST.getValue(), language);
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