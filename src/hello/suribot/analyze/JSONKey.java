package hello.suribot.analyze;

public enum JSONKey {
	IDUSER ("IDUSER"),
	CONTEXTE ("CONTEXTE"),
	IDENTIFICATION ("IDENTIFICATION"),
	QUOI ("QUOI"),
	COMPLEMENT ("COMPLEMENT"),
	URITOCALL ("URITOCALL");
	
	private String name ="";
	
	JSONKey(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	
}
