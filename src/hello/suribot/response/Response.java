package hello.suribot.response;

import java.util.List;

public class Response {

	private String message;
	private List<String> listChoice;
	
	public Response(){}
	
	public Response(String message){
		this.message = message;
	}
	
	public Response(String message, List<String> listChoice){
		this.message = message;
		this.listChoice = listChoice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getListChoice() {
		return listChoice;
	}

	public void setListChoice(List<String> listChoice) {
		this.listChoice = listChoice;
	}
}
