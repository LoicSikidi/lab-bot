package hello.suribot.communication.recastConnector;

import java.io.BufferedReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.suribot.communication.ai.AiController;

/**
 * Classe controleur permettant d'écouter des messages venant du programme Node.js de communication à MBC
 */
//TODO: Décommenter lors du passage de mbc a recast
//@RequestMapping("mbc")
//@RestController
class ReceiveMessageRecast{
	
	private AiController nextStep;

	public ReceiveMessageRecast() {
		this.nextStep = new AiController();
	}
	
	//@RequestMapping(value ={"/",""})
	public int receivingMessage(HttpServletRequest request){
		StringBuilder sb = null;
	    try {
			Reader body = request.getReader();
			BufferedReader reader = new BufferedReader(body);
			sb = new StringBuilder();
		    int cp;
		    while ((cp = reader.read()) != -1) {
		      sb.append((char) cp);
		    }
		    JSONObject json = new JSONObject(sb.toString());
		    System.out.println(json);
		    printUserMessage(json); // TODO: retirer à la fin de tests
		    
		    new Thread(() -> { // async call
		    	String idUser = json.getString("senderId"); // TODO : à améliorer
		    	String message = json.getJSONObject("message").getJSONObject("attachment").getString("content");
		    	nextStep.sendMessage(json, message, idUser);
		    }).start();
		   
		    
	    } catch (JSONException e){
	    	e.printStackTrace();
	    	System.out.println("No user message but a request has been received : ");
	    	System.out.println(sb.toString());
	    } catch (Exception e){
	    	e.printStackTrace();
	    	return HttpStatus.SC_INTERNAL_SERVER_ERROR;
	    }
	    return HttpStatus.SC_OK;
	}
	
	private void printUserMessage(JSONObject json) throws JSONException {
		System.out.println("User message : "+json.getJSONObject("message").getJSONObject("attachment").getString("content"));
	}
	
}