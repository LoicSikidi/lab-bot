package hello.suribot.utils;

public final class EnvVar {
	
	public static final String APPID = System.getenv("APPID");
	public static final String APPSECRET = System.getenv("APPSECRET");
	public static final String BOTPORT = System.getenv("BOTPORT");
	public static final String NODEJSPORT = System.getenv("NODEJSPORT");
	public static final String CONTRACTPORT = System.getenv("CONTRACTPORT");
	public static final String TOKENRECAST = System.getenv("TOKENRECAST");

}
