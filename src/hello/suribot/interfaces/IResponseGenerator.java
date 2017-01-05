package hello.suribot.interfaces;

import java.util.List;

import hello.suribot.response.MessagesResponses;

public interface IResponseGenerator {
	
	String generateUnderstoodMessage(String context, String calledMethod, boolean choice, String params);

	String generateInternalErrorMessage();

	String generateNotUnderstoodMessage();

	String generateMessageButMissOneArg(MessagesResponses key);

	String generateMessageButMissArgs(List<MessagesResponses> keys);

}