package nl.novi.stuivenberg.springboot.example.security.service;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest()
@Import(AuthorizationService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AuthorizationServiceIntegrationTest {
    private static final Logger logger =  LoggerFactory.getLogger(AuthorizationServiceIntegrationTest.class);

/*    @Autowired
    UserRepository userRepository;*/

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired AuthorizationService authorizationService;



    @Autowired
    TestEntityManager em;



    @BeforeEach
    void init(){
        /*
        Role adminRole = em.persist(new Role(ERole.ROLE_ADMIN));
        assertNotNull(adminRole);
        User user = new User("admin","admin@novi.nl","password");
        user.addRole(adminRole);
        em.persistAndGetId(user);

         */
    }

    @Test
    void whenUserWithRoleAdminExists_then_registerAdminThrowsError(){

    }



}
