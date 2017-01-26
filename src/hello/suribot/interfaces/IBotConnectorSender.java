package hello.suribot.interfaces;

import org.json.JSONObject;

import hello.suribot.response.Response;

/**
 * Interface pour le composant BotConnector permettant d'envoyer des messages aux différents BotConnector
 */
public interface IBotConnectorSender {
	
	/**
	 * Envoie du message au BotConnector (utilisants les tokens configurés, ...)
	 * @param json complet reçu par le BotConnector
	 * @param response à envoyer à l'utilisateur
	 * @return la reussite de l'envoi 
	 */
	public boolean sendMessage(JSONObject json, Response response);

}