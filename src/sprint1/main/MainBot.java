package sprint1.main;

import java.io.IOException;

import sprint1.bot.Bot;

public class MainBot {

	public static void main(String[] args) throws IOException {
		String token = System.getenv(Bot.TOKEN_NAME);
		if(token==null || token.isEmpty()){
			throw new Error("Add LAB_BOT with the token to your environment variables.");
		} else {
			System.out.println("LAB_BOT environment variable : "+token);
		}
		
		Bot bot = new Bot(token);
		bot.connect();
		bot.listenMessages();
	}

}
