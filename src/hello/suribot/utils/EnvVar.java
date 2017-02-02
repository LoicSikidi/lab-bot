package hello.suribot.utils;

/**
 * Enum contenant toutes les variables d'environnement du BOT et de l'API bouchonnée
 */
public enum EnvVar {
	
//  Old env var Microsoft Bot Connector
//	APPID
//	APPSECRET
	NODEJSPORT,

	RBCSLUG,
	RBCBOTID,
	RBCTOKEN,
	
	BOTPORT,
	CONTRACTPORT,
	TOKENRECAST,
	TOKENAPIAI;
	
	private String value;
	
	private EnvVar(){
		try{
			value = System.getenv(toString());
		} catch (Exception e){
			value = null;
		}
	}
	
	public String getValue(){ return value; }
	
}
