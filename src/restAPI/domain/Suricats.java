package restAPI.domain;

public class Suricats {
	
	private String name;
	private String location;
	
	public Suricats(){}

	public Suricats(String name, String location) {
		super();
		this.name = name;
		this.location = location;
	}

	public Suricats(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
