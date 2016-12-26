package hello.suribot.communication.api;

import hello.suribot.abstracts.AbstractHttpSender;

/**
 * Classe permettant de communiquer avec différentes API externes
 */
public class APIController extends AbstractHttpSender{
	
	public APIController() {}
	
	/**
	 * Send message to api, and listen response
	 */
	public String send(String url){
		try {
			return sendGet(url);
		} catch (Exception e) {
			System.out.println("APIController : Message with url \""+url+"\" not send... ("+e+")");
		}
		return null;
	}

}