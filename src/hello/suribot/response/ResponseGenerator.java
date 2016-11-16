package hello.suribot.response;

import hello.suribot.interfaces.IJsonCreator;
import hello.suribot.interfaces.IJsonDecoder;

/**
 * Classe permettant de générer des réponses (à S.S.1 communication MBC) suivant les données fournies.
 */
public class ResponseGenerator implements IJsonCreator, IJsonDecoder{
	
	public ResponseGenerator() {}

	//TODO : créer la ou les méthodes de génération
	public String generateMessage(String params) {
		System.out.println("ResponseGenerator generateMessage");
		return "GeneratedResponse";
	}
	
}