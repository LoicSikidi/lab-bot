package hello.suribot.communication.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonElement;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.interfaces.IHttpSender;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant d'écouter et envoyer des messages à Recast.ai
 */
public class AiController implements IHttpSender{
	
	private IntentsAnalyzer nextStep;

	public AiController() {
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
			JSONObject intents = new JSONObject();
			if(message.toLowerCase().contains("api")){
				AIConfiguration ai = new AIConfiguration(EnvVar.TOKENAPIAI);
				AIRequest request = new AIRequest(message);
				AIDataService data = new AIDataService(ai);
				AIResponse response = data.request(request);
				intents = apiAiToRecast(response);
			}else{
				intents = callRecast(message, EnvVar.TOKENRECAST, "fr");
			}
			nextStep.analyzeRecastIntents(json, intents, idUser, true);
			
		} catch (Exception e) {
			System.out.println("RecastAiController : Message "+message+" not send... ("+e+")");
		}
	}

	//TODO : utiliser l'interface IHttpSender ?
	private static JSONObject callRecast(String text, String token, String language) throws JSONException{
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";

        try {
            obj = new URL("https://api.recast.ai/v2/request");
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization",  "Token " + token);
            con.setDoOutput(true);
            text = "text=" + text;
            os = con.getOutputStream();
            os.write(text.getBytes());
			if (language != null) {
				String l = "&language=" + language;
				os.write(l.getBytes());
			}
            os.flush();
            os.close();

            responseCode = con.getResponseCode();
        } catch (MalformedURLException e) {
        	System.out.println("URL Malformed");
        } catch (IOException e) {
        	System.out.println("IOException");
        }

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                responseBuffer = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    responseBuffer.append(inputLine);
                }
                reader.close();
            } catch (IOException e) {
	        	System.out.println("IOException");
            }
            recastJson = responseBuffer.toString();
        } else {
            System.out.println(responseCode);
        }
        return new JSONObject(recastJson);
	}
	
	public static String getContext(JSONObject recastJson){
		JSONObject jsonResult = null;
		if(recastJson != null){
			try{
				jsonResult = new JSONObject();
				jsonResult = (JSONObject) recastJson.get(RecastKey.RESULTS);
				JSONArray ja = (JSONArray) jsonResult.get(RecastKey.INTENTS);
				if(ja != null) return ja.getJSONObject(0).getString(RecastKey.KEYINTENT);
			}catch(JSONException e){
				return null;
			}
		}
		return null;
	}
	
	public static JSONObject getEntities(JSONObject recastJson){
		JSONObject jsonResult = new JSONObject();
		if(recastJson != null){
			jsonResult = (JSONObject) recastJson.get(RecastKey.RESULTS);
			jsonResult = (JSONObject) jsonResult.get(RecastKey.ENTITIES);
			return jsonResult;
		}
		return null;
	}
	
	/////////////PARTIE CONVERTISSEUR/////////////////
	/**
	 * Transforme le JSON reçu de API.Ai en un JSON de la forme de ceux envoyé par Recast.
	 * JSONObject :
	 * {"intents" : JSONArray[
	 * 				JSONObject{ "slug" : String}
	 * 			]
		"entities" : JSONObject{
			entities1 : JSONArray[
				JSONObject { "raw" : String }
					]
			entities2 : JSONArray[
				JSONObject { "raw" : String }
					]
			entitiesN : JSONArray[
				JSONObject { "raw" : String }
					]
			}
	  }
	 */
	public static JSONObject apiAiToRecast(AIResponse response){
		
		HashMap<String, JsonElement> parameter = response.getResult().getParameters();
		JSONObject js = new JSONObject();
		JSONObject tmp = new JSONObject();
		JSONObject result = new JSONObject();
		JSONObject entities = apiIntentsToRecastIntents(parameter);
		
		JSONArray arrayIntent = new JSONArray();

		tmp.put(RecastKey.KEYINTENT, response.getResult().getMetadata().getIntentName());
		arrayIntent.put(tmp);
		result.put(RecastKey.INTENTS, arrayIntent);
		result.put(RecastKey.ENTITIES, entities);
		result.put(RecastKey.LANGUAGE, "fr");	//On met fr en langue par defaut car API.ai ne retourne pas la langue du message
		js.put("results", result);
		
		return js;
	}
	
	private static JSONObject apiIntentsToRecastIntents(HashMap<String, JsonElement> parameter){
		JSONObject tmp = new JSONObject();
		JSONObject entities = new JSONObject();
		JSONArray arrayEntitiesTmp = new JSONArray();
		if(parameter!=null){
			for(String elem : parameter.keySet()){
				//Dans la suite du code on utilise l'attribut raw de l'intent
				tmp.put(RecastKey.KEYENTITIES, parameter.get(elem).getAsString());	arrayEntitiesTmp.put(tmp);
				//On reinitialise le JSONObject
				tmp = new JSONObject();								
				entities.put(elem, arrayEntitiesTmp);
				//On reinitialise le JSONArray
				arrayEntitiesTmp = new JSONArray();
			}
		}
		return entities;
	}
}