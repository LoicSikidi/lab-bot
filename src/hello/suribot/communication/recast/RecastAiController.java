package hello.suribot.communication.recast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.interfaces.IHttpSender;
import hello.suribot.utils.EnvVar;

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
			//TODO call doAPIRequest
			JSONObject intents = callRecast(message, EnvVar.TOKENRECAST, "fr");
			//JSONObject intents = fakeRecast(message, idUser);
			nextStep.analyzeRecastIntents(json, intents, idUser);
			
		} catch (Exception e) {
			System.out.println("RecastAiController : Message "+message+" not send... ("+e+")");
		}
	}

	public static JSONObject callRecast(String text, String token, String language) throws JSONException{
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
				jsonResult = (JSONObject) recastJson.get("results");
				JSONArray ja = (JSONArray) jsonResult.get("intents");
				if(ja != null) return ja.getJSONObject(0).getString("slug");
			}catch(JSONException e){
				return null;
			}
		}
		return null;
	}
	
	public static JSONObject getEntities(JSONObject recastJson){
		JSONObject jsonResult = new JSONObject();
		if(recastJson != null){
			jsonResult = (JSONObject) recastJson.get("results");
			jsonResult = (JSONObject) jsonResult.get("entities");
			return jsonResult;
		}
		return null;
	}
}