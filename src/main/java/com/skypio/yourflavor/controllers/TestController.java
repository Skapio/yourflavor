package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.entity.Test;
import com.skypio.yourflavor.repository.TestRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    private final TestRepository testRepository;
    public TestController(TestRepository testRepository)
    {
        this.testRepository = testRepository;
    }

    @GetMapping("/getinformations")
    public List<Test> getAllByUserId(Principal principal)
    {
        List<Test> tests = testRepository.findByUserId(principal.getName());

        return tests;
    }

    @PostMapping("/add")
    public Test addTest(Principal principal, @RequestParam String marka, @RequestParam String model)
    {
       Test test = new Test(null, principal.getName(), marka, model);

       return testRepository.save(test);
    }

    @GetMapping("/test")
    public Principal test(Principal principal)
    {
        return principal;
    }

}
