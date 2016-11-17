package hello.suribot.analyze.contracts;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.ApiUrls;
import hello.suribot.analyze.jsonmemory.JSonMemory;
import hello.suribot.communication.recast.FakeRecastKeys;

public class ContractAnalyzer {

	public static JSONObject contractAnalyzer(JSONObject recastJson){
		JSONObject jsonReturn = new JSONObject();

		String idUser = recastJson.getString(FakeRecastKeys.IDUSER.getName());
		String uriToCall="";
		String contexte = ApiUrls.demande.toString();
		String identifiant ="";
		try{
			identifiant = recastJson.getString(FakeRecastKeys.IDENTIFICATION.getName());
			System.out.println(identifiant);
			JSonMemory.putIdContrat(idUser, identifiant);
		}catch(JSONException e){
			identifiant = JSonMemory.getIdContrat(idUser);
		}
		
		if(identifiant==null || identifiant.isEmpty()){
			//L'identifiant du contrat n'est ni renseigné par l'utilisateur ni stocké dans son fichier
			// TODO : stocker demande utilisateur ?
			jsonReturn.put("success", false);
			return jsonReturn;
		}else{
			jsonReturn.put("success", true);
			uriToCall+=ApiUrls.valueOf(contexte).getName()+identifiant+"/";
			jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
			
			try{
				String quelMethodeAppeler = recastJson.getString(FakeRecastKeys.QUOI.getName());
				if(quelMethodeAppeler.equals("risk")){
					uriToCall+=ContractParams.valueOf(quelMethodeAppeler).getName();
					try{
						String complement =recastJson.getString(FakeRecastKeys.COMPLEMENT.getName());
						complement=(ContractParams.IDOBJ.getName().replaceAll(ContractParams.IDREPLACE.getName(), complement));
						uriToCall+=complement+"/";
					}catch (JSONException e2){
						
					}
					jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
				
				}else if(quelMethodeAppeler.equals("billings")||quelMethodeAppeler.equals("role")){
					uriToCall+=ContractParams.valueOf(quelMethodeAppeler).getName();
					try{
						String complement =recastJson.getString(FakeRecastKeys.COMPLEMENT.getName());
						complement=(ContractParams.IDBILLING.getName()
								.replaceAll(ContractParams.IDREPLACE.getName(), complement));
						uriToCall+=complement+"/";
					}catch (JSONException e2){
						
					}
					jsonReturn.put(FakeRecastKeys.URITOCALL.name(), uriToCall);
				}else {
					//La demande n'a pas été comprise
					jsonReturn.put("success", false);
				}
			}catch(JSONException e){
				//Tentative de récupération de la méthode à appeler lance une exception 
				jsonReturn.put("success", false);
			}
		}
		return jsonReturn;
	}
}
