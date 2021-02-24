package se.group4.core;

import java.io.IOException;

public class UserHandler implements URLHandler {

    public void createAndAddUser(String id, String firstname, String lastname) {
        UserDAO pdao = new UserDAOWithJPAImpl();
        pdao.create(id,firstname,lastname);
    }

    @Override
    public Response handleURL(Request request) {
        Response response = new Response();
        response.setStatusMessage("HTTP/1.1 303 See other \r\nLocation: /submitted.html");

        try {
            postRequest(request.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void postRequest(String bodyIn) throws IOException {

        String[] body = bodyIn.split("&");

        String id = body[0].substring(body[0].indexOf("=") + 1);
        String firstname = body[1].substring(body[1].indexOf("=") + 1);
        String lastname = body[2].substring(body[2].indexOf("=") + 1);

        createAndAddUser(id,firstname,lastname);
    }
}