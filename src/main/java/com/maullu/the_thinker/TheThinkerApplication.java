package com.maullu.the_thinker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TheThinkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheThinkerApplication.class, args);
	}

}
