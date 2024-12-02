package server;

import dataaccess.*;
import handler.*;
import service.*;
import spark.*;
import ws.WebSocketHandler;

public class Server {
    public int run(int desiredPort) {
        // Database DAO objects
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();

        // Services
        ClearService clearService = new ClearService(authDAO,userDAO,gameDAO);
        RegisterService registerService = new RegisterService(authDAO,userDAO);
        LoginService loginService = new LoginService(authDAO,userDAO);
        LogoutService logoutService = new LogoutService(authDAO);
        ListGamesService listGamesService = new ListGamesService(authDAO,gameDAO);
        CreateGameService createGameService = new CreateGameService(authDAO,gameDAO);
        JoinGameService joinGameService = new JoinGameService(authDAO,gameDAO);

        // Handlers
        ClearHandler clearHandler = new ClearHandler(clearService);
        RegisterHandler registerHandler = new RegisterHandler(registerService);
        LoginHandler loginHandler = new LoginHandler(loginService);
        LogoutHandler logoutHandler = new LogoutHandler(logoutService);
        ListGamesHandler listGamesHandler = new ListGamesHandler(listGamesService);
        CreateGameHandler createGameHandler = new CreateGameHandler(createGameService);
        JoinGameHandler joinGameHandler = new JoinGameHandler(joinGameService);

        // Websocket
        WebSocketHandler webSocketHandler = new WebSocketHandler(authDAO,userDAO,gameDAO);

        // Spark configuration
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", webSocketHandler);
        Spark.delete("/db", (clearHandler::handle));
        Spark.post("/user", (registerHandler::handle));
        Spark.post("/session", (loginHandler::handle));
        Spark.delete("/session", (logoutHandler::handle));
        Spark.get("/game", (listGamesHandler::handle));
        Spark.post("/game",(createGameHandler::handle));
        Spark.put("/game",(joinGameHandler::handle));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
