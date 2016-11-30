package hello.suribot.analyze;

import hello.suribot.utils.EnvVar;

public enum ApiUrls {
	demande ("http://localhost:"+EnvVar.CONTRACTPORT+"/insurance/contract/"),
	maps ("http://maps.google.com/maps");
	
	private String url;
	
	ApiUrls(String url){
		this.url=url;
	}
	
	public String getUrl() {
		return url;
	}
}
