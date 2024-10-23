package handler;

import com.google.gson.Gson;
import datatransfer.*;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    CreateGameService service;
    public CreateGameHandler(CreateGameService service) {
        this.service = service;
    }

    public String handle(Request request, Response response){
        String authToken = request.headers("authorization");
        Gson gson = new Gson();
        CreateGameRequestBody body = gson.fromJson(request.body(),CreateGameRequestBody.class);
        if (body.gameName() == null || authToken == null) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Bad data", "Error: bad request"));
        }
        DataTransfer<?> responseData = service.createGame(new CreateGameRequest(authToken,body));
        if (responseData.data() instanceof  ErrorResponse) {
            response.status(401);
            return gson.toJson(responseData.data());
        }
        if (responseData.data() instanceof CreateGameResponse) {
            return gson.toJson(responseData.data());
        }
        response.status(500);
        return gson.toJson(new ErrorResponse("Catastrophic Failure","Error: Something went seriously wrong here"));
    }
}
