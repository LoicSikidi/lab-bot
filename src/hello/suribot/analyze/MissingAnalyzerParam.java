package hello.suribot.analyze;

import hello.suribot.interfaces.IResponseGenerator;

/**
 * Paramètres indiquant les informations manquantes que l'utilisateur doit fournir.
 * Utilisé par le composant "Générateur de réponse" {@link IResponseGenerator}
 */
public enum MissingAnalyzerParam {
	
	idContrat,
	couverture,
	billing,
	partyRole

}
