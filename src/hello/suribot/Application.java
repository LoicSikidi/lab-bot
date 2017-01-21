package hello.suribot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hello.suribot.utils.EnvVar;

@SpringBootApplication
public class Application {
	
	private static final Logger logger = LogManager.getLogger();
	private static final Logger logger2 = LogManager.getRootLogger();
	private static final Logger logger3 = LogManager.getFormatterLogger();
	
    public static void main(String[] args) {
    	checkEnvVar();
    	SpringApplication.run(Application.class, "");
    }
    
    private static void checkEnvVar(){
		
    	for(EnvVar var : EnvVar.values()){
    		logger.info("1 "+var + " : "+ var.getValue());
    		logger2.info("1 "+var + " :info2 "+ var.getValue());
    		logger3.info("1 "+var + " :info3 "+ var.getValue());
    		logger.error("1 "+var + " :1 "+ var.getValue());
    		logger2.error("1 "+var + " :2 "+ var.getValue());
    		logger3.error("1 "+var + " :3 "+ var.getValue());
    		if(var.getValue()==null){
    			logger.error("Check your environment var "+var.toString()+"... ");
    			System.exit(0);
    		}
    	}
		
    }
    
}