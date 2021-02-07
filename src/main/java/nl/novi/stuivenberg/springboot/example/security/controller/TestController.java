package nl.novi.stuivenberg.springboot.example.security.controller;

import nl.novi.stuivenberg.springboot.example.security.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
