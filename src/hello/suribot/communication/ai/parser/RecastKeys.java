package hello.suribot.communication.ai.parser;

/**
 * Toutes les clefs JSON des réponses de Recast.ai
 */
public abstract class RecastKeys {

	private RecastKeys(){}
	
	static final String RESULTS = "results";
	static final String ENTITIES = "entities";
	static final String INTENTS = "intents";
	static final String SLUG = "slug";
	static final String VALUES = "raw";
	static final String LANGUAGE = "language";
}
