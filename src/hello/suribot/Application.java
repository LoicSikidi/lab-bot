package hello.suribot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hello.suribot.utils.EnvVar;

@SpringBootApplication
public class Application {
	
	private static final Logger logger = LogManager.getLogger();
	
    public static void main(String[] args) {
    	checkEnvVar();
    	SpringApplication.run(Application.class, "");
    }
    
    private static void checkEnvVar(){
		
    	for(EnvVar var : EnvVar.values()){
    		logger.info(var + " : "+ var.getValue());
    		if(var.getValue()==null){
    			System.err.println("Check your environment var "+var.toString()+"... ");
    			System.exit(0);
    		}
    	}
		
    }
    
}