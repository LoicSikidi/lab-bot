package hello.suribot.analyze;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.contracts.ContractAnalyzer;
import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.api.APIController;
import hello.suribot.communication.mbc.NodeJsMBCSender;
import hello.suribot.communication.recast.FakeRecastKeys;
import hello.suribot.interfaces.IJsonDecoder;
import hello.suribot.response.ResponseGenerator;

public class IntentsAnalyzer implements IJsonDecoder{
	
	private NodeJsMBCSender nextToCall;
	
	private ResponseGenerator responsegenerator;
	private APIController apicontroller;
	

	public IntentsAnalyzer() {
		this.responsegenerator = new ResponseGenerator();
		this.apicontroller = new APIController();
		this.nextToCall = new NodeJsMBCSender();
	}

	public void analyzeRecastIntents(JSONObject mbc_json, JSONObject recastJson) {
		System.out.println("IntentsAnalyzer analyzeRecastIntents");
		//TODO: get url and parameters
		String apiResponse = null;
//		if(intents!=null) apiResponse = apicontroller.sendMessageAndReturnResponse("", intents);
//		String generatedResponse = responsegenerator.generateUnderstoodMessage(apiResponse);
		
//		nextToCall.sendMessage(json, generatedResponse);
		
		String idUser = recastJson.getString(FakeRecastKeys.IDUSER.getName());
		try{
			String contexte = recastJson.getString(FakeRecastKeys.CONTEXTE.getName());
			Boolean demandeComprise = false;
			if(contexte.equals("demande")){
				//Traitement pour l'api lab-bot-api
				JSONObject js = ContractAnalyzer.contractAnalyzer(recastJson);
				if(js.getBoolean("success")){
					JSonMemory.removeLastIntents(idUser);
					APIController api = new APIController();
					String rep = api.sendMessageAndReturnResponse(js.getString(FakeRecastKeys.URITOCALL.name()), "");
					ResponseGenerator reponseGenerator= new ResponseGenerator();
					String responseToMBC= reponseGenerator.generateUnderstoodMessage(rep);
					System.out.println("responseToMBC = "+responseToMBC);
					demandeComprise= true;
				}
			} else if (true) {
				//Traitement si on a d'autre contextes : transport, maps, ...
			}
			
			//On a pas réussi à traiter la demande l'utilisateur, on essaye de la compléter
			//avec l'ancienne demande
			if(!demandeComprise){
				String stringLastIntent = JSonMemory.getLastIntents(idUser);
				System.out.println("stringLastIntent == "+stringLastIntent);
				if(stringLastIntent==null||stringLastIntent.isEmpty()){
					System.out.println(recastJson.toString());
					System.out.println("in if");
					//Demande incomprise donc on arrete le traitement  et envoie une erreur a SS5
					JSonMemory.putLastIntents(idUser, recastJson.toString());
					ResponseGenerator reponseGenerator= new ResponseGenerator();
					String responseToMBC= reponseGenerator.generateUnderstoodMessage("erreur");
					System.out.println(responseToMBC);
					return;
				}
				JSONObject lastIntent = new JSONObject(stringLastIntent);
				System.out.println("lastIntent == "+lastIntent);
				System.out.println("recastJson == "+recastJson);
				JSONObject newRequest = generateNewRequestWithLastIntent(recastJson, lastIntent);
				System.out.println("newRequest == "+newRequest);
				JSonMemory.removeLastIntents(idUser);
				analyzeRecastIntents(mbc_json, newRequest);
			}
			//ENVOYER A SS4
		}catch(JSONException e){
			//Il y a eu une exception lors de la lecture de la recuperation du contexte,
			//on essaye de completer une ancienne demande avec les nouvelles données
			String stringLastIntent = JSonMemory.getLastIntents(idUser);
			if(stringLastIntent==null||stringLastIntent.isEmpty()){
				//Demande incomprise donc on arrete le traitement  et envoie une erreur a SS5
				JSonMemory.putLastIntents(idUser, recastJson.toString());
				ResponseGenerator reponseGenerator= new ResponseGenerator();
				String responseToMBC= reponseGenerator.generateUnderstoodMessage("erreur");
				System.out.println(responseToMBC);
				return;
			}
			JSONObject lastIntent = new JSONObject(stringLastIntent);
			JSONObject newRequest = generateNewRequestWithLastIntent(recastJson, lastIntent);
			JSonMemory.removeLastIntents(idUser);
			analyzeRecastIntents(mbc_json, newRequest);
			//CALL SS5
		}
	}
	
	public static JSONObject generateNewRequestWithLastIntent(JSONObject newDemande, JSONObject lastDemande){

		String[] keys = JSONObject.getNames(newDemande);
		
		for(String key: keys){
			//on insere les nouvelle données de la demande à la derniere demande incomprises pour essayer de la completer
			lastDemande.put(key, newDemande.getString(key));
		}
		
		return lastDemande;
	}
	
}
