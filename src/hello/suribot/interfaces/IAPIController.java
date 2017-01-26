package hello.suribot.interfaces;

import java.io.IOException;

/**
 * Interface pour le composant communication avec API permettant de communiquer avec les différents API externe
 */
public interface IAPIController {

	/**
	 * Envoie d'un message à l'API et écoute la réponse
	 * @throws IOException 
	 */
	String send(String url) throws IOException;

}