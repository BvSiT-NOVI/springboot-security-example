package nl.novi.stuivenberg.springboot.example.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String generatePublicContent() {
        return "Public Content.";
    }

    //TODO AnyRole = only with defined roles?
    @PreAuthorize("hasRole('COWORKER') or hasRole('MANAGER') or hasRole('EMPLOYEE') or hasRole('ADMIN') ")
    public String generateCoworkerContent() {
        return "Coworker Content.";
    }

    @PreAuthorize("hasRole('MANAGER')")
    public String generateManagerContent() {
        return "Moderator Board.";
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public String generateEmployeeContent() {
        return "Employee Board.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String generateAdminContent() {
        return "Admin Board.";
    }




/*

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String generateUserContent() {
        return "User Content.";
    }


    @PreAuthorize("hasRole('MODERATOR')")
    public String generateModContent() {
        return "Moderator Board.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String generateAdminContent() {
        return "Admin Board.";
    }
*/



}
