# The Thinker API

The Thinker is a RESTful API built with Java Spring Boot and PostgreSQL that allows users to manage and share ideas. Each idea can be public, private or protected(only friends and you), and the API provides endpoints (CRUD and more) to create, read, update, delete and filter ideas by visibility.

This project is designed as a backend service for applications that need to store and manage creative thoughts, brainstorming sessions, or knowledge sharing platforms.


## Getting Started

### Prerequisites

- Java 17 or higher installed.
- Docker installed (for Testcontainers).
- A running PostgreSQL instance (optional—Testcontainers handles tests automatically).

### Installation & Running

1. **Clone this repository:**

   ```bash
   git clone https://github.com/MauricioLlugdar/The-thinker.git
   cd The-thinker
   ```

2. **Configure the database** (if needed):

   - Edit `application.properties` or use a `.env` for your PostgreSQL credentials.
   - By default, Testcontainers is used during tests—no manual setup required.

3. **Run the application:**

   Using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

   Or with your IDE.

---

## Endpoints

See the endpoints while running the API.

Link of the Swagger:
[Endpoints](http://localhost:8080/swagger-ui.html)

---

## Testing

Run unit and integration tests with:

```bash
./mvnw test
```

Integration tests spin up a PostgreSQL container automatically via **Testcontainers**, ensuring test isolation and reproducibility.

---

## Project Structure

```
src/
 ├── main/
 │    ├── java/com/maullu/the_thinker/
 │    │    ├── Controllers/
 │    │    ├── Model/
 │    │    ├── repository/
 │    │    └── Aplication
 │    └── resources/
 └── test/
      └── java/... (integration tests using TestRestTemplate)
```

---

## Future Enhancements

- Add authentication (e.g., with Spring Security and user entities)
- Allow tagging or categorization of ideas

---

## License

This project is open source and available under the [MIT License](LICENSE).

---

## Author

**Mauricio Llugdar** – _Academic Project_  
[GitHub](https://github.com/MauricioLlugdar) • [LinkedIn](https://www.linkedin.com/in/mauricio-jeremias-llugdar-8bbab41b6/)