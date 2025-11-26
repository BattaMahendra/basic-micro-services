package com.mahi.pds.controllers;


import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LearningController {

    @GetMapping                    //this can be accessed with http://localhost:808/api
    public String welcome(){
        return "Hello!";
    }

    @GetMapping("/name")
    public String welcomeWithSpecificUser( String name){
        return "Hello "+name;
    }

    /*
    * Multiple paths*/
    @GetMapping({"/greet", "/hello"})
    public String multiplePaths() {
        return "Hello!";
    }

    /*
    * Request params
    *
    * url: http://localhost:808/api/users?id=5&active=true
    * */

    @GetMapping("/users")
    public String getUser(@RequestParam int id,
                          @RequestParam(defaultValue = "false") boolean active) {
        return "User " + id + ", Active: " + active;
    }


    /*
    * PathVariable with same as method parameter and different method parameter*/

    @GetMapping("/users/{id}/{name}")
    public String getUserById(@PathVariable("id") int userId, @PathVariable String name) {
        return "User with ID: " + userId+" and name :"+name;
    }

    /*
    * Optional paths
    * And also we can have pathVariable not mandatory by providing false*/
    @GetMapping({"/items", "/items/{id}"})
    public String getItem(@PathVariable(required = false) Integer id) {
        return id == null ? "All items" : "Item " + id;
    }

    /*
    * Manipulating output type of content
    *
    * produces / consumes → content negotiation.
    *
    * you can also experiment with consumes*/

    @GetMapping(value = "/text", produces = "text/plain")
    public String getText() {
        return "Plain text response";
    }

    @GetMapping(value = "/json", produces = "application/json")
    public Map<String, String> getJson() {
        return Map.of("message", "Hello JSON");
    }

    @GetMapping(value = "/xml", produces = "application/xml")
    public String getXml() {
      return  "<message>Welcome</message>";
    }


    @RequestMapping(value = " /k ", method = RequestMethod.GET)
    public String getHello(){
        return "hola";
    }


    /*

     * Path matching with stars - for example observe below

        /files/* matches /files/image.jpg
        /files/** matches /files/images/2023/photo.png
        /files/{filename} matches /files/report.pdf, with filename bound to report.pdf

     */


}
