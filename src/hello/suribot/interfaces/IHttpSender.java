package hello.suribot.interfaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public interface IHttpSender {

	/**
	 * Send GET request and return response body
	 * @param url
	 * @return
	 * @throws Exception
	 */
	default String sendGet(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	/**
	 * Send POST request and return response body
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	default String sendPostAndReturnResponse(String url, String jsonResponse) throws Exception {
		System.out.println("sendPost");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    con.setRequestMethod("POST");
	    con.setDoOutput(true);
	    con.setRequestProperty("Content-Type", "application/json");
	    OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
	    out.write(jsonResponse.toString());
	    out.close();
	    
	    String response = null;
	    int HttpResult = con.getResponseCode();  
	    if(HttpResult == HttpURLConnection.HTTP_OK){  
	    	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
	    	String line = null;  
	    	StringBuilder sb = new StringBuilder();
	    	while ((line = br.readLine()) != null) {  
	    		sb.append(line + "\n");  
	    	}  
	    	br.close();  

	    	System.out.println(""+sb.toString());  
	    	response = sb.toString();

	    }else{  
	    	System.out.println(con.getResponseMessage());  
	    }  

	    return response;
	}
	
	/**
	 * 
	 * @param url
	 * @param jsonResponse
	 * @throws Exception
	 */
	default void sendPost(String url, JSONObject jsonResponse) throws Exception {
		System.out.println("sendPost");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    con.setRequestMethod("POST");
	    con.setDoOutput(true);
	    con.setRequestProperty("Content-Type", "application/json");
	    OutputStreamWriter out = new  OutputStreamWriter(con.getOutputStream());
	    out.write(jsonResponse.toString());
	    out.flush();
	    out.close();
	    con.getResponseCode(); // send the request
	    System.out.println("envoyé");
	}

}
