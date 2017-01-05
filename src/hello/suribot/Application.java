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
		
    	for(EnvVar var : EnvVar.values()){
    		System.out.println(var + " : "+ var.getValue());
    		if(var.getValue()==null){
    			System.err.println("Check your environment var "+var.toString()+"... ");
    			System.exit(0);
    		}
    	}
		
    }
    
}