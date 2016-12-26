package hello.suribot.communication.ai;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;

import ai.api.model.AIResponse;
import hello.suribot.communication.ai.keys.RecastKeys;
import hello.suribot.communication.ai.keys.SuribotKeys;

/**
 * Ce Parser permet la transformation du JSON provenant d'une intelligence externe (API.ai, Recast, ...)
 * en un JSON formé pour le bot
 */
public class SuribotParser {
	
	public SuribotParser(){ }
	
	/**
	 * Prend en paramètre le JSON fourni par API.ai ({@link AIResponse}) et retourne un {@link JSONObject} formé pour le bot
	 * @param response
	 * @return le JSONObject formé avec des {@link SuribotKeys}
	 */
	public JSONObject parseApiAi(AIResponse response){
		Map<String, JsonElement> parameter = response.getResult().getParameters();
		
		JSONObject entities = new JSONObject();
		if(parameter!=null){  // récupération de tous les entities
			JSONObject tmp;
			JSONArray arrayEntitiesTmp ;
			for(String elem : parameter.keySet()){
				arrayEntitiesTmp = new JSONArray();
				tmp = new JSONObject();		
				
				//Dans la suite du code on utilise l'attribut raw de l'intent
				tmp.put(SuribotKeys.VALUES, parameter.get(elem).getAsString());	
				arrayEntitiesTmp.put(tmp);

				entities.put(elem, arrayEntitiesTmp);
			}
		}
		
		JSONArray arrayIntent = new JSONArray();
		JSONObject tmp = new JSONObject();
		tmp.put(SuribotKeys.SLUG, response.getResult().getMetadata().getIntentName());
		arrayIntent.put(tmp);
		
		JSONObject result = new JSONObject();
		result.put(SuribotKeys.INTENTS, arrayIntent);
		result.put(SuribotKeys.ENTITIES, entities);
		result.put(SuribotKeys.LANGUAGE, "fr");	//TODO: On met fr en langue par defaut car API.ai ne retourne pas la langue du message
		
		JSONObject parsed = new JSONObject();
		parsed.put(SuribotKeys.RESULTS, result);
		
		return parsed;
	}
	
	/**
	 * Prend en paramètre le JSON fourni par Recast ({@link JSONObject}) et retourne un {@link JSONObject} formé pour le bot
	 * @param response
	 * @return le JSONObject formé avec des {@link SuribotKeys}
	 */
	public JSONObject parseRecast(JSONObject response){
		if(response != null && response.length()!=0
				&& response.has(RecastKeys.RESULTS)){
			
			JSONObject resultObject = response.getJSONObject(RecastKeys.RESULTS);

			// récupération du slug
			JSONArray arrayIntent = new JSONArray();
			{
				JSONArray intentsObject = resultObject.getJSONArray(RecastKeys.INTENTS);
				if(intentsObject != null && intentsObject.length()!=0){
				
					String slug = intentsObject.getJSONObject(0).getString(RecastKeys.SLUG);
					
					JSONObject slugObject = new JSONObject();
					slugObject.put(SuribotKeys.SLUG, slug);
					arrayIntent.put(slugObject);
				}
			}
			
			// récupération des entities
			JSONObject entities = new JSONObject();
			{
				JSONObject entitiesObject = resultObject.getJSONObject(RecastKeys.ENTITIES);
 				if(entitiesObject != null){
					JSONObject contentEntity;
					String raw;
					JSONObject oneEntityRaw;
					for(String key : entitiesObject.keySet()){
						contentEntity = entitiesObject.getJSONArray(key).getJSONObject(0);
						raw = contentEntity.getString(RecastKeys.VALUES);
						oneEntityRaw = new JSONObject();
						oneEntityRaw.put(SuribotKeys.VALUES, raw);
						
						JSONArray rawArray = new JSONArray();
						rawArray.put(oneEntityRaw);
						entities.put(key, rawArray);
					}
				}
			}

			// récupération du langage
			String language = resultObject.getString(RecastKeys.LANGUAGE);
		
			JSONObject result = new JSONObject();
			result.put(SuribotKeys.INTENTS, arrayIntent);
			result.put(SuribotKeys.ENTITIES, entities);
			result.put(SuribotKeys.LANGUAGE, language);
			
			JSONObject parsed = new JSONObject();
			parsed.put(SuribotKeys.RESULTS, result);
			return parsed;
		}
		
		return null;
		
	}
	
}
