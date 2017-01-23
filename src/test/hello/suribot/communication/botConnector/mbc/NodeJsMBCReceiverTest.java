package test.hello.suribot.communication.botConnector.mbc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import hello.suribot.communication.botConnector.mbc.NodeJsMBCReceiver;


public class NodeJsMBCReceiverTest{
	
	private MockMvc mockMvc;

    @Before
    public void setUp() {
    	this.mockMvc = MockMvcBuilders.standaloneSetup(new NodeJsMBCReceiver()).build();
    }
	
	@Test
	public void receivingMessageTest() throws Exception{
		mockMvc.perform(get("/mbc/")
				.content(new JSONObject().toString()))
				.andExpect(status().isOk())
				.andReturn();
	}
}