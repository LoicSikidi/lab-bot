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
		
		String generatedResponse = responsegenerator.generateMessage(apiResponse);
		
		nextToCall.sendMessage(json, generatedResponse);
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
		System.out.println(idUser);
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
		System.out.println(idUser);
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

	public static String interpretIntentRecast(JSONObject recastJson){
		String identifiant;
		String uriToCall="";
		try{
			String contexte = recastJson.getString(JSONKey.CONTEXTE.getName());
			if(contexte.equals("demande")){
				
				identifiant = getIDContratUserAndStockIt(recastJson);
				if(identifiant == ""){
					//stocker demande utilisateur
					writeDemandeIncomplete(recastJson);
					
					//puis appeler SS5
					
				}else{
					uriToCall+=PreferenceAPI.valueOf(contexte).getName()+identifiant+"/";
					String quelMethodeAppeler = recastJson.getString(JSONKey.QUOI.getName());
					if(quelMethodeAppeler.equals("risk")){
						uriToCall+=PreferenceAPI.valueOf(quelMethodeAppeler).getName();
						try{
							String complement =recastJson.getString(JSONKey.COMPLEMENT.getName());
							complement=(PreferenceAPI.IDOBJ.getName().replaceAll(PreferenceAPI.IDREPLACE.getName(), complement));
							uriToCall+=complement+"/";
						}catch (JSONException e2){
							
						}
					}else if(quelMethodeAppeler.equals("billings")||quelMethodeAppeler.equals("role")){
						uriToCall+=PreferenceAPI.valueOf(quelMethodeAppeler).getName();
						try{
							String complement =recastJson.getString(JSONKey.COMPLEMENT.getName());
							complement=(PreferenceAPI.IDBILLING.getName().replaceAll(PreferenceAPI.IDREPLACE.getName(), complement));
							uriToCall+=complement+"/";
						}catch (JSONException e2){
							
						}
					}else {
						JSONObject js = getDataPreviousDemande(recastJson.getString(JSONKey.IDUSER.name()));
						//Methode non comprise stocker demande et envoyer flag à SS5
					}
				}
			
			}else if(true){
				
				//Traitement si on a d'autre contexte transport, ...
			}
			
			//ENVOY2 A SS4
		}catch(JSONException e){
			//Il n'y a eu une exception lors de la lecture du json alors on stocke la demande
			writeDemandeIncomplete(recastJson);
			
			//CALL SS5
		}
		return uriToCall;
	}


}
