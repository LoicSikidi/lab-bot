package hello.suribot;

/**
 * Toutes les clefs JSON propres au bot
 */
public enum SuribotKeys {
	
	RESULTS("results"),
	ENTITIES("entities"),
	INTENTS("intents"),
	SLUG("slug"),
	VALUES("raw"),
	LANGUAGE("language");
	
	public String value;
	
	SuribotKeys(String val){
		this.value=val;
	}
}
