package handler;

import com.google.gson.Gson;
import request.DataTransfer;
import response.ErrorResponse;
import response.ClearResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    ClearService service;
    public ClearHandler(ClearService clearService) {
        this.service = clearService;
    }

    public String handle(Request request, Response response) {
        Gson gson = new Gson();
        DataTransfer<?> responseData = service.clearDatabase();

        // Data is a ClearResponse
        if (responseData.data() instanceof ClearResponse) {
            return gson.toJson(responseData.data());
        }

        if (responseData.data() instanceof ErrorResponse) {
            response.status(500);
            return gson.toJson(responseData.data());
        }
        response.status(500);
        return gson.toJson(new ErrorResponse("Error: Something went seriously wrong here").message());
    }

}
