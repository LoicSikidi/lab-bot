package hello.suribot.communication.recast;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.interfaces.IHttpSender;

/**
 * Classe controleur permettant d'écouter et envoyer des messages à Recast.ai
 */
public class RecastAiController implements IHttpSender{
	
	private IntentsAnalyzer nextStep;

	public RecastAiController() {
		this.nextStep = new IntentsAnalyzer();
	}
	
	/**
	 * Send message to recast.ai, and listen response
	 * @param json 
	 * @param message
	 * @param idUser 
	 */
	public void sendMessage(final JSONObject json, String message, String idUser){
		try {
			//TODO : transferer contenu du message à Recast, et 
			// renvoyer response à S.S.3 pour analyse des intents de Recast.
			//String response = sendPostAndReturnResponse(recastURI, message);
			JSONObject intents = fakeRecast(message, idUser);
			nextStep.analyzeRecastIntents(json, intents);
			
		} catch (Exception e) {
			System.out.println("RecastAiController : Message "+message+" not send... ("+e+")");
		}
	}
	
	// TODO : à retirer une fois Recast OK
	public static JSONObject fakeRecast(String messageUser, String idUser){
		JSONObject js= new JSONObject();
		js.put(FakeRecastKeys.IDUSER.name(), idUser);
		if(containsContract(messageUser)){
			js.put(FakeRecastKeys.CONTEXTE.name(), IntentsAnalyzer.DEMANDE);
			
			if(messageUser.toLowerCase().contains("idid")) js.put(FakeRecastKeys.IDENTIFICATION.name(), "ID-5935697");
			
			if(containsRisk(messageUser)) js.put(FakeRecastKeys.QUOI.name(), "risk");
			else if(containsBilling(messageUser)) js.put(FakeRecastKeys.QUOI.name(), "billings");
			else if(containsPartyRole(messageUser)) js.put(FakeRecastKeys.QUOI.name(), "role");
			
			if(messageUser.toLowerCase().contains("idc")) js.put(FakeRecastKeys.COMPLEMENT.name(), "ID-731119");
		}
		
		return js;
	}
	
	private static boolean containsContract(String messageUser){
		if(messageUser==null) return false;
		return (messageUser.toLowerCase().contains("contract")) || (messageUser.toLowerCase().contains("contrat"));
	}
	
	private static boolean containsRisk(String messageUser){
		if(messageUser==null) return false;
		return (messageUser.toLowerCase().contains("risk")) || (messageUser.toLowerCase().contains("risque"));
	}
	
	private static boolean containsBilling(String messageUser){
		if(messageUser==null) return false;
		return (messageUser.toLowerCase().contains("billing")) 
				|| (messageUser.toLowerCase().contains("paiement"));
	}
	
	private static boolean containsPartyRole(String messageUser){
		if(messageUser==null) return false;
		return (messageUser.toLowerCase().contains("partyrole")) 
				|| (messageUser.toLowerCase().contains("proprietaire"))
				|| (messageUser.toLowerCase().contains("proprio"));
	}
	
}