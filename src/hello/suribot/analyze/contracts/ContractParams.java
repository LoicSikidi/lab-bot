package hello.suribot.analyze.contracts;

public enum ContractParams {
	
	risk ("risk/"),
	billings ("billings/"),
	role ("partyRoles/"),
	
	IDREPLACE ("YYYYYYY"),
	IDOBJ ("ID-"+IDREPLACE.chemin+"/couverture"),
	IDBILLING ("ID-"+IDREPLACE.chemin),
	COMPLEMENT ("complement");
	
	private String chemin;
	ContractParams(String chemin){
		this.chemin = chemin;
	}
	
	public String getChemin() {
		return chemin;
	}

}
