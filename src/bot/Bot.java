package bot;
import java.io.IOException;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class Bot{

	private static final String TOKEN_BOT = "xoxb-91352476245-kNoh5IbBqyc8BBmWieOc0Qig"; //TODO : à mettre dans variable d'env
	private SlackSession session;

	public Bot() throws IOException{
		session = SlackSessionFactory.createWebSocketSlackSession(TOKEN_BOT);
	}
	
	public void connect() throws IOException{
		if(session!=null){
			session.connect();
		}
	}

	public void disconnect() throws IOException{
		if(session!=null && session.isConnected()){
			session.disconnect();
		}
	}
	
	public boolean isConnected(){
		return session.isConnected();
	}

	/**
	 * This method shows how to register a listener on a SlackSession
	 */
	public void registeringAListener(SlackSession session)
	{
		// first define the listener
		SlackMessagePostedListener messagePostedListener = new SlackMessagePostedListener()
		{
			@Override
			public void onEvent(SlackMessagePosted event, SlackSession session)
			{
				// How to avoid message the bot send (yes it is receiving notification for its own messages)
				// session.sessionPersona() returns the user this session represents
				if (session.sessionPersona().getId().equals(event.getSender().getId())) {
					return;
				}
				String messageContent = event.getMessageContent();
				SlackUser messageSender = event.getSender();
				
				if (messageContent.contains("Hi")) {
					session.sendMessage(event.getChannel(),"Hello "+messageSender.getUserName()+" !");
					return;
				} else {
					session.sendMessage(event.getChannel(),"Hello "+messageSender.getUserName()+", my name is "+session.sessionPersona().getUserName());
					return;
				}
				
				// UTILS

				// if I'm only interested on a certain channel :
				// I can filter out messages coming from other channels
				// SlackChannel channelOnWhichMessageWasPosted = event.getChannel();
				// SlackChannel theChannel = session.findChannelByName("thechannel");
				// if (!theChannel.getId().equals(event.getChannel().getId())) {
				//	 return;
				// }

				// if I'm only interested on messages posted by a certain user :
				// I can filter out messages coming from other users
				// SlackUser myInterestingUser = session.findUserByUserName("gueststar");

				//if (!myInterestingUser.getId().equals(event.getSender().getId())) {
				//	return;
				//}

			}
		};
		
		//add it to the session
		session.addMessagePostedListener(messagePostedListener);
	}


	public void listenMessages() {
		registeringAListener(session);
	}



}
