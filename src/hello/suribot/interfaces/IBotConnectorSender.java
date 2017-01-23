package hello.suribot.interfaces;

import org.json.JSONObject;

import hello.suribot.response.Response;

public interface IBotConnectorSender {
	
	/**
	 * Send a message to the BotConnector (using configured tokens, etc...)
	 * @param json
	 * @param message
	 * @return la reussite de l'envoi 
	 */
	public boolean sendMessage(JSONObject json, Response response);

}