package hello.suribot.analyze;

import org.json.JSONObject;

import hello.suribot.communication.api.APIController;
import hello.suribot.communication.mbc.NodeJsMBCSender;
import hello.suribot.interfaces.IJsonDecoder;
import hello.suribot.response.ResponseGenerator;

public class IntentsAnalyzer implements IJsonDecoder{
	
	private NodeJsMBCSender nextToCall;
	
	private ResponseGenerator responsegenerator;
	private APIController apicontroller;
	

	public IntentsAnalyzer() {
		this.responsegenerator = new ResponseGenerator();
		this.apicontroller = new APIController();
		this.nextToCall = new NodeJsMBCSender();
	}

	public void analyzeRecastIntents(JSONObject json, String intents) {
		System.out.println("IntentsAnalyzer analyzeRecastIntents");
		//TODO: get url and parameters
		String apiResponse = null;
		if(intents!=null) apiResponse = apicontroller.sendMessageAndReturnResponse("", intents);
		
		String generatedResponse = responsegenerator.generateMessage(apiResponse);
		
		nextToCall.sendMessage(json, generatedResponse);
	}

}
