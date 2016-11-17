package hello.suribot.response;

import hello.suribot.interfaces.IJsonCreator;
import hello.suribot.interfaces.IJsonDecoder;

/**
 * Classe permettant de générer des réponses (à S.S.1 communication MBC) suivant les données fournies.
 */
public class ResponseGenerator implements IJsonCreator, IJsonDecoder{
	
	public ResponseGenerator() {}

	public String generateUnderstoodMessage(String params) {
		return "GeneratedResponse";
	}
	
	public String generateNotUnderstoodMessage() {
		return "generateNotUnderstoodMessage";
	}
	
	public String generateMessageButMissOneArg(String argName) {
		return "generateMessageButMissOneArg";
	}
	
	public String generateMessageButMissArgs(String...args) {
		return "generateMessageButMissArgs";
	}
	
}