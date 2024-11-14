package handler;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import errors.DataAccessException;
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

        try {
            LoginResponse loginResponse = service.login(loginRequest);
            response.status(200);
            return gson.toJson(loginResponse);
        } catch (UserDAO.UnauthorizedAccessException e) {
            response.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}
