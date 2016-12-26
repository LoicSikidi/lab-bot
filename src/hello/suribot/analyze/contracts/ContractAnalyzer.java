package hello.suribot.analyze.contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.ApiUrls;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.jsonmemory.JSONMemory;
import hello.suribot.communication.ai.keys.RecastKeys;
import hello.suribot.communication.ai.keys.SuribotKeys;

/**
 * Classe d'analyse des intents dans le contexte des "contrats"
 */
public class ContractAnalyzer {

	private ContractParams calledMethod;
	private boolean choice;

	// Constantes fortement liées au paramétrage du moteur d'intelligence (Recast, API.ai, ...).
	private static final String COUVERTURE = "risk";
	private static final String COMPLEM_COUVERTURE = "object-id";
	private static final String ROLE = "role";
	private static final String COMPLEM_ROLE = "person-id";
	private static final String PAIEMENT = "prelevement";
	private static final String COMPLEM_PAIEMENT = "prelevement-id";
	private static final String CONTRATID = "contrat-id";

	public ContractAnalyzer() {}

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
	public JSONObject analyze(JSONObject entities, String idUser){
		resetParams();
		JSONObject jsonReturn = new JSONObject();
		List<String> missingParams = new ArrayList<>(3);

		String identifiant;
		try{
			identifiant = getContractId(entities);
			JSONMemory.putIdContrat(idUser, identifiant);
		}catch( JSONException | ClassCastException e){
			identifiant = JSONMemory.getIdContrat(idUser); // récupération de l'identifiant si non renseigné
		}

		if(identifiant==null || identifiant.isEmpty()){
			//L'identifiant du contrat n'est ni renseigné par l'utilisateur ni stocké dans son fichier
			jsonReturn.put(IntentsAnalyzer.SUCCESS, false);
			missingParams.add("votre identifiant de contrat"); // TODO: sortir les textes en dur
			jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
			return jsonReturn;

		} else {
			jsonReturn.put(IntentsAnalyzer.SUCCESS, true);
			String uritoCall = ApiUrls.demande.getUrl()+"ID-"+identifiant+"/";

			try{
				//String quelMethodeAppeler = recastJson.getString(FakeRecastKeys.METHOD.getName());
				calledMethod = getMethodToCall(entities);
				if(calledMethod==null){
					missingParams.add("\n\nvos couvertures"); // TODO: sortir les textes en dur
					missingParams.add("\n\nvos prélèvements");
					missingParams.add("\n\nle rôle d'une personne");
					jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
					jsonReturn.put(IntentsAnalyzer.SUCCESS, false); //La demande n'a pas été comprise
					return jsonReturn;
				}

				String quelleMethodeAppeler = calledMethod.toString();
				uritoCall+=ContractParams.valueOf(calledMethod.toString()).getChemin();

				String complement = getComplement(entities, calledMethod);
				if(complement != null){
					if(quelleMethodeAppeler.equalsIgnoreCase(ContractParams.risk.toString())){
						complement=(ContractParams.IDOBJ.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
					} else {
						complement = (ContractParams.IDBILLING.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
					}
					uritoCall+=complement;
				}else{
					choice = true;
					/* no complements */
				}
				jsonReturn.put(ApiUrls.URITOCALL.name(), uritoCall);
			}catch(JSONException e){
				//Tentative de récupération de la méthode à appeler lance une exception 
				jsonReturn.put(IntentsAnalyzer.SUCCESS, false);
				jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
			}
		}
		return jsonReturn;
	}

	private void resetParams(){
		calledMethod = null;
		choice = false;
	}

	/**
	 * Retourne le nom de la méthode à appeler, detectée par l'appel de la méthode analyse.
	 * @return calledMethod
	 */
	public ContractParams getCalledMethod() {
		return calledMethod;
	}

	/**
	 * Retourne vrai si l'appel à la méthode analyse détecte un choix à proposer à l'utilisateur
	 * @return choice
	 */
	public boolean isChoice() {
		return choice;
	}

	private ContractParams getMethodToCall(JSONObject entities){
		if(entities!=null){
			Set<String> setKeyEntities = entities.keySet();
			if(setKeyEntities==null || setKeyEntities.isEmpty()) return null;
			if(setKeyEntities.contains(ROLE) || setKeyEntities.contains(COMPLEM_ROLE)) return ContractParams.role;
			if(setKeyEntities.contains(COUVERTURE) || setKeyEntities.contains(COMPLEM_COUVERTURE)) return ContractParams.risk;
			if(setKeyEntities.contains(PAIEMENT) || setKeyEntities.contains(COMPLEM_PAIEMENT)) return ContractParams.prelevement;
		}
		return null;
	}

	private String getComplement(JSONObject entities, ContractParams cp){
		if(cp != null ){
			try{
				String toApply = null;
				switch (cp.toString().toLowerCase()) {
				case COUVERTURE:
					toApply = COMPLEM_COUVERTURE;
					break;
				case ROLE:
					toApply = COMPLEM_ROLE;	
					break;
				case PAIEMENT:
					toApply = COMPLEM_PAIEMENT;
					break;
				}
				if(toApply != null){
					return entities.getJSONArray(toApply).getJSONObject(0).getString(SuribotKeys.VALUES).replaceAll("[^0-9]+", "");
				}
			}catch(JSONException e){}
		}
		return null;
	}

	private String getContractId(JSONObject entities) throws JSONException, ClassCastException{
		return entities.getJSONArray(CONTRATID).getJSONObject(0).getString(RecastKeys.VALUES).replaceAll("[^0-9]+", "");
	}
}
