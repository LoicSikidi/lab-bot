package hello.suribot.communication.api;

import hello.suribot.interfaces.IHttpSender;

/**
 * Classe permettant de communiquer avec différentes API externes
 */
public class APIController implements IHttpSender{
	
	public APIController() {}
	
	/**
	 * Send message to api, and listen response
	 * @param message
	 */
	public String sendMessageAndReturnResponse(String url, String message){
		System.out.println("APIController sendMessageAndReturnResponse");
		try {
			//if(message != null) return sendPostAndReturnResponse(url, message);
			
			//TODO : return response to S.S.3 analyse des intents de Recast
			return null;
		} catch (Exception e) {
			System.out.println("APIController : Message "+message+" not send... ("+e+")");
		}
		return "";
	}

}