package hello.suribot.communication.ai.parser;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;

import ai.api.model.AIResponse;
import hello.suribot.SuribotKeys;

/**
 * Ce Parser permet la transformation du JSON provenant d'une intelligence externe (API.ai, Recast, ...)
 * en un JSON formé pour le bot
 */
public class SuribotParser {

	public SuribotParser(){ }

	/** Prend en paramètre l'{@link AIResponse} fourni par API.ai et retourne un {@link JSONObject} formé pour le bot
	 * @param response fourni par API.AI
	 * @return le JSONObject formé avec des {@link SuribotKeys}
	 */
	public JSONObject parseApiAi(AIResponse response){
		if(response != null){
			Map<String, JsonElement> parameter = response.getResult().getParameters();

			JSONObject entities = new JSONObject();
			if(parameter!=null){  // récupération de tous les entities
				JSONObject tmp;
				JSONArray arrayEntitiesTmp ;
				for(String elem : parameter.keySet()){
					arrayEntitiesTmp = new JSONArray();
					tmp = new JSONObject();		

					//Dans la suite du code on utilise l'attribut raw de l'intent
					tmp.put(SuribotKeys.VALUES.value, parameter.get(elem).getAsString());	
					arrayEntitiesTmp.put(tmp);

					entities.put(elem, arrayEntitiesTmp);
				}
			}

			JSONArray arrayIntent = new JSONArray();
			JSONObject tmp = new JSONObject();
			tmp.put(SuribotKeys.SLUG.value, response.getResult().getMetadata().getIntentName());
			arrayIntent.put(tmp);

			JSONObject result = new JSONObject();
			result.put(SuribotKeys.INTENTS.value, arrayIntent);
			result.put(SuribotKeys.ENTITIES.value, entities);
			result.put(SuribotKeys.LANGUAGE.value, "fr");

			JSONObject parsed = new JSONObject();
			parsed.put(SuribotKeys.RESULTS.value, result);

			return parsed;
		}
		return null;
	}

	/**
	 * Prend en paramètre le JSON fourni par Recast ({@link JSONObject}) et retourne un {@link JSONObject} formé pour le bot
	 * @param response fourni par Recast
	 * @return le JSONObject formé avec des {@link SuribotKeys}
	 */
	public JSONObject parseRecast(JSONObject response){
		if(response != null && response.length()!=0
				&& response.has(RecastKeys.RESULTS.value)){

			JSONObject resultObject = response.getJSONObject(RecastKeys.RESULTS.value);

			// récupération du slug
			JSONArray arrayIntent = new JSONArray();
			{
				JSONArray intentsObject = resultObject.getJSONArray(RecastKeys.INTENTS.value);
				if(intentsObject != null && intentsObject.length()!=0){

					String slug = intentsObject.getJSONObject(0).getString(RecastKeys.SLUG.value);

					JSONObject slugObject = new JSONObject();
					slugObject.put(SuribotKeys.SLUG.value, slug);
					arrayIntent.put(slugObject);
				}
			}

			// récupération des entities
			JSONObject entities = new JSONObject();
			{
				JSONObject entitiesObject = resultObject.getJSONObject(RecastKeys.ENTITIES.value);
				if(entitiesObject != null){
					JSONObject contentEntity;
					String raw;
					JSONObject oneEntityRaw;
					for(String key : entitiesObject.keySet()){
						contentEntity = entitiesObject.getJSONArray(key).getJSONObject(0);
						raw = contentEntity.getString(RecastKeys.VALUES.value);
						oneEntityRaw = new JSONObject();
						oneEntityRaw.put(SuribotKeys.VALUES.value, raw);

						JSONArray rawArray = new JSONArray();
						rawArray.put(oneEntityRaw);
						entities.put(key, rawArray);
					}
				}
			}

			// récupération du langage
			String language = resultObject.getString(RecastKeys.LANGUAGE.value);

			JSONObject result = new JSONObject();
			result.put(SuribotKeys.INTENTS.value, arrayIntent);
			result.put(SuribotKeys.ENTITIES.value, entities);
			result.put(SuribotKeys.LANGUAGE.value, language);

			JSONObject parsed = new JSONObject();
			parsed.put(SuribotKeys.RESULTS.value, result);
			return parsed;
		}

		return null;
	}

}
