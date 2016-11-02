package restAPI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class Application {
	
	@RequestMapping(value ="/")
	public String getURLValue(HttpServletRequest request){
	    String test = request.getRequestURI();
	    System.out.println(test);
	    return test;
	}
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, "--server.port=${static.port:12345}");
    }
    
}