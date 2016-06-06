package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    static ArrayList<User> userList = new ArrayList<>();
    static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {
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
                        return new ModelAndView(map, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    user = new User(username);
                    userList.add(user);
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
    }
}

