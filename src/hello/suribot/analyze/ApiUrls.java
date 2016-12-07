package hello.suribot.analyze;

import hello.suribot.utils.EnvVar;

public enum ApiUrls {
	URITOCALL,
	
	demande ("http://localhost:"+EnvVar.CONTRACTPORT+"/insurance/contract/"),
	maps ("http://maps.google.com/maps");
	
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
