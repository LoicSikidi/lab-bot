package test.hello.suribot.communication.api;

import java.io.IOException;

import org.junit.Test;

import hello.suribot.communication.api.APIController;

public class APIControllerTest{
	
	@Test(expected=IOException.class)
	public void sendTest() throws IOException{
		APIController controller = new APIController();
		controller.send(null);
	}
	
}