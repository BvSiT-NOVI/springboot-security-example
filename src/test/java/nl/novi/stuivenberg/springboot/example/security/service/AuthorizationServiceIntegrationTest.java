package nl.novi.stuivenberg.springboot.example.security.service;

import nl.novi.stuivenberg.springboot.example.security.domain.ERole;
import nl.novi.stuivenberg.springboot.example.security.domain.Role;
import nl.novi.stuivenberg.springboot.example.security.domain.User;
import nl.novi.stuivenberg.springboot.example.security.payload.request.SignupRequest;
import nl.novi.stuivenberg.springboot.example.security.repository.RoleRepository;
import nl.novi.stuivenberg.springboot.example.security.repository.UserRepository;
import nl.novi.stuivenberg.springboot.example.security.service.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = arrayOf("spring.h2.console.enabled=true"))
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorizationServiceIntegrationTest {
    private static final Logger logger =  LoggerFactory.getLogger(AuthorizationServiceIntegrationTest.class);

    @Autowired AuthorizationService authorizationService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void init(){
        /*
        Role adminRole = em.persist(new Role(ERole.ROLE_ADMIN));
        assertNotNull(adminRole);
        User user = new User("admin","admin@novi.nl","password");
        user.addRole(adminRole);
        em.persistAndGetId(user);

         */

       // userRepository.deleteAll();

    }

    @Test
    void whenUserWithRoleAdminExists_then_registerAdminThrowsError(){
        //Arrange
        //userRepository.deleteAll();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
        assertNotNull(adminRole);
        User user = new User("admin","admin@novi.nl","password");
        //user.addRole(adminRole);
        //user.getRoles().add(adminRole);
        logger.info("Number of users: "+userRepository.findAll().size());
        userRepository.save(user);

/*
        //Act
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("admin2");
        signupRequest.setPassword("password");
        signupRequest.setEmail("admin2@novi.nl");
        signupRequest.setRole(Collections.singleton("adm"));

        authorizationService.registerAdmin(signupRequest);

        //Assert
*/
    }

    @Transactional //Important! Reverts all changes to the database
    @Test
    void when_registerCoworkerWithoutRoleInRequest_thenIsSavedWithRoleCoworker(){
        //Arrange
        userRepository.deleteAll();
        User user = new User("coworker100","coworker100@novi.nl","password");

        //Act
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(user.getUsername());
        signupRequest.setPassword(user.getPassword());
        signupRequest.setEmail(user.getEmail());
        authorizationService.registerCoworker(signupRequest);

        //Assert
        User savedUser = userRepository.findByUsername(user.getUsername()).get();
        String actual = savedUser.getUsername();
        String expected =user.getUsername();
        assertEquals(actual,expected);
        assertEquals(savedUser.getRoles().size(), 1);
        assertSame(savedUser.getRoles().iterator().next().getName(), ERole.ROLE_COWORKER);

    }



/*
    @Transactional
    public void insertWithQuery(User user) {
        em.createNativeQuery("INSERT INTO user (username) VALUES (?)")
                .setParameter(1, user.getUsername())
                .executeUpdate();
    }
*/

}
