package hello.suribot.analyze;

import hello.suribot.utils.API.ApiUrls;

public enum PreferenceAPI {
	deplacement (ApiUrls.RATP),
	demande ("http://localhost:8080/insurance/contract/"),
	risk ("risk/"),
	billings ("billings/"),
	role ("partyRoles/"),
	IDREPLACE ("YYYYYYY"),
	IDOBJ (IDREPLACE.getName()+"/couverture"),
	IDBILLING (IDREPLACE.getName()),
	COMPLEMENT ("complement");
	
	private String name ="";
	PreferenceAPI(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
