package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.ClearHandler;
import service.ClearService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        // DAO objects
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        // Services
        ClearService clearService= new ClearService(authDAO,userDAO,gameDAO);

        // Handlers
        ClearHandler clearHandler = new ClearHandler(clearService);

        // Spark configuration
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (clearHandler::handle));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
