package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> userMap = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    HashMap m = new HashMap();
                    if (username == null){
                        return new ModelAndView(m, "index.html");
                    }
                    else {
                        User user = userMap.get(username);
                        m.put("name", username);
                        m.put("messages", user.messages);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                (request, response) -> {
                    String name = request.queryParams("username");
                    String pass = request.queryParams("password");
                    if (name == null || pass == null) {
                        throw new Exception("Name or pass not sent");
                    }

                    User user = userMap.get(name);
                    if (user == null) {
                        user = new User(name, pass);
                        userMap.put(name, user);
                    }
                    else if (!name.equals(user.name)) {
                        throw new Exception("Wrong password");
                    }

                    Session session = request.session();
                    session.attribute("username", name);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    String message = request.queryParams("message");
                    if (message.isEmpty()) {
                        throw new Exception("Invalid form field");
                    }

                    User user= userMap.get(username);
                    if (user == null) {
                        throw new Exception("User does not exist");
                    }

                    Message m1 = new Message(message);
                    user.messages.add(m1);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));

                    User user = userMap.get(username);
                    if (id <= 0 || id -1 >= user.messages.size()) {
                        throw new Exception("Invalid id");
                    }

                    user.messages.get(id -1);
                    Message m1 = new Message(request.queryParams("message2"));
                    //user.messages.remove(id - 1);

                    String message = request.queryParams("message2");
                    if (message.isEmpty()) {
                        throw new Exception("Invalid form field");
                    }

                    m1.message = message;
                    user.messages.set(id -1, m1);

                    //Message m2 = new Message(message);
                    //user.messages.add(m2);
                    // get message from user's message list
                    // m.message = message;

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
               "/delete-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));

                    User user = userMap.get(username);
                    if (id <= 0 || id -1 >= user.messages.size()) {
                        throw new Exception("Invalid id");
                    }
                    user.messages.remove(id - 1);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                }
        );
    }
}

