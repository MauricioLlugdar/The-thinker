package com.maullu.the_thinker;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.Role;
import com.maullu.the_thinker.Model.User;
import com.maullu.the_thinker.Model.Visibility;
import com.maullu.the_thinker.Repository.UserRepository;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maullu.the_thinker.Repository.IdeasRepository;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TheThinkerApplicationTests {

	Idea idea1;
	Idea idea2;
	Idea idea3;
	User user;

	@Container
	@ServiceConnection //It configures automatically the testDb
	static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:17");

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	IdeasRepository ideasRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	ResponseEntity<String> findById(Long id){
		return restTemplate
				.getForEntity("/ideas/"+id, String.class);
	}

	@BeforeEach
	void setup(){
		ideasRepository.deleteAll();
		userRepository.deleteAll();

		User user = new User(null, "Carlos", "cgimenez@gmail.com", "123", Role.USER);
		this.user = userRepository.save(user);

		Idea idea1 = new Idea(null, "FirstIdea", "FirstDes", Visibility.PUBLIC, this.user.getId());
		Idea idea2 = new Idea(null, "SecondIdea", "SecondDes", Visibility.PRIVATE, this.user.getId());
		Idea idea3 = new Idea(null, "ThirdIdea", "ThirdDes", Visibility.PUBLIC, this.user.getId());
		this.idea1 = ideasRepository.save(idea1);
		this.idea2 = ideasRepository.save(idea2);
		this.idea3 = ideasRepository.save(idea3);
	}

	@Test
	void shouldFindByVisibility(){
		ResponseEntity<String> response = restTemplate
				.getForEntity("/ideas/visibility/"+ Visibility.PUBLIC +"?page=0&size=10", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray ideasFound = documentContext.read("$[*]");
		assertThat(ideasFound.size()).isEqualTo(2);
		List<String> visibilities = documentContext.read("[*].visibility");
		visibilities.forEach( visibility -> assertThat(visibility).isEqualTo(Visibility.PUBLIC.toString()));
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

	@Test
	void shouldReturnTheIdeaById(){
		ResponseEntity<String> response = findById(idea1.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id.longValue()).isEqualTo(idea1.getId());
	}

	@Test
	@DirtiesContext
	void shouldCreateNewIdea(){
		Idea newIdea = new Idea(null, "IDEA", "FIRST IDEA EVER CREATED", Visibility.PROTECTED, this.user.getId());
		ResponseEntity<Void> createResponse = restTemplate
				.postForEntity("/ideas", newIdea, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfTheNewIdea = createResponse.getHeaders().getLocation();
		ResponseEntity<String> findResponse = restTemplate
				.getForEntity(locationOfTheNewIdea, String.class);
		assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(findResponse.getBody());
		Number id = documentContext.read("$.id");
		String title = documentContext.read("$.title");
		assertThat(title).isEqualTo("IDEA");
		assertThat(id.longValue()).isNotNull();
	}

	@Test
	@DirtiesContext
	void shouldUpdateAnIdea(){
		Map<String, Object> updates = new HashMap<>();
		String updatedTitle = "Updated Title";
		updates.put("title", updatedTitle);
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(updates);
		ResponseEntity<Void> updateResponse = restTemplate
				.exchange("/ideas/"+idea1.getId(), HttpMethod.PATCH, request, Void.class);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> findUpdatedResponse = findById(idea1.getId());
		assertThat(findUpdatedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(findUpdatedResponse.getBody());
		String title = documentContext.read("$.title");
		assertThat(title).isEqualTo(updatedTitle);
	}

	@Test
	@DirtiesContext
	void shouldDeleteAnIdea(){
		ResponseEntity<Void> deleteResponse = restTemplate
				.exchange("/ideas/" + idea1.getId(), HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> findDeletedIdea = findById(idea1.getId());
		assertThat(findDeletedIdea.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void deleteNotExistingIdea(){
		ResponseEntity<Void> deleteResponse = restTemplate
				.exchange("/ideas/10000", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void getNotExistingIdeaById(){
		ResponseEntity<String> response = findById(10000L);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}


}
