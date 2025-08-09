# Device-Manager

---

## Running the Application

### Prerequisites

- Java 21 or higher
- Maven 3.9+
- Docker & Docker Compose

### Steps

1. **Clone the repository:**
   ```sh
   git clone https://github.com/nunopinhao/Device-Manager.git
   cd Device-Manager
   ```

2. **Compile the application:**
   ```sh
   mvn clean install
   ```

3. **Run the app in Docker:**
   ```sh
   docker compose up 
   ```
   The app will be available at [http://localhost:8090/api/device-manager/](http://localhost:8090/api/device-manager/).


4. **API Documentation:**
   
   Visit [http://localhost:8090/api/device-manager/swagger-ui/index.html](http://localhost:8090/api/device-manager/swagger-ui/index.html).


5. **Postman Collection:**

   A Postman collection with example requests is available at:
   - `postman/DeviceManager.postman_collection.json`

    To use:
    1. Open Postman.
    2. Import the collection file.
    3. Use the requests as templates for interacting with the API.
---

## Project Structure

- `src/main/java/` — Main Java source code
    - `controller/` — Implements API endpoints defined by Swagger/OpenAPI
    - `service/` — Business logic interfaces and implementations
    - `repository/` — Spring Data repositories for MongoDB or other databases.
    - `model/` — Data transfer objects (DTOs) and domain models
    - `document/` — MongoDB document representations
    - `mapper/` — MapStruct mappers for DTO ↔ Document conversion
    - `validator/` — Custom validators and validation utilities for request and domain objects
    - `exception/` — Custom exceptions and global exception handlers.
    - `util/` — Utility classes and helper functions used across the project.
- `src/main/resources/` — Configuration files (e.g., `application.yaml`, OpenAPI spec)
- `postman/` — Postman collection with example API requests
- `pom.xml` — Maven build and dependency configuration
- `README.md` — Project overview and usage instructions
- `docker-compose.yaml` — Docker Compose setup for local development


Swagger/OpenAPI is used for API documentation and code generation, enabling clear separation between API, domain, and persistence layers. This approach ensures consistent interfaces, supports versioning, and promotes scalability and maintainability as the project evolves.
on controller implemented the api defined on the swagger

## Future Improvements
  - CI/CD Pipeline: Set up continuous integration and deployment workflows for automated builds, tests, and deployments.
  - Error Handling: Improve global exception handling for more consistent and informative error responses. 
  - Increase test coverage: Add unit and integration tests to ensure code quality and reliability.
  - Authentication & Authorization: Integrate security mechanisms (e.g., OAuth2, JWT) to protect API endpoints and manage user roles and permissions.
  - Centralized Logging: Add structured logging (e.g., SLF4J with Logback) to make it easier to track and monitor application activity.