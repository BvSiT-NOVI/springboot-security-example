package nl.novi.stuivenberg.springboot.example.security.controller;

import nl.novi.stuivenberg.springboot.example.security.domain.User;
import nl.novi.stuivenberg.springboot.example.security.service.TestService;
import nl.novi.stuivenberg.springboot.example.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/all")
    public String allAccess() {
        return testService.generatePublicContent();
    }

    @GetMapping("/coworker")
    public String coworkerAccess() {
        return testService.generateCoworkerContent();
    }

    @GetMapping("/coworkers/{id}")
    public User coworkerTest(@AuthenticationPrincipal UserDetailsImpl authUser, @PathVariable Long id) {
        System.out.println(id);
        return testService.getUser(authUser,id);
    }


/*
    @GetMapping("/coworkers/{id}")
    public UserDetailsImpl coworkerAccessSecured(@PathVariable Long id, @RequestHeader(name="Authorization") String token) {
        re
    }*/

    @GetMapping("/employee")
    public String employeeAccess() {
        return testService.generateEmployeeContent();
    }

    @GetMapping("/manager")
    public String managerAccess() {
        return testService.generateManagerContent();
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return testService.generateAdminContent();
    }
}
