package hello.suribot.interfaces;

import org.json.JSONObject;

public interface IIntentsAnalyzer {

	/**
	 * Cette méthode analyse les données fournies par un moteur d'intelligence (qui a analysé la demande d'un utilisateur), 
	 * et permet la production d'une réponse adaptée à la demande utilisateur.
	 * @param mbc_json
	 * @param recastJson
	 * @param idUser
	 * @param firstTraitement
	 */
	void analyzeIntents(JSONObject mbc_json, JSONObject recastJson, String idUser, boolean firstTraitement);

}