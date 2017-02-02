package hello.suribot.interfaces;

import java.util.List;

import hello.suribot.analyze.MissingAnalyzerParam;
import hello.suribot.response.Response;

public interface IResponseGenerator {
	
	/**
	 * Retourne une réponse positive par rapport à la demande utilisateur
	 * @param context
	 * @param calledMethod
	 * @param choice
	 * @param params
	 * @return la réponse
	 */
	Response generateUnderstoodMessage(String context, String calledMethod, boolean choice, String params);

	/**
	 * Retourne une réponse d'erreur interne du serveur
	 * @return la réponse
	 */
	Response generateInternalErrorMessage();

	/**
	 * Retourne une réponse négative par rapport à la demande utilisateur
	 * @return la réponse
	 */
	Response generateNotUnderstoodMessage();

	/**
	 * Retourne une réponse expliquant qu'un paramètre est manquant
	 * @param key
	 * @return la réponse
	 */
	Response generateMessageButMissOneArg(MissingAnalyzerParam key);

	/**
	 * Retourne une réponse expliquant que plusieurs paramètres sont manquants
	 * @param keys
	 * @return la réponse
	 */
	Response generateMessageButMissArgs(List<MissingAnalyzerParam> keys);

}