package hello.suribot.communication.api;

import java.io.IOException;

import hello.suribot.abstracts.AbstractHttpSender;
import hello.suribot.interfaces.IAPIController;

/**
 * Classe permettant de communiquer avec différentes API externes
 */
public class APIController extends AbstractHttpSender implements IAPIController{
	
	public APIController() {}
	
	/* (non-Javadoc)
	 * @see hello.suribot.communication.api.IAPIController#send(java.lang.String)
	 */
	@Override
	public String send(String url) throws IOException{
		return sendGet(url);
	}

}