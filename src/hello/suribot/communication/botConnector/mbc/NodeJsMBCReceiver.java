package hello.suribot.communication.botConnector.mbc;

import java.io.BufferedReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.suribot.communication.ai.AiController;
import hello.suribot.interfaces.IAiController;

/**
 * Classe controleur permettant d'écouter des messages venant du programme Node.js de communication à MBC
 */
@RequestMapping("mbc")
@RestController
public class NodeJsMBCReceiver{
	
	private static final Logger logger = LogManager.getLogger();
	
	private IAiController nextStep;

	public NodeJsMBCReceiver() {
		this.nextStep = new AiController();
	}
	
	@RequestMapping(value ="/")
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
		    printUserMessage(json);
		    
	    	String idUser = json.getJSONObject("user").getString("id").split(":")[0];
	    	nextStep.sendMessage(json, json.getString("text"), idUser);
		    
	    } catch (JSONException e){
	    	logger.info("No user message but a request has been received : ");
	    	logger.info(sb.toString());
	    } catch (Exception e){
	    	logger.error(e);
	    	return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	    }
	    return HttpServletResponse.SC_OK;
	}
	
	private void printUserMessage(JSONObject json) throws JSONException {
		logger.info("User message : "+json.getString("text"));
	}
	
}