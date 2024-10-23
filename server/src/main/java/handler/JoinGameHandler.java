package handler;

import com.google.gson.Gson;
import request.*;
import response.ErrorResponse;
import response.JoinGameResponse;
import response.TakenErrorResponse;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    JoinGameService service;

    public JoinGameHandler(JoinGameService service) {
        this.service = service;
    }

    public String handle(Request request, Response response) {
        String authToken = request.headers("authorization");
        Gson gson = new Gson();
        JoinGameRequestBody body = gson.fromJson(request.body(), JoinGameRequestBody.class);

        // Validate input data early
        if (authToken == null || body.playerColor() == null ||
                (!body.playerColor().equals("WHITE") && !body.playerColor().equals("BLACK")) ||
                body.gameID() <= 0) {
            response.status(400);  // Bad Request
            return gson.toJson(new ErrorResponse("Bad data", "Error: bad request"));
        }

        // Call the join game service
        DataTransfer<?> responseData = service.joinGame(new JoinGameRequest(authToken, body));

        // Handle service response and assign appropriate status codes
        if (responseData.data() instanceof ErrorResponse) {
            response.status(401);  // Unauthorized
            return gson.toJson(responseData.data());
        }

        if (responseData.data() instanceof TakenErrorResponse) {
            response.status(403);  // Forbidden - Color already taken
            return gson.toJson(responseData.data());
        }

        if (responseData.data() instanceof JoinGameResponse) {
            response.status(200);  // OK - Successful join
            return gson.toJson(responseData.data());
        }

        // Default fallback in case of an unexpected error
        response.status(500);  // Internal Server Error
        return gson.toJson(new ErrorResponse("Catastrophic Failure", "Error: Something went seriously wrong here"));
    }
}
