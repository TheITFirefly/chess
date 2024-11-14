package handler;

import com.google.gson.Gson;
import errors.DataAccessException;
import errors.DuplicateEntryException;
import request.RegisterRequest;
import response.ErrorResponse;
import response.RegisterResponse;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    private final RegisterService service;

    public RegisterHandler(RegisterService registerService) {
        this.service = registerService;
    }

    public String handle(Request request, Response response) {
        Gson gson = new Gson();

        // Parse request body into RegisterRequest object
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);

        // Validate required fields
        if (registerRequest.username() == null ||
                registerRequest.password() == null ||
                registerRequest.email() == null) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        }

        try {
            RegisterResponse responseData = service.register(registerRequest);
            response.status(200);
            return gson.toJson(responseData);

        } catch (DuplicateEntryException e) {
            // Username is already taken
            response.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));

        } catch (DataAccessException e) {
            // Probably a database error
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}
