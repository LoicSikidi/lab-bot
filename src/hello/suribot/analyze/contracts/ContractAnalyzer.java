package hello.suribot.analyze.contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.ApiUrls;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.jsonmemory.JSonMemory;

public class ContractAnalyzer {
	
	private ContractParams calledMethod;
	private boolean choice;
	
	public ContractAnalyzer() {}
	
	/**
	 * Exemple 1 :{"risk":[{"confidence":0.91,"raw":"couvertures","value":"couvertures"}]}
	 * Exemple 2 :{"prelevement-id":[{"confidence":0.39,"raw":"prélèvement 478855","value":"prélèvement 478855"}]}
	 * Exemple 3 :{"role":[{"confidence":0.82,"raw":"role","value":"role"}]}
	 */
	public JSONObject analyze(JSONObject entities, String idUser){
		System.out.println(entities);
		resetParams();
		JSONObject jsonReturn = new JSONObject();
		List<String> missingParams = new ArrayList<>(3);
		
		String identifiant;
		try{
			identifiant = getContractId(entities);
			JSonMemory.putIdContrat(idUser, identifiant);
		}catch( JSONException | ClassCastException e){
			identifiant = JSonMemory.getIdContrat(idUser); // récupération de l'identifiant si non renseigné
		}
		
		if(identifiant==null || identifiant.isEmpty()){
			//L'identifiant du contrat n'est ni renseigné par l'utilisateur ni stocké dans son fichier
			jsonReturn.put(IntentsAnalyzer.SUCCESS, false);
			missingParams.add("votre identifiant de contrat");
			jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
			return jsonReturn;
			
		} else {
			jsonReturn.put(IntentsAnalyzer.SUCCESS, true);
			String uriToCall = ApiUrls.demande.getUrl()+"ID-"+identifiant+"/";
			
			try{
				//String quelMethodeAppeler = recastJson.getString(FakeRecastKeys.METHOD.getName());
				calledMethod = getMethodToCall(entities);
				if(calledMethod==null){
					missingParams.add("\n\nvos couvertures");
					missingParams.add("\n\nvos prélèvements");
					missingParams.add("\n\nle rôle d'une personne");
					jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
					jsonReturn.put(IntentsAnalyzer.SUCCESS, false); //La demande n'a pas été comprise
					return jsonReturn;
				}

				String quelMethodeAppeler = calledMethod.toString();
				uriToCall+=ContractParams.valueOf(calledMethod.toString()).getChemin();
				
				String complement = getComplement(entities, calledMethod);
				if(complement != null){
					if(quelMethodeAppeler.equalsIgnoreCase(ContractParams.risk.toString())){
						complement=(ContractParams.IDOBJ.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
					} else {
						complement = (ContractParams.IDBILLING.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
					}
					uriToCall+=complement;
				}else{
					choice = true;
					/* no complements */
				}
				jsonReturn.put(ApiUrls.URITOCALL.name(), uriToCall);
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
			if(setKeyEntities.contains("role") || setKeyEntities.contains("person-id")) return ContractParams.role;
			if(setKeyEntities.contains("risk") || setKeyEntities.contains("object-id")) return ContractParams.risk;
			if(setKeyEntities.contains("prelevement") || setKeyEntities.contains("prelevement-id")) return ContractParams.billings;
		}
		return null;
	}
	
	private String getComplement(JSONObject entities, ContractParams cp){
		if(cp == null ) return null;
		try{
			if(cp.toString().equalsIgnoreCase("risk")) return entities.getJSONArray("object-id").getJSONObject(0).getString("raw").replaceAll("[^0-9]+", "");
			else if(cp.toString().equalsIgnoreCase("role")) return entities.getJSONArray("person-id").getJSONObject(0).getString("raw").replaceAll("[^0-9]+", "");
			else if(cp.toString().equalsIgnoreCase("billings")) return entities.getJSONArray("prelevement-id").getJSONObject(0).getString("raw").replaceAll("[^0-9]+", "");
		}catch(JSONException e){}
		
		return null;
	}
	
	private String getContractId(JSONObject entities) throws JSONException,ClassCastException{
		return entities.getJSONArray("contrat-id").getJSONObject(0).getString("raw").replaceAll("[^0-9]+", "");
	}
}
