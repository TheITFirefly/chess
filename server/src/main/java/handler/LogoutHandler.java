package handler;

import com.google.gson.Gson;
import datatransfer.DataTransfer;
import datatransfer.ErrorResponse;
import datatransfer.LogoutRequest;
import datatransfer.LogoutResponse;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    LogoutService service;
    public LogoutHandler(LogoutService logoutService) {
        this.service = logoutService;
    }

    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));
        DataTransfer<?> responseData = service.logout(logoutRequest);
        if (responseData.data() instanceof ErrorResponse) {
            response.status(401);
            return gson.toJson(responseData.data());
        }
        if (responseData.data() instanceof LogoutResponse) {
            return gson.toJson(responseData.data());
        }
        response.status(500);
        return gson.toJson(new ErrorResponse("Catastrophic Failure","Error: Something went seriously wrong here"));
    }
}
