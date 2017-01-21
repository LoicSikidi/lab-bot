package hello.suribot.communication.recastConnector;

import java.io.BufferedReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.suribot.communication.ai.AiController;
import hello.suribot.interfaces.IAiController;

/**
 * Classe controleur permettant d'écouter des messages venant de RBC (Recast Bot Connector)
 */
@RequestMapping("rbc")
@RestController
class RecastBotConnectorReceiver {
	
	private static final Logger logger = LogManager.getRootLogger();
	private static final Logger logger2 = LogManager.getLogger();
	private static final Logger logger3 = LogManager.getFormatterLogger();
	
	private IAiController nextStep;

	public RecastBotConnectorReceiver() {
		this.nextStep = new AiController();
	}
	
	@RequestMapping(value ={"/",""})
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
		    logger.info("1"+json);
		    logger2.info("2"+json);
		    logger3.info("3"+json);
		    printUserMessage(json);
		    
	    	String idUser = json.getString("senderId");
	    	String message = json.getJSONObject("message").getJSONObject("attachment").getString("content");
	    	nextStep.sendMessage(json, message, idUser);
		    
	    } catch (JSONException e){
	    	logger.info("1No user message but a request has been received : ");
	    	logger.info(sb.toString());
	    	logger2.info("2No user message but a request has been received : ");
	    	logger2.info(sb.toString());
	    	logger3.info("3No user message but a request has been received : ");
	    	logger3.info(sb.toString());
	    } catch (Exception e){
	    	e.printStackTrace();
	    	return HttpStatus.SC_INTERNAL_SERVER_ERROR;
	    }
	    return HttpStatus.SC_OK;
	}
	
	private void printUserMessage(JSONObject json) throws JSONException {
		logger.info("User message : "+json.getJSONObject("message").getJSONObject("attachment").getString("content"));
	}
	
}