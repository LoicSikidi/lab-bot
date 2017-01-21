package hello.suribot.response.contracts;

import hello.suribot.response.Response;

public interface IContractResponseGenerator {
	
	//TODO: javadoc
	Response generateContractMessage(String calledMethod, boolean choice, String params) throws IllegalArgumentException;

}