### Java Multi-Client Chat Messenger (Socket Programming Project):

I created this multi-client chat messenger using Java to better understand how real-time communication works in a client-server environment. The application is designed around a central server that accepts multiple client connections and allows all connected users to exchange messages in real time. Each client runs as its own program, while the server is responsible for managing active connections, receiving messages, and broadcasting them to the appropriate clients.

This project focuses heavily on core networking concepts such as TCP socket communication, multithreading, and object-oriented design. Instead of using external libraries or frameworks, everything is built using standard Java libraries so that the underlying logic of network communication is clear and easy to follow. The goal was not just to make a chat application, but to understand how servers handle multiple users at the same time and how concurrent processes can run without interfering with each other.

--- 

## Features:

- Built using core Java with TCP socket programming to handle network communication.

- Supports multiple clients connecting to a single server at the same time.

- Uses multithreading on the server side so each client is handled independently.

- Enables real-time message broadcasting between all connected clients.

- Handles client connections and disconnections without crashing the server.

- Separates server logic and client logic to keep the code organized and readable.

- Uses a console-based interface to keep the focus on backend networking behavior.

---

## How It Works:

- The server is started first and listens on a specified port for incoming client connections.

- When a client connects, the server creates a new thread dedicated to managing that client’s communication.

- Each client sends messages to the server through an active socket connection.

- The server receives incoming messages and broadcasts them to all other connected clients.

- Because each client runs on its own thread, multiple users can send and receive messages at the same time without blocking one another.

- Clients can safely join or leave the chat without interrupting ongoing communication.

---

## How to Run the Project:

- Open the project in IntelliJ IDEA or any Java-compatible IDE.

- Locate the project’s source files for the server and client programs.

- Run the server program first to start listening for connections.

- Open multiple terminal windows or IDE instances and run the client program in each one.

- Once connected, type messages in any client window to see them broadcast to all connected users in real time.

---

## Notes:

This project was completed as part of a team-based academic final assignment, and this repository represents my personal version of the project for learning and portfolio purposes. Working on this project helped me gain a much clearer understanding of how networking works at a lower level, especially how socket connections are established and how servers manage multiple clients simultaneously. It also gave me hands-on experience with multithreading, synchronization, and designing a clean client-server architecture. Overall, this project strengthened my understanding of backend communication and concurrency in Java and showed how real-time systems are built from the ground up.
