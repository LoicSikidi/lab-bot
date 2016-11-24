package hello.suribot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hello.suribot.utils.EnvVar;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
    	checkEnvVar();
    	SpringApplication.run(Application.class, "");
    }
    
    private static void checkEnvVar(){
		
		if(EnvVar.APPID == null) System.err.println("Check your environment var APPID...");
		if(EnvVar.APPSECRET == null) System.err.println("Check your environment var APPSECRET...");
		if(EnvVar.BOTPORT == null) System.err.println("Check your environment var BOTPORT...");
		if(EnvVar.NODEJSPORT == null) System.err.println("Check your environment var NODEJSPORT...");
		if(EnvVar.CONTRACTPORT == null) System.err.println("Check your environment var CONTRACTPORT...");
		
		if(EnvVar.APPID==null || EnvVar.APPSECRET==null || EnvVar.NODEJSPORT==null 
				|| EnvVar.BOTPORT==null || EnvVar.CONTRACTPORT==null){
			System.exit(0);
		}
    }
    
}