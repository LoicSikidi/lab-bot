package restAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {
	
	@RequestMapping("")
    @ResponseBody
    String home() {
        return "Hello Suricats !";
    }
	
	@RequestMapping("/")
    @ResponseBody
    String home2() {
        return home();
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}