package hello.suribot.interfaces;

import java.io.IOException;

public interface IAPIController {

	/**
	 * Send message to api, and listen response
	 * @throws IOException 
	 */
	String send(String url) throws IOException;

}