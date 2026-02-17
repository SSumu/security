package com.springbootacademy.security.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin
public class AccountController {

    @GetMapping("/my-account")
    public String getAccountDetails(){
        System.out.println("awa "); // This is to check if data are coming to this method. But for the result that it was not printed on the Run. Spring Security filters are applied from the moment we apply Spring Security dependency ( starter-security dependency ) to our class path.
//      Filters monitor every request that comes to our backend server one by one. It has many inbuilt filters that are connected to each other like a chain. We can customize and create that filters. Filters check the path from where this end-user is accessing. We need to make sure who this end-user really is. Check if the end-user has admin access. End-user is our user.
//      The end-user accesses the path from where. Then, based on that path and configuration, filters determine whether this is a request from a protected resource or not.
        return "Here are the account details";
    }

}
