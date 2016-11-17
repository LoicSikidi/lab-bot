package hello.suribot.analyze;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.api.APIController;
import hello.suribot.communication.mbc.NodeJsMBCSender;
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

	public void analyzeRecastIntents(JSONObject json, String intents) {
		System.out.println("IntentsAnalyzer analyzeRecastIntents");
		//TODO: get url and parameters
		String apiResponse = null;
		if(intents!=null) apiResponse = apicontroller.sendMessageAndReturnResponse("", intents);
		
		String generatedResponse = responsegenerator.generateUnderstoodMessage(apiResponse);
		
		nextToCall.sendMessage(json, generatedResponse);
	}
	
	public static JSONObject generateNewRequestWithLastIntent(JSONObject newDemande, JSONObject lastDemande){

		String[] keys = JSONObject.getNames(newDemande);
		
		for(String key: keys){
			//on insere les nouvelle données de la demande à la derniere demande incomprises pour essayer de la completer
			lastDemande.put(key, newDemande.getString(key));
		}
		
		return lastDemande;
	}
	
	public static void writeDemandeIncomplete(JSONObject js){
		String idUser = js.getString(JSONKey.IDUSER.getName());
		try (FileWriter file = new FileWriter(idUser+".txt")) {
			file.write(js.toString());
		} catch (IOException e) {
			System.out.println("impossible de créer le fichier");
		}
	}
	
	public static String getIdContratUserByIdSlack(String idUser){
		Path path = Paths.get(idUser+".txt");
		Charset charset = Charset.forName("UTF-8");
		String idContrat="";
		try {
			List<String> lines = Files.readAllLines(path, charset);
			for (String line : lines) {
				JSONObject js = new JSONObject(line); 
				try{
					idContrat = js.getString(JSONKey.IDENTIFICATION.name());
				} catch(JSONException e) {
					return "";
				}
	      }
		} catch (IOException e1) {
			return "";
		}
		return idContrat;
	}

	public static JSONObject getDataPreviousDemande(String idUser) {
		Path path = Paths.get(idUser+".txt");
		Charset charset = Charset.forName("UTF-8");
		JSONObject js = new JSONObject();
		try {
			List<String> lines = Files.readAllLines(path, charset);
			return new JSONObject(lines.get(0));
		} catch (IOException | JSONException e) {
			return js;
		}
	}

	public static String getIDContratUserAndStockIt(JSONObject json){
		String idContrat="";
		try{
			idContrat=json.getString(JSONKey.IDENTIFICATION.name());
			writeIdContratUserInFile(idContrat,json.getString(JSONKey.IDUSER.getName()));
			//ecrire le nouvel identifiant de contrat dans le fichier de l'utilisateur
		}catch(JSONException e2){
			//Recast n'a pas récupéré d'identifiant de contrat on essaye de le rechercher dans de precedente demande
			idContrat=getIdContratUserByIdSlack(json.getString(JSONKey.IDUSER.getName()));
		}
		return idContrat;
	}
	
	private static void writeIdContratUserInFile(String idContrat, String idUser) {
		Path path = Paths.get(idUser+".txt");
		Charset charset = Charset.forName("UTF-8");
		JSONObject js = new JSONObject(); 
		try {
			List<String> lines = Files.readAllLines(path, charset);
			for (String line : lines) {
				js = new JSONObject(line); 
			}
			js.put(JSONKey.IDENTIFICATION.name(),idContrat);
			try (FileWriter file = new FileWriter(idUser+".txt")) {
				file.write(js.toString());
			} catch (IOException e) {
				System.out.println("impossible de créer le fichier");
			}
		} catch (IOException e1) {
			try (FileWriter file = new FileWriter(idUser+".txt")) {
				file.write(js.put(JSONKey.IDENTIFICATION.name(),idContrat).toString());
			} catch (IOException e) {
				System.out.println("impossible de créer le fichier");
			}
		}
	}

	public static void interpretIntentRecast(JSONObject recastJson){
		String idUser = recastJson.getString(JSONKey.IDUSER.getName());
		try{
			String contexte = recastJson.getString(JSONKey.CONTEXTE.getName());
			Boolean demandeComprise = false;
			if(contexte.equals("demande")){
				//Traitement pour l'api lab-bot-api
				JSONObject js = ContractAnalyzer.contractAnalyser(recastJson);
				if(js.getBoolean("success")){
					JSonMemory.removeLastIntents(idUser);
					APIController api = new APIController();
					String rep = api.sendMessageAndReturnResponse(js.getString(JSONKey.URITOCALL.name()), "");
					ResponseGenerator reponseGenerator= new ResponseGenerator();
					String responseToMBC= reponseGenerator.generateUnderstoodMessage(rep);
					System.out.println("responseToMBC = "+responseToMBC);
					demandeComprise= true;
				}
			}else if(true){
				
				//Traitement si on a d'autre contexte transport, ...
			}
			
			//On a pas réussi à traiter la demande l'utilisateur on essaye de la compléter
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
				interpretIntentRecast(newRequest);
			}
			//ENVOY2 A SS4
		}catch(JSONException e){
			//Il n'y a eu une exception lors de la lecture de la recuperation du contexte
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
			interpretIntentRecast(newRequest);
			//CALL SS5
		}
	}

}
