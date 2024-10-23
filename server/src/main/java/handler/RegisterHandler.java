package handler;

import com.google.gson.Gson;
import datatransfer.DataTransfer;
import datatransfer.ErrorResponse;
import datatransfer.RegisterRequest;
import datatransfer.RegisterResponse;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    RegisterService service;
    public RegisterHandler(RegisterService registerService) {
        this.service = registerService;
    }
    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(request.body(),RegisterRequest.class);
        if (registerRequest.username() == null ||
                registerRequest.password() == null ||
                registerRequest.email() == null) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Bad data", "Error: bad request"));
        }
        DataTransfer<?> responseData = service.register(registerRequest);
        if (responseData.data() instanceof ErrorResponse) {
            response.status(403);
            return gson.toJson(responseData.data());
        }
        if (responseData.data() instanceof RegisterResponse) {
            return gson.toJson(responseData.data());
        }
        response.status(500);
        return gson.toJson(new ErrorResponse("Catastrophic Failure","Error: Something went seriously wrong here"));

    }
}
