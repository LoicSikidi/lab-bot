package hello.suribot.communication.recastConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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
	
	/* (non-Javadoc)
	 * @see interfaces.IRecastBotConnectorSender#sendMessage(org.json.JSONObject, java.lang.String)
	 */
	@Override
	//TODO: A modifier
	public void sendMessage(JSONObject json, Response response){
		try {
			String idConv = json.getJSONObject("message").getString("conversation");
			callRecastBotConnectorBis(response,idConv);
		} catch (JSONException e) {
			try {
				String idConv = json.getJSONObject("message").getString("conversation");
				callRecastBotConnector("Demande incomprise",idConv);
			} catch (Exception e1) {
				System.out.println("NodeJsMBCSender : Message "+response.getMessage()+" not send... ");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("NodeJsMBCSender : Message "+response.getMessage()+" not send... ");
			e.printStackTrace();
		}
	}
	
	//TODO: use AbstractHttpSender and extract tokens, parameters, ...
	public static void callRecastBotConnector(String text, String idConv) throws JSONException{
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";
        System.out.println("idConv =="+idConv);
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
            String message2 = "messages:[{"+
							  "type: 'quickReplies',"+
		            		  "content: {"+
								  "buttons: [{"+
								                "title: 'SUPER',"+
								                "value: 'STRING'"+
								             "},"+
								             "{"+
								                "title: 'SUPER',"+
								                "value: 'STRING'"+
								             "}" + 
								             "]"+
								      "},"+
							"}],";
            String parameter = "{"+message2+"}";
            System.out.println(parameter);
            System.out.println(new JSONObject(parameter).toString());
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
	
	//TODO: use AbstractHttpSender and extract tokens, parameters, ...
	public static void callRecastBotConnector(Response response, String idConv) throws JSONException {
		System.out.println("response");
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";
        System.out.println("idConv =="+idConv);
        String idConversation = idConv;
        String text = response.getMessage();
        List<String> listChoice = response.getListChoice();
        try {
        	obj = new URL("https://api-botconnector.recast.ai/users/"+EnvVar.RBCSLUG.getValue()+"/bots/"+EnvVar.RBCBOTID.getValue()+
            		"/conversations/"+idConversation+"/messages/");
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", EnvVar.RBCTOKEN.getValue());
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            text = URLEncoder.encode(text, "UTF-8");
            String message;
            message = "messages:[{"+
						  "type: 'text',"+
						  "content: '"+text+"',"+
						"},";
            if(listChoice!=null && !listChoice.isEmpty()){
            	message +="{"+
						  "type: 'quickReplies',"+
	            		  "content: {"+
							  "buttons: [";
            	for(String s : listChoice){
            		s = URLEncoder.encode(s, "UTF-8");
            		message+="{title: '"+s+"',"+
			                "value: '"+s+"'"+
			             "},";
            	}
            	message+="]}}";
            }
            message+="]";
            String parameter = "{"+message+"}";
            System.out.println(parameter);
            System.out.println(new JSONObject(parameter).toString());
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
	
	public static void callRecastBotConnectorImage(Response response, String idConv) throws JSONException{
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";
        System.out.println("idConv =="+idConv);
        String idConversation = idConv;
        String text = response.getMessage();
        try {
        	obj = new URL("https://api-botconnector.recast.ai/users/"+EnvVar.RBCSLUG.getValue()+"/bots/"+EnvVar.RBCBOTID.getValue()+
            		"/conversations/"+idConversation+"/messages/");
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", EnvVar.RBCTOKEN.getValue());
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            text = URLEncoder.encode(text, "UTF-8");
            String message;
            message = "messages:[{"+
						  "type: 'picture',"+
						  "content: '"+text+"',"+
						"},";
            message+="]";
            
            String parameter = "{"+message+"}";
            System.out.println(parameter);
            System.out.println(new JSONObject(parameter).toString());
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
	
	public static void callRecastBotConnectorCard(Response response, String idConv) throws JSONException{
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";
        System.out.println("idConv =="+idConv);
        String idConversation = idConv;
        String text = response.getMessage();
        try {
        	obj = new URL("https://api-botconnector.recast.ai/users/"+EnvVar.RBCSLUG.getValue()+"/bots/"+EnvVar.RBCBOTID.getValue()+
            		"/conversations/"+idConversation+"/messages/");
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", EnvVar.RBCTOKEN.getValue());
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            text = URLEncoder.encode(text, "UTF-8");
            String message;
            message = "messages:[{"+
						  "type: 'card',"+
						  "content: {"+
						  "title: toto,"+
						  	"imageUrl: 'http://s11.favim.com/orig/160919/forum-communaute-topic-9808-hd-putlocker-watch-hell-or-high-water-online-full-movie-1080p-Favim.com-4740061.jpeg',"+
						  	"buttons: [ "+ 
						  		"{"+
							  		"title: 'SEE ON THE LEFT',"+
							  		"value: 'SEE ON THE LEFT'"+
							  	"},"+
							  	"{"+
							  		"title: 'SEE ON THE Center',"+
							  		"value: 'SEE ON THE LEFT'"+
							  	"},"+
							  	"{"+
							  		"title: 'SEE ON THE Center',"+
							  		"value: 'SEE ON THE LEFT'"+
							  	"},"+
						  		"{"+
							  		"title: 'SEE ON THE RIGHT',"+
							  		"value: 'SEE ON THE RIGHT'"+
							  	"} "+
					  		"]"+
						"},";
            message+="},]";

            String parameter = "{"+message+"}";
            System.out.println(parameter);
            System.out.println(new JSONObject(parameter).toString());
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

	public static void callRecastBotConnectorBis(Response response, String idConv) throws JSONException {
        URL	obj;
        HttpsURLConnection	con = null;
        OutputStream os;
        int	responseCode = 0;
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        String recastJson = "";
        System.out.println("idConv =="+idConv);
        String idConversation = idConv;
        try {
        	obj = new URL("https://api-botconnector.recast.ai/users/"+EnvVar.RBCSLUG.getValue()+"/bots/"+EnvVar.RBCBOTID.getValue()+
            		"/conversations/"+idConversation+"/messages/");
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", EnvVar.RBCTOKEN.getValue());
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            String message = generateRequest(response);
            String parameter = "{"+message+"}";
            System.out.println(parameter);
            System.out.println(new JSONObject(parameter).toString());
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
	
	private static String generateRequest(Response resp) throws UnsupportedEncodingException{
		String message= "messages:[{"+
				  "type: 'card',"+
				  "content: {";
		
		String respMessage = resp.getMessage();
		if(respMessage!=null && !respMessage.isEmpty())message+="title: '"+URLEncoder.encode(respMessage, "UTF-8")+"',";
		else message+="title: '',";

		String urlImage = resp.getUrlImage();
		if(urlImage!=null && !urlImage.isEmpty()) message+="imageUrl: '"+URLEncoder.encode(urlImage, "UTF-8")+"',";
		else message+="imageUrl: '',";
		
		List<String> listChoice = resp.getListChoice();
		if(listChoice!=null && !listChoice.isEmpty()){
			String listButton = "";
			for(String s : listChoice){
	    		s = URLEncoder.encode(s, "UTF-8");
	    		listButton+="{title: '"+s+"',"+
			                "value: '"+s+"'},";
	    	}
			message+="buttons: ["+listButton+"]";
		}else {
			message+="buttons: []";
		}

		message+="}}]";
		
		return message;
	}
}