package restAPI.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceivingMessagesController {
	
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
	
} 
