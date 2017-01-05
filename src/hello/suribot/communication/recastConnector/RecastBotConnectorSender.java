package hello.suribot.communication.recastConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.interfaces.IRecastBotConnectorSender;
import hello.suribot.utils.EnvVar;

/**
 * Classe controleur permettant d'envoyer des messages à RBC (Recast Bot Connector)
 */
public class RecastBotConnectorSender extends AbstractHttpSender implements IRecastBotConnectorSender{
	
	/* (non-Javadoc)
	 * @see interfaces.IRecastBotConnectorSender#sendMessage(org.json.JSONObject, java.lang.String)
	 */
	@Override
	public void sendMessage(JSONObject json, String message){
		try {
			String idConv = json.getJSONObject("message").getString("conversation");
			callRecastBotConnector(message,idConv);
		} catch (JSONException e) {
			try {
				String idConv = json.getJSONObject("message").getString("conversation");
				callRecastBotConnector("Demande incomprise",idConv);
			} catch (Exception e1) {
				System.out.println("NodeJsMBCSender : Message "+message+" not send... ");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("NodeJsMBCSender : Message "+message+" not send... ");
			e.printStackTrace();
		}
	}
	
	//TODO: use AbstractHttpSender and extract tokens, parameters, ...
	private static void callRecastBotConnector(String text, String idConv) throws JSONException{
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";

        String idConversation = idConv;
        try {
        	obj = new URL("https://api-botconnector.recast.ai/users/"+EnvVar.RBCSLUG.getValue()+"/bots/"+EnvVar.RBCBOTID.getValue()+
            		"/conversations/"+idConversation+"/messages/");
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", EnvVar.RBCTOKEN.getValue());
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            text = URLEncoder.encode(text, "UTF-8");
            String message = "messages:[{"+
								  "type: 'text',"+
								  "content: '"+text+"',"+
								"}],";
            String parameter = "{"+message+"}";
            os = con.getOutputStream();
            os.write(new JSONObject(parameter).toString().getBytes());
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
            System.out.println(recastJson);
        } else {
           System.out.println(responseCode);
        }
	}
}