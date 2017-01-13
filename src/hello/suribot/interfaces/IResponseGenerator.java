package hello.suribot.interfaces;

import java.util.List;

import hello.suribot.response.MessagesResponses;
import hello.suribot.response.Response;

public interface IResponseGenerator {
	
	Response generateUnderstoodMessage(String context, String calledMethod, boolean choice, String params);

	Response generateInternalErrorMessage();

	Response generateNotUnderstoodMessage();

	Response generateMessageButMissOneArg(MessagesResponses key);

	Response generateMessageButMissArgs(List<MessagesResponses> keys);

}