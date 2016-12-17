package hello.suribot.analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.contracts.ContractAnalyzer;
import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.ai.AiController;
import hello.suribot.communication.api.APIController;
import hello.suribot.communication.mbc.NodeJsMBCSender;
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


	public void analyzeRecastIntents(JSONObject mbc_json, JSONObject recastJson, String idUser, boolean firstTraitement) {
		
		String contexte = null;
		JSONObject entities = null;
		boolean isChoice = false;
		try{
			
			if(firstTraitement) contexte = AiController.getContext(recastJson);
			else contexte = recastJson.getString(JSonMemory.CONTEXTE);
			String responseToMBC = "";
			boolean demandeComprise = false;
			
			if(firstTraitement) entities = AiController.getEntities(recastJson);
			else entities = recastJson.getJSONObject(JSonMemory.ENTITIES);
			
			if(contexte != null && contexte.equals(CONTRAT)){
				
				//Traitement pour l'api lab-bot-api
				ContractAnalyzer analyzer = new ContractAnalyzer();

				JSONObject js = analyzer.analyze(entities, idUser);
				if(js.getBoolean(SUCCESS)){
					JSonMemory.removeLastEntities(idUser);
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
				if(firstTraitement){
					String stringLastIntent = JSonMemory.getLastEntities(idUser);
					if(stringLastIntent==null || stringLastIntent.isEmpty()){
						// Demande incomprise et il n'y a pas d'ancienne demande en attente, 
						// donc on arrete le traitement  et envoie une erreur a SS5
						recastJson = AiController.getEntities(recastJson);
						JSonMemory.putLastEntities(idUser, recastJson.toString());
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
					JSONObject newRequest = generateNewRequestWithLastEntities(recastJson, lastIntent);
					JSonMemory.removeLastEntities(idUser);
					if(contexte != null && !contexte.isEmpty()) newRequest.put(JSonMemory.CONTEXTE, contexte);
					analyzeRecastIntents(mbc_json, newRequest, idUser, false);
				}else{
					// Ce n'est pas le premier traitement de la demande de l'utilisateur
					
					// Demande incomprise et il n'y a pas d'ancienne demande en attente, 
					// donc on arrete le traitement  et envoie une erreur a SS5
					JSonMemory.putLastEntities(idUser, entities.toString());
					JSonMemory.putContext(idUser, contexte);
					if(responseToMBC.isEmpty()) responseToMBC = responsegenerator.generateNotUnderstoodMessage();
					nextToCall.sendMessage(mbc_json, responseToMBC);
					return;
				}
			} else { // demande comprise
				if(firstTraitement && isChoice){
					//si la demande est un choix on stocke la demande pour y ajouter eventuellement 
					entities = AiController.getEntities(recastJson);
					if(entities!=null) JSonMemory.putLastEntities(idUser, entities.toString());
					JSonMemory.putContext(idUser, contexte);
				}else{
					JSonMemory.removeLastEntities(idUser);
					JSonMemory.removeLastContexte(idUser);
				}
					
				if(responseToMBC.isEmpty()){
					nextToCall.sendMessage(mbc_json, responsegenerator.generateNotUnderstoodMessage());
				} else {
					nextToCall.sendMessage(mbc_json, responseToMBC);
				}
			}
		}catch(JSONException e){
			if(firstTraitement){
				//Il y a eu une exception lors de la lecture de la recuperation du contexte,
				//on essaye de completer une ancienne demande avec les nouvelles données
				String stringLastEntities = JSonMemory.getLastEntities(idUser);
				if(stringLastEntities==null||stringLastEntities.isEmpty()){
					//Demande incomprise et il n'y a pas d'ancienne demande en attente
					//donc on arrete le traitement  et envoie une erreur a SS5
					JSonMemory.putLastEntities(idUser, recastJson.toString());
					String responseToMBC = responsegenerator.generateNotUnderstoodMessage();
					nextToCall.sendMessage(mbc_json, responseToMBC);
					return;
				}
				JSONObject lastEntities = new JSONObject(stringLastEntities);
				JSONObject newRequest = generateNewRequestWithLastEntities(recastJson, lastEntities);
				JSonMemory.removeLastEntities(idUser);
				analyzeRecastIntents(mbc_json, newRequest, idUser, false);
			}else{
				//Demande incomprise et il n'y a pas d'ancienne demande en attente
				//donc on arrete le traitement  et envoie une erreur a SS5
				if(entities != null) JSonMemory.putLastEntities(idUser, entities.toString());
				JSonMemory.putContext(idUser, contexte);
				String responseToMBC = responsegenerator.generateNotUnderstoodMessage();
				nextToCall.sendMessage(mbc_json, responseToMBC);
			}
		}
	}
	

	private static JSONObject generateNewRequestWithLastEntities(JSONObject newDemande, JSONObject lastDemande){
		try{
			newDemande = AiController.getEntities(newDemande);
		}catch(JSONException e){
			newDemande = newDemande.getJSONObject(JSonMemory.CONTEXTE);
		}
		Set<String> keys = newDemande.keySet();
		for(String key: keys){
			//on insere les nouvelle données de la demande à la derniere demande incomprises
			//spour essayer de la completer
			lastDemande.put(key, newDemande.get(key));
		}
		JSONObject js = new JSONObject(lastDemande.toString());
		lastDemande.put(JSonMemory.ENTITIES, js);
		return lastDemande;
	}
}