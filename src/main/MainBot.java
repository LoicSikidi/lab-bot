package main;

import java.io.IOException;

import bot.Bot;

public class MainBot {

	public static void main(String[] args) throws IOException {
		Bot bot = new Bot();
		bot.connect();
		bot.listenMessages();
	}

}
