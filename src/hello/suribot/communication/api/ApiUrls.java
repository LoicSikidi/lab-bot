package hello.suribot.communication.api;

import hello.suribot.utils.EnvVar;

/**
 * Urls des API connues par le bot
 */
public enum ApiUrls {
	
	/**
	 * Utilisé par les "Analyzer" pour former l'URL à appeler
	 */
	URITOCALL,
	
	demande ("http://localhost:"+System.getenv(EnvVar.CONTRACTPORT.toString())+"/insurance/contract/");
	
	private String url;
	
	ApiUrls(String url){
		this.url=url;
	}
	
	ApiUrls(){
		this.url=null;
	}
	
	public String getUrl() {
		return url;
	}
}
