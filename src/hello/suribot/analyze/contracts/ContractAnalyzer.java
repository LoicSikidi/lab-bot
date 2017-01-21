package hello.suribot.analyze.contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.SuribotKeys;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.MissingAnalyzerParam;
import hello.suribot.analyze.jsonmemory.JSONMemory;
import hello.suribot.communication.api.ApiUrls;

/**
 * Classe d'analyse des intents dans le contexte des "contrats"
 */
public class ContractAnalyzer implements IContractAnalyzer {

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
	private static final String RIB = "rib";
	private static final String NOMBRE = "nombre";

	public ContractAnalyzer() {}

	/* (non-Javadoc)
	 * @see hello.suribot.analyze.contracts.IContractAnalyzer#analyze(org.json.JSONObject, java.lang.String)
	 */
	@Override
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
			missingParams.add(MissingAnalyzerParam.idContrat.toString());
			jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
			return jsonReturn;

		} else {
			jsonReturn.put(IntentsAnalyzer.SUCCESS, true);
			String uritoCall = ApiUrls.demande.getUrl()+"ID-"+identifiant+"/";

			try{
				//String quelMethodeAppeler = recastJson.getString(FakeRecastKeys.METHOD.getName());
				calledMethod = getMethodToCall(entities);
				if(calledMethod==null){
					missingParams.add(MissingAnalyzerParam.billing.toString());
					missingParams.add(MissingAnalyzerParam.couverture.toString());
					missingParams.add(MissingAnalyzerParam.partyRole.toString());
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
					} else if( !complement.isEmpty()){
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

	/* (non-Javadoc)
	 * @see hello.suribot.analyze.contracts.IContractAnalyzer#getCalledMethod()
	 */
	@Override
	public ContractParams getCalledMethod() {
		return calledMethod;
	}

	/* (non-Javadoc)
	 * @see hello.suribot.analyze.contracts.IContractAnalyzer#isChoice()
	 */
	@Override
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
			if(setKeyEntities.contains(RIB)) return ContractParams.rib;
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
				case RIB:
					return "";
				}
				if(toApply != null){
					try{
						//On essaye de récupérer le complement bien formulé (objet {number}, prelevement {number})
						return entities.getJSONArray(toApply).getJSONObject(0).getString(SuribotKeys.VALUES).replaceAll("[^0-9]+", "");
					}catch(JSONException e){
						//Si le complément n'est pas formulé directement on regarde si l'utilisateur n'a pas entré qu'un numéro
						return entities.getJSONArray(NOMBRE).getJSONObject(0).getString(SuribotKeys.VALUES).replaceAll("[^0-9]+", "");
					}
				}
			}catch(JSONException e){
				return null;
			}
		}
		return null;
	}

	private String getContractId(JSONObject entities) throws JSONException, ClassCastException{
		return entities.getJSONArray(CONTRATID).getJSONObject(0).getString(SuribotKeys.VALUES).replaceAll("[^0-9]+", "");
	}
}
