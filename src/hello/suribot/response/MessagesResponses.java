package hello.suribot.response;

import hello.suribot.analyze.MissingAnalyzerParam;

/**
 * Enum des clefs de bundle.properties permettant de manipuler les messages pré-formé à fournir
 */
public enum MessagesResponses {

	//Clefs générales
	notUnderstoodMessage,
	missOneArg,
	missArgs,
	internalErrorMessage,

	// clefs de contrat
	billingsChoiceResponse,
	billingInfosResponse,
	partyRolesChoiceResponse,
	partyRoleInfosResponse,
	risksChoiceResponse,
	risksInfosResponse,
	ribInfosResponse,
	billingsChoice, 
	partyRolesChoice, 
	risksChoice,
	idContratMissingResponse,
	couvertureMissingResponse,
	billingMissingResponse,
	partyRoleMissingResponse;
	
	/**
	 * Fais la laison entre un paramètre manquant {@link MissingAnalyzerParam} et un {@link MessagesResponses}.
	 * @param param
	 * @return le {@link MessagesResponses} associé, null sinon.
	 */
	public static MessagesResponses adapt(MissingAnalyzerParam param){
		if(param != null){
			switch (param) {
			case idContrat:
				return idContratMissingResponse;
			case billing:
				return billingMissingResponse;
			case couverture:
				return couvertureMissingResponse;
			case partyRole:
				return partyRoleMissingResponse;
			default:
				return null;
			}
		}
		return null;
	}
}
