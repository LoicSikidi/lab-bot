package hello.suribot.interfaces;

import org.json.JSONObject;

public interface IRecastBotConnectorSender {
	
	/**
	 * Send a message to RecastBotConnector (using configured tokens, etc...)
	 * @param json
	 * @param message
	 */
	void sendMessage(JSONObject json, String message);

}