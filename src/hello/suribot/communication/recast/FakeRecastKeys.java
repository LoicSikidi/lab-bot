package hello.suribot.communication.recast;

public enum FakeRecastKeys {
	IDUSER ("IDUSER"),
	CONTEXTE ("CONTEXTE"),
	IDENTIFICATION ("IDENTIFICATION"),
	QUOI ("QUOI"),
	COMPLEMENT ("COMPLEMENT"),
	URITOCALL ("URITOCALL");
	
	private String name;
	
	FakeRecastKeys(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	
}
