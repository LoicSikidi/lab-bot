package hello.suribot.communication.mbc;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant d'envoyer des messages au programme Node.js de communication à MBC
 */
public class NodeJsMBCSender extends AbstractHttpSender{
	
	public void sendMessage(JSONObject json, String message){
		try {
			json.put("text", message);
			sendPost("http://localhost:"+EnvVar.NODEJSPORT+"/mbc", json);
		} catch (JSONException e) {
			json.put("text", "Demande incomprise");
			try {
				sendPost("http://localhost:"+EnvVar.NODEJSPORT+"/mbc", json);
			} catch (Exception e1) {
				System.out.println("NodeJsMBCSender : Message "+message+" not send... ");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("NodeJsMBCSender : Message "+message+" not send... ");
			e.printStackTrace();
		}
	}

}