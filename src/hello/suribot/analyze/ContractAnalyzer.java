package hello.suribot.analyze;

import org.json.JSONException;
import org.json.JSONObject;

import hello.suribot.analyze.jsonmemory.JSonMemory;

public class ContractAnalyzer {

	public static JSONObject contractAnalyser(JSONObject recastJson){
		JSONObject jsonReturn = new JSONObject();

		String idUser = recastJson.getString(JSONKey.IDUSER.getName());
		String uriToCall="";
		String contexte = "demande";
		String identifiant ="";
		try{
			identifiant = recastJson.getString(JSONKey.IDENTIFICATION.getName());
			System.out.println(identifiant);
			JSonMemory.putIdContrat(idUser, identifiant);
		}catch(JSONException e){
			identifiant = JSonMemory.getIdContrat(idUser);
		}
		
		if(identifiant==null || identifiant.equals("")){
			//L'identifiant du contrat n'est ni renseigné par l'utilisateur ni stocké dans son fichier
			//stocker demande utilisateur
			jsonReturn.put("success", false);
			return jsonReturn;
		}else{
			jsonReturn.put("success", true);
			uriToCall+=PreferenceAPI.valueOf(contexte).getName()+identifiant+"/";
			jsonReturn.put(JSONKey.URITOCALL.name(), uriToCall);
			
			try{
				String quelMethodeAppeler = recastJson.getString(JSONKey.QUOI.getName());
				if(quelMethodeAppeler.equals("risk")){
					uriToCall+=PreferenceAPI.valueOf(quelMethodeAppeler).getName();
					try{
						String complement =recastJson.getString(JSONKey.COMPLEMENT.getName());
						complement=(PreferenceAPI.IDOBJ.getName().replaceAll(PreferenceAPI.IDREPLACE.getName(), complement));
						uriToCall+=complement+"/";
					}catch (JSONException e2){
						
					}
					jsonReturn.put(JSONKey.URITOCALL.name(), uriToCall);
				
				}else if(quelMethodeAppeler.equals("billings")||quelMethodeAppeler.equals("role")){
					uriToCall+=PreferenceAPI.valueOf(quelMethodeAppeler).getName();
					try{
						String complement =recastJson.getString(JSONKey.COMPLEMENT.getName());
						complement=(PreferenceAPI.IDBILLING.getName().replaceAll(PreferenceAPI.IDREPLACE.getName(), complement));
						uriToCall+=complement+"/";
					}catch (JSONException e2){
						
					}
					jsonReturn.put(JSONKey.URITOCALL.name(), uriToCall);
				}else {
					//La demande n'a pas était comprises
					jsonReturn.put("success", false);
				}
			}catch(JSONException e){
				//Tentative de récupérer la méthode à appeler lance une exception du coup on a pas pu traitre la demande
				jsonReturn.put("success", false);
			}
		}
		System.out.println("jsonReturn == "+jsonReturn);
		return jsonReturn;
	}
}
