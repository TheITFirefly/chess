package handler;

import com.google.gson.Gson;
import request.*;
import response.ErrorResponse;
import response.LoginResponse;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    LoginService service;
    public LoginHandler(LoginService loginService) {
        this.service = loginService;
    }

    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(request.body(),LoginRequest.class);
        if (loginRequest.username() == null ||
                loginRequest.password() == null) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        }
        DataTransfer<?> responseData = service.login(loginRequest);
        if (responseData.data() instanceof ErrorResponse) {
            response.status(401);
            return gson.toJson(responseData.data());
        }
        if (responseData.data() instanceof LoginResponse) {
            return gson.toJson(responseData.data());
        }
        response.status(500);
        return gson.toJson(new ErrorResponse("Error: Something went seriously wrong here"));
    }
}
