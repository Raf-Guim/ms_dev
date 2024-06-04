package gerencimanto.login.historico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient; // Add this import statement

@SpringBootApplication
@EnableEurekaClient
public class HistoricoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoricoApplication.class, args);
	}

}
