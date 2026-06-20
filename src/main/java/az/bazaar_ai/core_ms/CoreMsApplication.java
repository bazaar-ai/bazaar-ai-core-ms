package az.bazaar_ai.core_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class CoreMsApplication {

	static void main(String[] args) {
		SpringApplication.run(CoreMsApplication.class, args);
	}
}
