package hello.suribot.analyze;

public enum ApiUrls {
	demande ("http://localhost:12347/insurance/contract/"),
	maps ("http://maps.google.com/maps");
	
	private String name;
	
	ApiUrls(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
