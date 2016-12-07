package hello.suribot.analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.contracts.ContractAnalyzer;
import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.api.APIController;
import hello.suribot.communication.mbc.NodeJsMBCSender;
import hello.suribot.communication.recast.RecastAiController;
import hello.suribot.interfaces.IJsonDecoder;
import hello.suribot.response.ResponseGenerator;

public class IntentsAnalyzer implements IJsonDecoder{
	
	private NodeJsMBCSender nextToCall;
	
	private ResponseGenerator responsegenerator;
	private APIController apicontroller;
	
	public static final String SUCCESS = "success";
	public static final String MISSINGPARAMS = "missingparams";
	public static final String CONTRAT = "contrat";
	

	public IntentsAnalyzer() {
		this.responsegenerator = new ResponseGenerator();
		this.apicontroller = new APIController();
		this.nextToCall = new NodeJsMBCSender();
	}

	public void analyzeRecastIntents(JSONObject mbc_json, JSONObject recastJson, String idUser) {
		
		String contexte;
		boolean isChoice = false;
		try{
			contexte = RecastAiController.getContext(recastJson);
			String responseToMBC = "";
			boolean demandeComprise = false;
			
			if(contexte != null && contexte.equals(CONTRAT)){
				
				//Traitement pour l'api lab-bot-api
				ContractAnalyzer analyzer = new ContractAnalyzer();
				JSONObject entities = RecastAiController.getEntities(recastJson);
				JSONObject js = analyzer.analyze(entities, idUser);
				if(js.getBoolean(SUCCESS)){
					JSonMemory.removeLastIntents(idUser);
					String rep;
					
					try {
						rep = apicontroller.sendGet(js.getString(ApiUrls.URITOCALL.name()));
					} catch (Exception e) {
						rep = null;
					}
					isChoice=analyzer.isChoice();
					responseToMBC = responsegenerator.generateContractUnderstoodMessage(analyzer.getCalledMethod(), isChoice, rep);
					demandeComprise = true;
				}else if(js.has(MISSINGPARAMS)){
					try {
						List<String> missingParams = new ArrayList<>(js.getJSONArray(MISSINGPARAMS).length());
						js.getJSONArray(MISSINGPARAMS).toList().forEach(e -> missingParams.add(e.toString()));
						if(missingParams.size()==1){
							responseToMBC = responsegenerator.generateMessageButMissOneArg(missingParams.get(0));
						} else if(missingParams.size()>1){
							responseToMBC = responsegenerator.generateMessageButMissArgs(missingParams);
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
					recastJson = RecastAiController.getEntities(recastJson);
					JSonMemory.putLastIntents(idUser, recastJson.toString());
					if(contexte != null && !contexte.isEmpty()) JSonMemory.putContext(idUser, contexte);
					if(responseToMBC.isEmpty()){
						nextToCall.sendMessage(mbc_json, responsegenerator.generateNotUnderstoodMessage());
						return;
					} else {
						nextToCall.sendMessage(mbc_json, responseToMBC);
						return;
					}
				}
				//On essaye de completer l'ancienne demande présente avec les nouvelles données reçues
				JSONObject lastIntent = new JSONObject(stringLastIntent);
				JSONObject newRequest = generateNewRequestWithLastIntent(recastJson, lastIntent);
				JSonMemory.removeLastIntents(idUser);
				if(contexte != null && !contexte.isEmpty()) newRequest.put("contexte", contexte);
				analyzeNotUnderstood(mbc_json, newRequest, idUser);
				
			} else {
				if(isChoice){
					JSONObject entities = RecastAiController.getEntities(recastJson);
					if(entities!=null)JSonMemory.putLastIntents(idUser, entities.toString());
					JSonMemory.putContext(idUser, contexte);
				}else{
					JSonMemory.removeLastIntents(idUser);
					JSonMemory.removeLastContexte(idUser);
				}
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
			analyzeNotUnderstood(mbc_json, newRequest, idUser);
		}
	}
	
	public void analyzeNotUnderstood(JSONObject mbc_json, JSONObject newDemande, String idUser){
		String contexte = "";
		JSONObject entities = null;
		try{
			contexte = newDemande.getString("contexte");
			String responseToMBC = "";
			boolean demandeComprise = false;

			entities = newDemande.getJSONObject("entities");
			
			if(contexte != null && contexte.equals(CONTRAT)){
				
				//Traitement pour l'api lab-bot-api
				ContractAnalyzer analyzer = new ContractAnalyzer();
				JSONObject js = analyzer.analyze(entities, idUser);
				if(js.getBoolean(SUCCESS)){
					JSonMemory.removeLastIntents(idUser);
					String rep;
					
					try {
						rep = apicontroller.sendGet(js.getString(ApiUrls.URITOCALL.name()));
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
						} else if(missingParams.size()>1){
							responseToMBC = responsegenerator.generateMessageButMissArgs(missingParams);
						}
						
					} catch (Exception e){}
				
				}
			} else {
				// demande incomprise;
				// Traitement si on a d'autre contextes : transport, maps, ...
			}
			
			if(!demandeComprise){
				// Demande incomprise et il n'y a pas d'ancienne demande en attente, 
				// donc on arrete le traitement  et envoie une erreur a SS5
				JSonMemory.putLastIntents(idUser, entities.toString());
				JSonMemory.putContext(idUser, contexte);
				if(responseToMBC.isEmpty()) responseToMBC = responsegenerator.generateNotUnderstoodMessage();
				nextToCall.sendMessage(mbc_json, responseToMBC);
				return;
				
			} else {
				JSonMemory.removeLastIntents(idUser);
				JSonMemory.removeLastContexte(idUser);
				if(responseToMBC.isEmpty()){
					nextToCall.sendMessage(mbc_json, responsegenerator.generateNotUnderstoodMessage());
				} else {
					nextToCall.sendMessage(mbc_json, responseToMBC);
				}
			}
		}catch(JSONException e){
			//Demande incomprise et il n'y a pas d'ancienne demande en attente
			//donc on arrete le traitement  et envoie une erreur a SS5
			if(entities != null) JSonMemory.putLastIntents(idUser, entities.toString());
			JSonMemory.putContext(idUser, contexte);
			String responseToMBC = responsegenerator.generateNotUnderstoodMessage();
			nextToCall.sendMessage(mbc_json, responseToMBC);
			return;
		}
		
	}
	
	public static JSONObject generateNewRequestWithLastIntent(JSONObject newDemande, JSONObject lastDemande){
		try{
			newDemande = RecastAiController.getEntities(newDemande);
		}catch(JSONException e){
			newDemande = newDemande.getJSONObject("entities");
		}
		Set<String> keys = newDemande.keySet();
		for(String key: keys){
			//on insere les nouvelle données de la demande à la derniere demande incomprises
			//spour essayer de la completer
			lastDemande.put(key, newDemande.get(key));
		}
		JSONObject js = new JSONObject(lastDemande.toString());
		lastDemande.put("entities", js);
		return lastDemande;
	}
}
