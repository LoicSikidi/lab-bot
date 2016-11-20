package hello.suribot.analyze.contracts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.ApiUrls;
import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.recast.FakeRecastKeys;

public class ContractAnalyzer {

	public static JSONObject contractAnalyzer(JSONObject recastJson){
		JSONObject jsonReturn = new JSONObject();
		List<String> paramManquant = new ArrayList<>();
		String idUser = recastJson.getString(FakeRecastKeys.IDUSER.getName());
		String uriToCall="";
		String contexte = ApiUrls.demande.toString();
		String identifiant ="";
		try{
			identifiant = recastJson.getString(FakeRecastKeys.IDENTIFICATION.getName());
			JSonMemory.putIdContrat(idUser, identifiant);
		}catch(JSONException e){
			identifiant = JSonMemory.getIdContrat(idUser);
		}
		
		if(identifiant==null || identifiant.isEmpty()){
			//L'identifiant du contrat n'est ni renseigné par l'utilisateur ni stocké dans son fichier
			jsonReturn.put("success", false);
			paramManquant.add("votre identifiant de contrat");
			jsonReturn.put("paramManquant", paramManquant);
			return jsonReturn;
		}else{
			jsonReturn.put("success", true);
			uriToCall+=ApiUrls.valueOf(contexte).getName()+identifiant+"/";
			jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
			
			try{
				String quelMethodeAppeler = recastJson.getString(FakeRecastKeys.QUOI.getName());
				if(quelMethodeAppeler.equals(ContractParams.risk.toString())){
					uriToCall+=ContractParams.valueOf(quelMethodeAppeler).getChemin();
					try{
						String complement = recastJson.getString(FakeRecastKeys.COMPLEMENT.getName());
						complement=(ContractParams.IDOBJ.getChemin().replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
						uriToCall+=complement+"/";
					}catch (JSONException e2){
						
					}
					jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
				
				}else if(quelMethodeAppeler.equals(ContractParams.billings.toString())||quelMethodeAppeler.equals(ContractParams.role.toString())){
					uriToCall+=ContractParams.valueOf(quelMethodeAppeler).getChemin();
					try{
						String complement =recastJson.getString(FakeRecastKeys.COMPLEMENT.getName());
						complement=(ContractParams.IDBILLING.getChemin()
								.replaceAll(ContractParams.IDREPLACE.getChemin(), complement));
						uriToCall+=complement+"/";
					}catch (JSONException e2){
						
					}
					jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
				}else {
					paramManquant.add("methode risk");
					paramManquant.add("methode billings");
					paramManquant.add("methode partyRole");
					//La demande n'a pas été comprise
					jsonReturn.put("success", false);
					jsonReturn.put("paramManquant", paramManquant);
				}
			}catch(JSONException e){
				//Tentative de récupération de la méthode à appeler lance une exception 
				jsonReturn.put("success", false);
				jsonReturn.put("paramManquant", paramManquant);
			}
		}
		return jsonReturn;
	}
}
