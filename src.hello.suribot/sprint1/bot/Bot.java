package sprint1.bot;
import java.io.IOException;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class Bot{

	private SlackSession session;
	public static final String TOKEN_NAME = "LAB_BOT";

	public Bot(String token) throws IOException{
		session = SlackSessionFactory.createWebSocketSlackSession(token);
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
				// To avoid message the bot send (yes it is receiving notification for its own messages)
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
				
			}
		};
		
		//add it to the session
		session.addMessagePostedListener(messagePostedListener);
	}


	public void listenMessages() {
		registeringAListener(session);
	}



}
