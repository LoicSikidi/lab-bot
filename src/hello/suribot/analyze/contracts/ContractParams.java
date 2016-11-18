package hello.suribot.analyze.contracts;

public enum ContractParams {
	
	risk ("risk/"),
	billings ("billings/"),
	role ("partyRoles/"),
	IDREPLACE ("YYYYYYY"),
	IDOBJ (IDREPLACE.getChemin()+"/couverture"),
	IDBILLING (IDREPLACE.getChemin()),
	COMPLEMENT ("complement");
	
	private String chemin;
	ContractParams(String chemin){
		this.chemin = chemin;
	}
	
	public String getChemin() {
		return chemin;
	}

}
