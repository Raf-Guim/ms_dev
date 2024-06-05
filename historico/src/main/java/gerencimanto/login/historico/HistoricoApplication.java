package gerencimanto.login.historico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HistoricoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoricoApplication.class, args);
	}

}
