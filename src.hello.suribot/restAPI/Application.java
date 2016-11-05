package restAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
    	checkEnvVar();
    	SpringApplication.run(Application.class, "");
    }
    
    private static void checkEnvVar(){
    	String appid = System.getenv("APPID");
		String appsecret = System.getenv("APPSECRET");
		if(appid == null) System.err.println("Check your environment var APPID...");
		if(appsecret == null) System.err.println("Check your environment var APPSECRET...");
		
		if(appid==null || appsecret==null){
			System.exit(0);
		}
    }
    
}