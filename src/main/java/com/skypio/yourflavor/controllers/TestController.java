package com.skypio.yourflavor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class TestController {


    @GetMapping("/getinformations")
    public String getAllByUserId(Principal principal)
    {
        // List<Test> tests = testRepository.findByUserId(principal.getName());

        return "test";
    }


    @GetMapping("/test")
    public Principal test(Principal principal)
    {
        return principal;
    }

}
