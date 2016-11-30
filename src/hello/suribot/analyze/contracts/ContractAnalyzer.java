package hello.suribot.analyze.contracts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.ApiUrls;
import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.recast.FakeRecastKeys;

public class ContractAnalyzer {
	
	private ContractParams calledMethod;
	private boolean choice;
	
	public ContractAnalyzer() {}
	
	public JSONObject analyze(JSONObject recastJson){
		resetParams();
		JSONObject jsonReturn = new JSONObject();
		List<String> missingParams = new ArrayList<>(3);
		
		String identifiant;
		String idUser = recastJson.getString(FakeRecastKeys.IDUSER.getName());
		try{
			identifiant = recastJson.getString(FakeRecastKeys.IDENTIFICATION.getName());
			JSonMemory.putIdContrat(idUser, identifiant);
		}catch(JSONException e){
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
			String uriToCall = ApiUrls.demande.getUrl()+identifiant+"/";
			
			try{
				String quelMethodeAppeler = recastJson.getString(FakeRecastKeys.METHOD.getName());
				
				if(quelMethodeAppeler.equalsIgnoreCase(ContractParams.risk.toString())){
					calledMethod = ContractParams.risk;
				} else if(quelMethodeAppeler.equalsIgnoreCase(ContractParams.billings.toString())){
					calledMethod = ContractParams.billings;
				} else if(quelMethodeAppeler.equalsIgnoreCase(ContractParams.role.toString())){
					calledMethod = ContractParams.role;
				} else {
					missingParams.add("méthode risk");
					missingParams.add("méthode billings");
					missingParams.add("méthode partyRole");
					jsonReturn.put(IntentsAnalyzer.MISSINGPARAMS, missingParams);
					jsonReturn.put(IntentsAnalyzer.SUCCESS, false); //La demande n'a pas été comprise
					return jsonReturn;
				}
				
				uriToCall+=ContractParams.valueOf(quelMethodeAppeler).getChemin();
				try{
					String complement = recastJson.getString(FakeRecastKeys.COMPLEMENT.getName());
					
					if(quelMethodeAppeler.equalsIgnoreCase(ContractParams.risk.toString())){
						complement=(ContractParams.IDOBJ.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
					} else {
						complement = (ContractParams.IDBILLING.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
					}
					uriToCall+=complement+"/";
				} catch (JSONException e){
					choice = true;
					/* no complements */
				}
				jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
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

	public ContractParams getCalledMethod() {
		return calledMethod;
	}

	public boolean isChoice() {
		return choice;
	}

}
