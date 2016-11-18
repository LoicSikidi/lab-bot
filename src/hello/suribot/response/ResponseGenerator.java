package hello.suribot.response;

import hello.suribot.interfaces.IJsonCreator;
import hello.suribot.interfaces.IJsonDecoder;

/**
 * Classe permettant de générer des réponses (à S.S.1 communication MBC) suivant les données fournies.
 */
public class ResponseGenerator implements IJsonCreator, IJsonDecoder{
	
	public ResponseGenerator() {}

	public String generateUnderstoodMessage(String params) {
		return params;
	}
	
	public String generateNotUnderstoodMessage() {
		return "Veuillez reformuler votre question";
	}
	
	public String generateMessageButMissOneArg(String argName) {
		if(argName==null || argName.isEmpty()) return generateNotUnderstoodMessage();
		return "Il manque un argument à votre demande : "+argName;
	}
	
	public String generateMessageButMissArgs(String...args) {
		if(args==null || args.length==0) return generateNotUnderstoodMessage();
		else if (args.length==1) return generateMessageButMissOneArg(args[0]);
		String response = "Veuillez préciser si votre demande concerne : \n";
		for(String arg : args){
			if(arg!=null && !arg.isEmpty()) response+=arg+"\n";
		}
		return response;
	}
	
}