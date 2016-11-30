package hello.suribot.analyze;

import java.util.ArrayList;
import java.util.List;

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
	
	public static final String SUCCESS = "success";
	public static final String MISSINGPARAMS = "missingparams";
	public static final String DEMANDE = "demande";
	

	public IntentsAnalyzer() {
		this.responsegenerator = new ResponseGenerator();
		this.apicontroller = new APIController();
		this.nextToCall = new NodeJsMBCSender();
	}

	public void analyzeRecastIntents(JSONObject mbc_json, JSONObject recastJson) {
		
		String idUser = recastJson.getString(FakeRecastKeys.IDUSER.getName());
		try{
			String contexte = recastJson.getString(FakeRecastKeys.CONTEXTE.getName());
			String responseToMBC = "";
			boolean demandeComprise = false;
			
			if(contexte.equals(DEMANDE)){
				
				//Traitement pour l'api lab-bot-api
				ContractAnalyzer analyzer = new ContractAnalyzer();
				JSONObject js = analyzer.analyze(recastJson);
				if(js.getBoolean(SUCCESS)){
					JSonMemory.removeLastIntents(idUser);
					String rep;
					
					try {
						rep = apicontroller.sendGet(js.getString(FakeRecastKeys.URITOCALL.name()));
					} catch (Exception e) {
						rep = null;
					}
					responseToMBC = responsegenerator.generateContractUnderstoodMessage(analyzer.getCalledMethod(), analyzer.isChoice(), rep);
					demandeComprise = true;
					
				} else if(js.has(MISSINGPARAMS)){
					try {
						List<String> missingParams = new ArrayList<>(js.getJSONArray(MISSINGPARAMS).length());
						js.getJSONArray(MISSINGPARAMS).toList().forEach(e -> missingParams.add(e.toString()));
						if(missingParams.size()==1){
							responseToMBC = responsegenerator.generateMessageButMissOneArg(missingParams.get(0));
							demandeComprise = true;
						} else if(missingParams.size()>1){
							responseToMBC = responsegenerator.generateMessageButMissArgs(missingParams);
							demandeComprise = true;
						}
						
					} catch (Exception e){}
				
				}
			} else {
				// demande incomprise;
				// Traitement si on a d'autre contextes : transport, maps, ...
			}
			
			//On a pas réussi à traiter la demande l'utilisateur, on essaye de la compléter avec l'ancienne demande
			if(!demandeComprise){
				String stringLastIntent = JSonMemory.getLastIntents(idUser);
				if(stringLastIntent==null || stringLastIntent.isEmpty()){
					// Demande incomprise et il n'y a pas d'ancienne demande en attente, 
					// donc on arrete le traitement  et envoie une erreur a SS5
					JSonMemory.putLastIntents(idUser, recastJson.toString());
					responseToMBC = responsegenerator.generateNotUnderstoodMessage();
					nextToCall.sendMessage(mbc_json, responseToMBC);
					return;
				}
				//On essaye de completer l'ancienne demande présente avec les nouvelles données reçues
				JSONObject lastIntent = new JSONObject(stringLastIntent);
				JSONObject newRequest = generateNewRequestWithLastIntent(recastJson, lastIntent);
				JSonMemory.removeLastIntents(idUser);
				analyzeRecastIntents(mbc_json, newRequest);
			
			} else {
				if(responseToMBC.isEmpty()){
					nextToCall.sendMessage(mbc_json, responsegenerator.generateNotUnderstoodMessage());
				} else {
					nextToCall.sendMessage(mbc_json, responseToMBC);
				}
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
