package hello.suribot.interfaces;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.communication.botConnector.BotConnectorIdentity;

public interface IAiController {

	/**
	 * Send message to Recast or API.ai, parse the response, and transfer it to {@link IntentsAnalyzer}
	 * @param identity
	 * @param json 
	 * @param message
	 * @param idUser 
	 * @return reussite ou non
	 */
	boolean sendMessage(BotConnectorIdentity identity, JSONObject json, String message, String idUser);

}