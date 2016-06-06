package com.theironyard;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    static ArrayList<User> userList = new ArrayList<>();
    static ArrayList<String> messages = new ArrayList<>();

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
                        map.put("message", messages);
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
                    user = new User(message);
                    messages.add(message);
                    response.redirect("/");
                    return "";
                }
        );
    }
}

