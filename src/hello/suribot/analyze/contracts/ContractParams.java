package hello.suribot.analyze.contracts;

/**
 * Divers paramètres et méthodes liés à l'URL de l'API à appeler.
 */
public enum ContractParams {
	
	risk ("risk/"),
	prelevement ("billings/"),
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
