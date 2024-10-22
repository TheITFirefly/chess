package handler;

import com.google.gson.Gson;
import datatransfer.DataTransfer;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    ClearService service;
    public ClearHandler(ClearService clearService) {
        this.service = clearService;
    }

    public Response handle(Request request, Response response) {
        Gson gson = new Gson();
        DataTransfer responseData = service.clearDatabase();
        if ("success".equals(responseData.status())) {
            response.status(200);
        } else {
            response.status(500);
            response.body(gson.toJson(responseData.data()));
        }
        return  response;
    }
}
