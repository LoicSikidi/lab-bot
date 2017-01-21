package hello.suribot.communication.mbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant d'envoyer des messages au programme Node.js de communication à MBC
 */
public class NodeJsMBCSender extends AbstractHttpSender{
	
	private static final Logger logger = LogManager.getLogger();
	
	public void sendMessage(JSONObject json, String message){
		try {
			json.put("text", message);
			sendPost("http://localhost:"+EnvVar.NODEJSPORT+"/mbc", json);
		} catch (JSONException e) {
			json.put("text", "Demande incomprise");
			try {
				sendPost("http://localhost:"+EnvVar.NODEJSPORT+"/mbc", json);
			} catch (Exception e1) {
				logger.error("NodeJsMBCSender : Message "+message+" not send... ");
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("NodeJsMBCSender : Message "+message+" not send... ");
			e.printStackTrace();
		}
	}

}