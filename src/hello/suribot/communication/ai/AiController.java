package hello.suribot.communication.ai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import hello.suribot.SuribotKeys;
import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.communication.ai.parser.SuribotParser;
import hello.suribot.communication.botConnector.BotConnectorIdentity;
import hello.suribot.interfaces.IAiController;
import hello.suribot.interfaces.IIntentsAnalyzer;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant de transmettre des messages utilisateur à un moteur d'intelligence (Recast.ai, API.ai, ...),
 * et d'analyser la réponse obtenue.
 */
public class AiController extends AbstractHttpSender implements IAiController{
	
	private static final Logger logger = LogManager.getLogger();

	private IIntentsAnalyzer nextStep;

	public AiController() {
		this.nextStep = new IntentsAnalyzer();
	}

	/* (non-Javadoc)
	 * @see hello.suribot.interfaces.IAiController#sendMessage(hello.suribot.communication.botConnector.BotConnectorIdentity, org.json.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendMessage(BotConnectorIdentity identity, JSONObject json, String message, String idUser){
		logger.info("AiController : start sendMessage");
		try {
			String language = "fr";
			JSONObject intents = null;
			SuribotParser parser = new SuribotParser();
 			if(message.toLowerCase().contains("api")){ // call API.ai
				AIResponse response = callApiAi(message, EnvVar.TOKENAPIAI.getValue(), language);
				intents = parser.parseApiAi(response);
			} else { // call Recast.ai
				intents = callRecast(message, EnvVar.TOKENRECAST.getValue(), language);
				intents = parser.parseRecast(intents);
			}
 			if(message.toLowerCase().contains("english")) intents.getJSONObject(SuribotKeys.RESULTS.value).put(SuribotKeys.LANGUAGE.value, "en");
			nextStep.analyzeIntents(identity, json, intents, idUser, true);
			return true;

		} catch (Exception e) {
			logger.error("Message \""+message+"\" not send...\n"+e.getStackTrace());
		}
		return false;
	}

	public AIResponse callApiAi(String message, String tokenapiai, String langue) throws AIServiceException {
		AIConfiguration ai = new AIConfiguration(tokenapiai);
		return new AIDataService(ai).request(new AIRequest(message));
	}

	public JSONObject callRecast(String text, String token, String language) throws JSONException{
		String response = null;
		try {
			response = sendPost("https://api.recast.ai/v2/request", text, "Authorization", "Token "+token, "language="+language);
		} catch (Exception e) {}
		
		if(response != null) return new JSONObject(response);
		return new JSONObject();
	}

}