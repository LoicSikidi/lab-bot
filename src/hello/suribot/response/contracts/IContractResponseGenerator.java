package hello.suribot.response.contracts;

import hello.suribot.response.Response;

public interface IContractResponseGenerator {
	
	/**
	 * Retourne une réponse positive adaptée à la demande de l'utilisateur concernant les contrats
	 * @param calledMethod
	 * @param choice
	 * @param params
	 * @return la réponse
	 * @throws IllegalArgumentException
	 */
	Response generateContractMessage(String calledMethod, boolean choice, String params) throws IllegalArgumentException;

}