package hello.suribot.interfaces;

import org.json.JSONObject;

import hello.suribot.response.Response;

public interface IRecastBotConnectorSender {
	
	/**
	 * Send a message to RecastBotConnector (using configured tokens, etc...)
	 * @param json
	 * @param message
	 * @return la reussite de l'envoi 
	 */
	boolean sendMessage(JSONObject json, Response response);

}