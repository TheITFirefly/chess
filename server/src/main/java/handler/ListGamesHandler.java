package handler;

import com.google.gson.Gson;
import request.*;
import response.ErrorResponse;
import response.ListGamesResponse;
import service.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    ListGamesService service;
    public ListGamesHandler(ListGamesService listGamesService) {
        this.service = listGamesService;
    }
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        ListGamesRequest listGamesRequest = new ListGamesRequest(request.headers("authorization"));
        DataTransfer<?> responseData = service.listGames(listGamesRequest);
        if (responseData.data() instanceof ErrorResponse) {
            response.status(401);
            return gson.toJson(responseData.data());
        }
        if (responseData.data() instanceof ListGamesResponse) {
            return gson.toJson(responseData.data());
        }
        response.status(500);
        return gson.toJson(new ErrorResponse("Error: Something went seriously wrong here"));
    }
}
