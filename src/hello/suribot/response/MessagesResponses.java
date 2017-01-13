package hello.suribot.response;

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
	partyRoleMissingResponse
	//TODO: Ajouter value rib
}
