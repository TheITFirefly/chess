package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.*;
import service.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        // DAO objects
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        // Services
        ClearService clearService = new ClearService(authDAO,userDAO,gameDAO);
        RegisterService registerService = new RegisterService(authDAO,userDAO);
        LoginService loginService = new LoginService(authDAO,userDAO);
        LogoutService logoutService = new LogoutService(authDAO);
        ListGamesService listGamesService = new ListGamesService(authDAO,gameDAO);

        // Handlers
        ClearHandler clearHandler = new ClearHandler(clearService);
        RegisterHandler registerHandler = new RegisterHandler(registerService);
        LoginHandler loginHandler = new LoginHandler(loginService);
        LogoutHandler logoutHandler = new LogoutHandler(logoutService);
        ListGamesHandler listGamesHandler = new ListGamesHandler(listGamesService);

        // Spark configuration
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (clearHandler::handle));
        Spark.post("/user", (registerHandler::handle));
        Spark.post("/session", (loginHandler::handle));
        Spark.delete("/session", (logoutHandler::handle));
        Spark.get("/game", (listGamesHandler::handle));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
