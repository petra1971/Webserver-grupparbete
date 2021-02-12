package se.group4.core;

import java.util.List;

public class UserHandler implements URLHandler {
    @Override
    public Response handleURL(Request request) {
        Response response = null;
        if(request.getRequestType().equals("GET")) {
            UserDAO userDAO = new UserDAOWithJPAImpl();
            List<User> user = userDAO.getByName("namn");

            if(user!=null) {
                JsonConverter jsonConverter = new JsonConverter();
                var jsonResponse = jsonConverter.convertToJson(user);
                response.setContentType("application/json"); //ska det vara nån annan typ?
                byte[] jsonBytes = jsonResponse.getBytes();
                response.setContentLength(jsonBytes.length);
                response.setContent(jsonBytes);
                response.setStatusMessage("HTTP/1.1 200 OK");
            } else
            response.setStatusMessage("HTTP/1.1 404");
            response.setContentLength(0);
            String error = "I am error";
            byte[] errorBytes = error.getBytes();
            response.setContent(errorBytes);
        }
        return response;
    }
}
