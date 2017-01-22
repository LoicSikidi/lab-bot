package hello.suribot.communication.recastConnector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.interfaces.IRecastBotConnectorSender;
import hello.suribot.response.Response;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant d'envoyer des messages à RBC (Recast Bot Connector)
 */
public class RecastBotConnectorSender extends AbstractHttpSender implements IRecastBotConnectorSender{
	
	private static final Logger logger = LogManager.getLogger();
	
	/* (non-Javadoc)
	 * @see interfaces.IRecastBotConnectorSender#sendMessage(org.json.JSONObject, java.lang.String)
	 */
	@Override
	//TODO: A modifier
	public boolean sendMessage(JSONObject json, Response response){
		String idConv = "";
		try {
			idConv = json.getJSONObject("message").getString("conversation");
			callRecastBotConnector(response,idConv);
			return true;
		} catch (JSONException e) {
			if(idConv.isEmpty()){ //Impossible de récupérer l'id de la conversation
				logger.info("NodeJsMBCSender : Message "+response.getMessage()+" not send... ");
			}else{  //Erreur lors de la création du JSON dans callRecastBotConnector on envoie donc un message à l'utilisateur
				try {
					callRecastBotConnector(new Response("Demande incomprise"),idConv);
				} catch (Exception e2) { //Impossible d'envoyer le message
					logger.info("NodeJsMBCSender : Message "+response.getMessage()+" not send... : "+e);
				}
			}
		} catch (Exception e) {
			logger.info("NodeJsMBCSender : Message "+response.getMessage()+" not send... : "+e);
		}
		return false;
	}
	
	private void callRecastBotConnector(Response response, String idConv) throws Exception {
		Map<String, String> property = new HashMap<String,String>();
		property.put("Authorization", EnvVar.RBCTOKEN.getValue());
		property.put("Content-Type", "application/json");
		String url = "https://api-botconnector.recast.ai/users/"+EnvVar.RBCSLUG.getValue()+"/bots/"+EnvVar.RBCBOTID.getValue()+
        		"/conversations/"+idConv+"/messages/";
		String message = generateRequest(response);
		JSONObject js = new JSONObject("{"+message+"}");
		sendPost(url,property,js);
	}
	
	private String generateRequest(Response resp){
		String separator = ",";
		String message= "messages:[{"+
				  "type: 'card'"+separator+
				  "content: {";
		
		String respMessage = resp.getMessage();
		if(respMessage!=null && !respMessage.isEmpty())
			try {
				message+="title: '"+URLEncoder.encode(respMessage, "UTF-8")+"'"+separator;
			} catch (UnsupportedEncodingException e) {
				message+="title: '"+respMessage+"'"+separator;
			}
		else message+="title: ''"+separator;

		String urlImage = resp.getUrlImage();
		if(urlImage!=null && !urlImage.isEmpty())
			try {
				message+="imageUrl: '"+URLEncoder.encode(urlImage, "UTF-8")+"'"+separator;
			} catch (UnsupportedEncodingException e) {
				message+="imageUrl: '"+urlImage+"'"+separator;
			}
		else message+="imageUrl: ''"+separator;
		
		List<String> listChoice = resp.getListChoice();
		if(listChoice!=null && !listChoice.isEmpty()){
			String listButton = "";
			for(String s : listChoice){
	    		try {
					s = URLEncoder.encode(s, "UTF-8");
				} catch (UnsupportedEncodingException e) { }
	    		listButton+="{title: '"+s+"'"+separator+
			                "value: '"+s+"'}"+separator;
	    	}
			message+="buttons: ["+listButton+"]";
		}else {
			message+="buttons: []";
		}
		message+="}}]";
		
		return message;
	}
}