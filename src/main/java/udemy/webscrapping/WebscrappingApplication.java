package udemy.webscrapping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WebscrappingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebscrappingApplication.class, args);
	}

	@GetMapping("/")
	public static String helloWorld(){
		return "<h1>Hello World</h1>";
	}

}

