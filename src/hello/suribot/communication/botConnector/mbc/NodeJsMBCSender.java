package hello.suribot.communication.botConnector.mbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.interfaces.IBotConnectorSender;
import hello.suribot.response.Response;
import hello.suribot.response.ResponseGenerator;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant d'envoyer des messages au programme Node.js de communication à MBC
 */
public class NodeJsMBCSender extends AbstractHttpSender implements IBotConnectorSender{

	private static final Logger logger = LogManager.getLogger();

	@Override
	public boolean sendMessage(JSONObject json, Response message){
		logger.info("NodeJsMBCSender : start sendMessage");
		try {
			json.put("text", generateRequest(message));
			callNodeJsMBC(json);
			return true;
		} catch (JSONException e) {
			try {
				json.put("text", new ResponseGenerator().generateInternalErrorMessage());
				callNodeJsMBC(json);
			} catch (Exception e1) {
				logger.error("Message "+message+" not send... \n"+e.getStackTrace());
			}
		} catch (Exception e) {
			logger.error("Message "+message+" not send... \n"+e.getStackTrace());
		}
		return false;
	}
	
	private void callNodeJsMBC(JSONObject json) throws Exception {
		sendPost("http://localhost:"+EnvVar.NODEJSPORT+"/mbc", json);
	}

	private String generateRequest(Response resp){
		if(resp!=null){
			String separator = "\n\n";
			String response = null;
			if(resp.getMessage()!=null) response += resp.getMessage();
			if(resp.getListChoice()!=null){
				for(String choice : resp.getListChoice()){
					if(choice!=null) response += separator+choice;
				}
			}
			if(response!=null) return response;
		}
		return new ResponseGenerator().generateInternalErrorMessage().getMessage();
	}

}