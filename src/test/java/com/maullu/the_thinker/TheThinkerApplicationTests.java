package com.maullu.the_thinker;

import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.Visibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TheThinkerApplicationTests {

	@Container
	@ServiceConnection //It configures automatically the testDb
	static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:17");

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	IdeasRepository ideasRepository;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setup(){
		ideasRepository.deleteAll();
		Idea idea1 = new Idea(null, "FirstIdea", "FirstDes", Visibility.PUBLIC);
		Idea idea2 = new Idea(null, "SecondIdea", "SecondDes", Visibility.PRIVATE);
		ideasRepository.save(idea1);
		ideasRepository.save(idea2);
		System.out.println("TODOSSS: " + ideasRepository.findAll());
	}

	@Test
	void shouldFindByVisibility(){
		assertThat(ideasRepository.findByVisibility(Visibility.PUBLIC).getFirst().getTitle()).isEqualTo("FirstIdea");
	}


}
