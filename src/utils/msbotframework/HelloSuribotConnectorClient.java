package utils.msbotframework;

import java.io.IOException;
import java.net.URI;

import org.bots4j.msbotframework.beans.Message;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;

public class HelloSuribotConnectorClient {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private static final String BASE_URL = "https://api.botframework.com";
    private String baseUrl;
    private HttpRequestFactory requestFactory;
    private String appId;
    private String appSecret;
    
    public HelloSuribotMessages Messages = new HelloSuribotMessages();
    
    public HelloSuribotConnectorClient(String appId, String appSecret, String token){
    	this.appId = appId;
    	this.appSecret = appSecret;
    	this.baseUrl = BASE_URL;
    	requestFactory = createHttpRequestFactory(token);
    }

    protected <E> E postRequest(String path, Object body, Class<E> responseType){
        try {
            URI uri = uri(path);
            GenericUrl url = new GenericUrl(uri);

            HttpContent content = new JsonHttpContent(JSON_FACTORY,body);
            HttpRequest request = requestFactory.buildPostRequest(url,content);
            E response = (E) request.execute().parseAs(responseType);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URI uri(String path) {
        String str = baseUrl;
        if ( str.endsWith("/") && path.startsWith("/") ){
            str += path.substring(1);
        }
        else if (baseUrl.endsWith("/") || path.startsWith("/") ){
            str += path;
        }
        else {
            str += "/" + path;
        }
        return URI.create(str);
    }
    
    protected HttpRequestFactory createHttpRequestFactory(String token) {
        return HTTP_TRANSPORT.createRequestFactory(request -> {
            request.setParser(new JsonObjectParser(JSON_FACTORY));
            request.getHeaders().setAuthorization("Basic " + authentication());
            request.getHeaders().set("Bearer", token);
        });
    }

    private String authentication() {
        byte[] bytes = (appId + ":" + appSecret).getBytes();
        return Base64.encodeBase64String(bytes);
    }

    public class HelloSuribotMessages{

        private static final String MESSAGES_PATH = "/bot/v1.0/messages";

        public Message sendMessage(Message message){
            return postRequest(MESSAGES_PATH, message, Message.class);
        }

    }
}
