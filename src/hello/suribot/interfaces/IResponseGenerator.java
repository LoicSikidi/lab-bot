package hello.suribot.interfaces;

import java.util.List;

import hello.suribot.analyze.MissingAnalyzerParam;
import hello.suribot.response.Response;

//TODO: JAVADOC
public interface IResponseGenerator {
	
	Response generateUnderstoodMessage(String context, String calledMethod, boolean choice, String params);

	Response generateInternalErrorMessage();

	Response generateNotUnderstoodMessage();

	Response generateMessageButMissOneArg(MissingAnalyzerParam key);

	Response generateMessageButMissArgs(List<MissingAnalyzerParam> keys);

}