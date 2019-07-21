package pl.ynleborg.ynlebapiorg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class YnlebapiorgApplication {

	public static void main(String[] args) {
		SpringApplication.run(YnlebapiorgApplication.class, args);
	}
}
