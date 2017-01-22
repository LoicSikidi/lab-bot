package hello.suribot.interfaces;

import org.json.JSONObject;

public interface IIntentsAnalyzer {

	/**
	 * Cette méthode analyse les données fournies par un moteur d'intelligence (qui a analysé la demande d'un utilisateur), 
	 * et permet la production d'une réponse adaptée à la demande utilisateur.
	 * @param fullJSon
	 * @param intents
	 * @param idUser
	 * @param firstTraitement
	 */
	void analyzeIntents(JSONObject fullJSon, JSONObject intents, String idUser, boolean firstTraitement);

}