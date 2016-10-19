package main;

import java.io.IOException;

import bot.Bot;

public class MainBot {

	public static void main(String[] args) throws IOException {
		String token = System.getenv(Bot.TOKEN_NAME);
		if(token==null || token.isEmpty()){
			throw new Error("Add LAB_BOT with the token to your environment variables. (see in resources/init.sh)");
		} else {
			Bot bot = new Bot(token);
			bot.connect();
			bot.listenMessages();
		}
	}

}
