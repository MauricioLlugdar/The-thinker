package com.maullu.the_thinker;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.Visibility;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.data.domain.Pageable;
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
	}

	@Test
	void shouldFindTheFirstByVisibility(){
		Pageable firstPage = PageRequest.of(0, 1);
		assertThat(ideasRepository.findByVisibility(Visibility.PUBLIC, firstPage).getFirst().getTitle()).isEqualTo("FirstIdea");
	}

	@Test
	void shouldFindAllTheIdeasWhenIsRequested(){
		ResponseEntity<String> response = restTemplate
				.getForEntity("/ideas?page=0&size=2", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray ideasFound = documentContext.read("$[*]");
		assertThat(ideasFound.size()).isEqualTo(2);

	}




}
