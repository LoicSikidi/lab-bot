package hello.suribot.communication.ai.parser;

/**
 * Toutes les clefs JSON des réponses de Recast.ai
 */
public enum RecastKeys {

	RESULTS("results"),
	ENTITIES("entities"),
	INTENTS("intents"),
	SLUG("slug"),
	VALUES("raw"),
	LANGUAGE("language");
	
	public String value;
	
	RecastKeys(String val){
		this.value=val;
	}
}
