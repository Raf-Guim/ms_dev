package gerencimanto.login.historico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient; // Add this import statement

@SpringBootApplication
@EnableEurekaClient
public class Atividade3Application {

	public static void main(String[] args) {
		SpringApplication.run(Atividade3Application.class, args);
	}

}
