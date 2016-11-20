package hello.suribot.analyze;

import hello.suribot.utils.EnvVar;

public enum ApiUrls {
	demande ("http://localhost:"+EnvVar.CONTRACTPORT+"/insurance/contract/"),
	maps ("http://maps.google.com/maps");
	
	private String name;
	
	ApiUrls(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
