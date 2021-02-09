package nl.novi.stuivenberg.springboot.example.security.service;

import nl.novi.stuivenberg.springboot.example.security.domain.User;
import nl.novi.stuivenberg.springboot.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    UserRepository userRepository;

    public String generatePublicContent() {
        return "Public Content.";
    }

    //TODO AnyRole = only with defined roles?
    @PreAuthorize("hasRole('COWORKER') or hasRole('MANAGER') or hasRole('EMPLOYEE') or hasRole('ADMIN') ")
    public String generateCoworkerContent(Long id,String token) {
        authorizationService.matches(id,token);
        return generateCoworkerContent();
    }

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


    //@PreAuthorize("hasRole('ADMIN')")

    //@PreAuthorize("hasRole('ROLE_ADMIN') or #user.id == #userId")

/*
    //OK!
    @PreAuthorize("#user.id == #userId")
    public UserDetailsImpl getUser(@AuthenticationPrincipal UserDetailsImpl user,Long userId){
        return user;
    }
*/

    @PreAuthorize("(hasRole('ROLE_COWORKER') and #authUser.id == #userId) or (hasRole('ROLE_ADMIN'))")
    public User getUser(@AuthenticationPrincipal UserDetailsImpl authUser,Long userId){
        return userRepository.findById(authUser.getId()).orElseThrow(RuntimeException::new);
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
