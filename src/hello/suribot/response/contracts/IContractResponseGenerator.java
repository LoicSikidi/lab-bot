package hello.suribot.response.contracts;

public interface IContractResponseGenerator {
	
	//TODO: javadoc
	String generateContractMessage(String calledMethod, boolean choice, String params) throws IllegalArgumentException;

}