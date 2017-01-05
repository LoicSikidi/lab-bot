package hello.suribot.utils;

public enum EnvVar {
	
//  OLD VAR ENV Microsoft Bot Connector
//	APPID = System.getenv("APPID");
//	APPSECRET = System.getenv("APPSECRET");
	
//  VAR ENV Recast Bot Connector
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
