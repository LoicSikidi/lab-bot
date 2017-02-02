package hello.suribot.interfaces;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.communication.botConnector.BotConnectorIdentity;

/**
 * Interface pour le composant analyseur d'intent permettant de communiquer avec les intelligences artificielles
 */
public interface IAiController {

	/**
	 * Envoie un message à Recast ou à API.ai, parse la réponse, et la transfert à {@link IntentsAnalyzer}
	 * @param identity du botConnector
	 * @param json complet provenant du botConnector
	 * @param message de l'utilisateur
	 * @param idUser identifiant de l'utilisateur
	 * @return reussite ou non
	 */
	boolean sendMessage(BotConnectorIdentity identity, JSONObject json, String message, String idUser);

}