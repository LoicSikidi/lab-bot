package sprint1.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import sprint1.bot.Bot;

public class BotTests {

	@Test
	public void connectionTest() throws IOException {
		Bot bot = new Bot(System.getenv(Bot.TOKEN_NAME));
		assertFalse(bot.isConnected());
		bot.connect();
		assertTrue(bot.isConnected());
		bot.disconnect();
		assertFalse(bot.isConnected());
	}

	//TODO: verification de bonne reception de messages
	
	//TODO: verification de bons envois de messages
}
