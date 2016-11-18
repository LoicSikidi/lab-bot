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
		
		String idUser = recastJson.getString(FakeRecastKeys.IDUSER.getName());
		try{
			String contexte = recastJson.getString(FakeRecastKeys.CONTEXTE.getName());
			Boolean demandeComprise = false;
			if(contexte.equals("demande")){
				//Traitement pour l'api lab-bot-api
				JSONObject js = ContractAnalyzer.contractAnalyzer(recastJson);
				if(js.getBoolean("success")){
					JSonMemory.removeLastIntents(idUser);
					String rep;
					String responseToMBC;
					try {
						rep = apicontroller.sendGet(js.getString(FakeRecastKeys.URITOCALL.name()));
					} catch (Exception e) {
						rep = null;
					}
					responseToMBC = responsegenerator.generateUnderstoodMessage(rep);
					nextToCall.sendMessage(mbc_json, responseToMBC);
					demandeComprise= true;
				}
			} else if (true) {
				System.out.println();
				//Traitement si on a d'autre contextes : transport, maps, ...
			}
			
			//On a pas réussi à traiter la demande l'utilisateur, on essaye de la compléter
			//avec l'ancienne demande
			if(!demandeComprise){
				String stringLastIntent = JSonMemory.getLastIntents(idUser);
				if(stringLastIntent==null||stringLastIntent.isEmpty()){
					//Demande incomprise et il n'y a pas d'ancienne demande en attente
					//donc on arrete le traitement  et envoie une erreur a SS5
					JSonMemory.putLastIntents(idUser, recastJson.toString());
					String responseToMBC = responsegenerator.generateNotUnderstoodMessage();
					nextToCall.sendMessage(mbc_json, responseToMBC);
					return;
				}
				//On essaye de completer l'ancienne demande présente avec les nouvelles données reçus
				JSONObject lastIntent = new JSONObject(stringLastIntent);
				JSONObject newRequest = generateNewRequestWithLastIntent(recastJson, lastIntent);
				JSonMemory.removeLastIntents(idUser);
				analyzeRecastIntents(mbc_json, newRequest);
			}
		}catch(JSONException e){
			//Il y a eu une exception lors de la lecture de la recuperation du contexte,
			//on essaye de completer une ancienne demande avec les nouvelles données
			String stringLastIntent = JSonMemory.getLastIntents(idUser);
			if(stringLastIntent==null||stringLastIntent.isEmpty()){
				//Demande incomprise et il n'y a pas d'ancienne demande en attente
				//donc on arrete le traitement  et envoie une erreur a SS5
				JSonMemory.putLastIntents(idUser, recastJson.toString());
				String responseToMBC = responsegenerator.generateNotUnderstoodMessage();
				nextToCall.sendMessage(mbc_json, responseToMBC);
				return;
			}
			JSONObject lastIntent = new JSONObject(stringLastIntent);
			JSONObject newRequest = generateNewRequestWithLastIntent(recastJson, lastIntent);
			JSonMemory.removeLastIntents(idUser);
			analyzeRecastIntents(mbc_json, newRequest);
		}
	}
	
	public static JSONObject generateNewRequestWithLastIntent(JSONObject newDemande, JSONObject lastDemande){
		String[] keys = JSONObject.getNames(newDemande);
		
		for(String key: keys){
			//on insere les nouvelle données de la demande à la derniere demande incomprises
			//spour essayer de la completer
			lastDemande.put(key, newDemande.getString(key));
		}
		return lastDemande;
	}
	
}
