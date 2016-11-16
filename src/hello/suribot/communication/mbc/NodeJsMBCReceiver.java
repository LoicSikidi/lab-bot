package hello.suribot.communication.mbc;

import java.io.BufferedReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.suribot.communication.recast.RecastAiController;

/**
 * Classe controleur permettant d'écouter des messages venant du programme Node.js de communication à MBC
 */
@RequestMapping("mbc")
@RestController
class NodeJsMBCReceiver{
	
	private RecastAiController nextStep;

	public NodeJsMBCReceiver() {
		this.nextStep = new RecastAiController();
	}
	
	@RequestMapping(value ="/")
	public int receivingMessage(HttpServletRequest request){
		System.out.println("receive");
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
		    printUserMessage(json); // TODO: retirer à la fin de tests
		    
		    new Thread(() -> { // async call
		    	 nextStep.sendMessage(json, json.getString("text"));
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
		System.out.println("User message : "+json.getString("text"));
	}
	
}