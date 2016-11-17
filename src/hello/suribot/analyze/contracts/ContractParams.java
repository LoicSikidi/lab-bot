package hello.suribot.analyze.contracts;

public enum ContractParams {
	
	risk ("risk/"),
	billings ("billings/"),
	role ("partyRoles/"),
	IDREPLACE ("YYYYYYY"),
	IDOBJ (IDREPLACE.getName()+"/couverture"),
	IDBILLING (IDREPLACE.getName()),
	COMPLEMENT ("complement");
	
	private String name;
	ContractParams(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

}
