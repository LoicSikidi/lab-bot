package hello.suribot.analyze.contracts;

import org.json.JSONObject;

public interface IContractAnalyzer {

	/**
	 * Prend en paramètre des entities, les analyses, et retourne une instruction sous forme de
	 * {@link JSONObject} (demande comprise ou non, url à éventuellement appeler, ...) 
	 * Exemple 1 :{"risk":[{"confidence":0.91,"raw":"couvertures","value":"couvertures"}]}
	 * Exemple 2 :{"prelevement-id":[{"confidence":0.39,"raw":"prélèvement 478855","value":"prélèvement 478855"}]}
	 * Exemple 3 :{"role":[{"confidence":0.82,"raw":"role","value":"role"}]}
	 * @param entities
	 * @param idUser
	 * @return
	 */
	JSONObject analyze(JSONObject entities, String idUser);

	/**
	 * Retourne le nom de la méthode à appeler, detectée par l'appel de la méthode analyse.
	 * @return calledMethod
	 */
	ContractParams getCalledMethod();

	/**
	 * Retourne vrai si l'appel à la méthode analyse détecte un choix à proposer à l'utilisateur
	 * @return choice
	 */
	boolean isChoice();

}