package hello.suribot.test;

import org.json.JSONObject;

import hello.suribot.analyze.IntentsAnalyzer;
import hello.suribot.communication.recast.RecastAiController;

public class TestAnalyseMessageUser {
	public static void main(String[] args) {
		String idUser="felix";
		JSONObject js = new JSONObject();
		js=RecastAiController.fakeRecast("contract risk ", idUser);
		System.out.println(js);
		IntentsAnalyzer.interpretIntentRecast(js);		
		
	}
}
