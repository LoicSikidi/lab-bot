package hello.suribot.interfaces;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;

public interface IAiController {

	/**
	 * Send message to Recast or API.ai, parse the response, and transfer it to {@link IntentsAnalyzer}
	 * @param json 
	 * @param message
	 * @param idUser 
	 */
	void sendMessage(JSONObject json, String message, String idUser);

}