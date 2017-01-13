package hello.suribot.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import hello.suribot.communication.recastConnector.RecastBotConnectorSender;
import hello.suribot.response.MessagesResponses;
import hello.suribot.response.Response;
import hello.suribot.response.ResponseGenerator;
import hello.suribot.response.contracts.ContractResponseGenerator;

public class TestSender {

	public static void main(String[] args) {
		ResourceBundle messages = ResourceBundle.getBundle(ResponseGenerator.bundleFile);
		ContractResponseGenerator generator = new ContractResponseGenerator(messages);
		String s = "{   \"methode\": \"cheque\",   \"amount\": 542.97,   \"identifiant\": \"123987456\",   \"frequency\": \"hebdomadaire\",   \"next_date\": \"2017-11-10\" }";
		System.out.println(generator.extractBillingInfos(s).getMessage());
		/*assertEquals("[methode : cheque, amount : 542.97, identifiant : 123987456, frequency : hebdomadaire, next_date : 2017-11-10]", 
				Arrays.toString(generator.extractBillingInfos(s)));*/
		/*List<String> listChoice = new ArrayList<>();
		listChoice.add("prélèvement 4444");
		String message = "";
		Response resp = new Response();
		resp.setMessage(message);
		resp.setUrlImage("https://mabanque.bnpparibas/rsc/contrib/image/particuliers/gabarits-libres/rib.jpg");
		resp.setListChoice(listChoice);
		RecastBotConnectorSender.callRecastBotConnectorBis(resp,"585d996b47657279ddb3ac55");*/
	}

}
