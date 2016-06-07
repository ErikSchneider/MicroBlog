package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    static HashMap<String, User> userList = new HashMap<>();
    static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {
        Spark.externalStaticFileLocation("Public");
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap map = new HashMap();
                    if (user == null) {
                        return new ModelAndView(map, "index.html");
                    }
                    else {
                        map.put("name", user.name);
                        map.put("messages", messages);
                        map.put("password", user.pass);
                        return new ModelAndView(map, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    String pass = request.queryParams("password");
                    user = userList.get(username);
                    if (user == null) {
                        if (!username.equals("") && !pass.equals("")) {

                            user = new User(username, pass);
                            userList.put(username, user);
                        }
                        else {
                            response.redirect("/");
                        }
                    }
                    if (!pass.equals(user.pass)) {
                       user = null;
                    }

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    String message = request.queryParams("message");
                    messages.add(new Message(message));
                    response.redirect("/");
                    return "";

                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    user = null;
                    response.redirect("/");
                    return "";
                }
        );
    }
}

