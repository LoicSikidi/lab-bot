package restAPI.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.bots4j.msbotframework.beans.ChannelAccount;
import org.bots4j.msbotframework.beans.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.msbotframework.HelloSuribotConnectorClient;

@RestController
public class ReceivingMessagesController {
	
	// TODO To change every hour : make a daemon to update the token, and put it in the cache
	private static final String token = "";
	
	private static final String appid = System.getenv("APPID");
	private static final String appsecret = System.getenv("APPSECRET");
	
	@RequestMapping(value ="/")
	public int receivingMessage(HttpServletRequest request){
	    
		JSONObject json = null;
	    try {
			Reader body = request.getReader();
			BufferedReader reader = new BufferedReader(body);
			StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = reader.read()) != -1) {
		      sb.append((char) cp);
		    }
		    json = new JSONObject(sb.toString());
		    printUserMessage(json);
		    
		    System.out.println("\n====sendResponse=====\n");
		    HelloSuribotConnectorClient client = new HelloSuribotConnectorClient(appid, appsecret, token);
		    Message message = new Message()
		            .withFrom(from())
		            .withTo(to())
		            .withText("This is a test message from Hello Suribot")
		            .withLanguage("en");

		    printEntireJSONMessage(json);
		    Message reply = client.Messages.sendMessage(message);
		    System.out.println("Response : "+reply.getText());
		    
	    } catch (JSONException e){
	    	System.out.println("No user message but a request has been received.");
	    	printEntireJSONMessage(json);
	    } catch (RuntimeException | IOException e){
	    	e.printStackTrace();
	    	return HttpStatus.SC_UNAUTHORIZED;
	    }
			
	    return HttpStatus.SC_OK;
	}
	
	private void printEntireJSONMessage(JSONObject json){
		System.out.println("Entire JSON Body : \n"+ json.toString(3) );
	}
	
	private void printUserMessage(JSONObject json) throws JSONException {
		System.out.println("User message : "+json.getString("text"));
	}
	
	public static ChannelAccount to(){
        return new ChannelAccount()
                .withName("julien.margarido")
                .withId("U2LJMA17F")
//                .withAddress("julien.margarido")
//                .withChannelId("slack")
                .withIsBot(false);
    }

    public static ChannelAccount from(){
        return new ChannelAccount()
                .withName("lab_bot")
                .withId("B2S5SJQ3Y")
//                .withAddress("lab-bot")
//                .withChannelId("slack")
                .withIsBot(true);
    }
} 
