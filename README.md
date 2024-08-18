# Battle Ship
# Game Server Project

## Overview
This project is a multiplayer game server built using Java and Spring Boot. It involves various technologies and concepts such as multithreading, networking, PostgreSQL database, and Java Swing for the user interface.

## Technologies Used
- **Java**: The primary programming language used for the project.
- **Spring Boot**: A framework used to create stand-alone, production-grade Spring-based applications.
- **Maven**: A build automation tool used for managing project dependencies and building the project.
- **PostgreSQL**: A powerful, open-source object-relational database system used for storing game data.
- **Java Swing**: A GUI widget toolkit for Java used to create the user interface.
- **Multithreading**: Used to handle multiple client connections and game logic concurrently.
- **Networking**: Utilized for communication between the client and server using sockets.
- **Lombok**: A Java library that helps to reduce boilerplate code by generating getters, setters, and other methods at compile time.
- **JSON**: Used for data interchange between the client and server.
- **JUnit**: A testing framework used for unit testing the application.
- **Mockito**: A mocking framework used for testing the interactions between objects.

## Project Structure
- `src/main/java/org/example/`: Contains the main Java source files.
  - `GameServer.java`: Manages the game logic and interactions with the database.
  - `ClientThread.java`: Handles individual client connections and game interactions.
  - `TimerThread.java`: Manages the game timer for each player.
  - `HttpClient.java`: Utility class for sending HTTP requests to the server.
  - `shipsModels/`: Contains classes representing different types of ships in the game.
  - `exception/`: Contains custom exception classes used in the project.
- `src/main/resources/`: Contains configuration files and resources.
- `src/test/java/org/example/`: Contains test classes for unit testing.
- `pom.xml`: Maven configuration file for managing dependencies.

## Key Concepts
### Multithreading
The project uses multithreading to handle multiple client connections simultaneously. Each client connection is managed by a separate `ClientThread`, allowing for concurrent gameplay and interactions. The `TimerThread` class is used to manage the game timer for each player, ensuring that each player has a limited amount of time to make their moves.

### Networking
Networking is a crucial part of the project, enabling communication between the client and server. The server listens for client connections on a specified port and handles incoming requests using sockets. The `HttpClient` class is used to send HTTP requests to the server for various operations such as updating player statistics and game states.

### Database Integration
The project uses PostgreSQL to store game data such as player information, game states, and ship positions. The `GameServer` class interacts with the database to update and retrieve data as needed. The database schema includes tables for players, games, and ships, with relationships defined between them to maintain data integrity.

### Java Swing
Java Swing is used to create the graphical user interface for the game. It provides a rich set of components for building the game's UI, allowing for a more interactive and user-friendly experience. The UI includes features such as a game board, player statistics, and game controls.

### RESTful API
The project uses RESTful APIs to interact with the database and perform CRUD operations. The `HttpClient` class is used to send HTTP requests to the server, and the server exposes endpoints for various operations such as updating player statistics, retrieving game states, and managing ships.

### Exception Handling
Custom exception handling is implemented to manage errors and ensure the application runs smoothly. The `exception` package contains custom exception classes used throughout the project. These exceptions are used to handle various error scenarios such as invalid input, database errors, and network issues.

### Timer Management
The `TimerThread` class is used to manage the game timer for each player, ensuring that each player has a limited amount of time to make their moves. The timer is started and stopped based on the game state and player actions, and the remaining time is displayed to the player.

### JSON Processing
The project uses JSON for data interchange between the client and server. The `JSONObject` class is used to create and parse JSON data. This allows for easy serialization and deserialization of data, making it easier to send and receive complex data structures.

### Lombok
Lombok is used to reduce boilerplate code by generating getters, setters, and other methods at compile time. This helps to keep the code clean and maintainable. Annotations such as `@Getter`, `@Setter`, and `@ToString` are used to automatically generate the corresponding methods.

### Unit Testing
JUnit is used for unit testing the application. Test classes are created for various components of the project to ensure that they function correctly. Mockito is used to mock dependencies and test the interactions between objects. This helps to ensure that the application is robust and free of bugs.

### Logging
Logging is implemented using the SLF4J (Simple Logging Facade for Java) library. This allows for logging important events and errors, making it easier to debug and monitor the application. Log messages are written to a log file, which can be reviewed to diagnose issues and track the application's behavior.

### Configuration Management
Configuration management is handled using Spring Boot's configuration properties. Configuration files such as `application.properties` are used to define various settings such as database connection details, server port, and other application-specific properties. This allows for easy customization and management of the application's configuration.

## How to Run
1. **Clone the repository**: `git clone <repository-url>`
2. **Navigate to the project directory**: `cd <project-directory>`
3. **Build the project using Maven**: `mvn clean install`
4. **Run the Spring Boot application**: `mvn spring-boot:run`
5. **Start the game client**: Run the `ClientThread` class to connect to the server and start playing.

## Conclusion
This project demonstrates the use of various technologies and concepts to build a robust multiplayer game server. It leverages the power of Java and Spring Boot for backend development, PostgreSQL for data storage, and Java Swing for the user interface, providing a comprehensive solution for multiplayer gaming. The project also incorporates best practices such as unit testing, logging, and configuration management to ensure that the application is maintainable and scalable.
