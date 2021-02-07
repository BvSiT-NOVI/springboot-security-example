package nl.novi.stuivenberg.springboot.example.security.bvs;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.stuivenberg.springboot.example.security.domain.ERole;
import nl.novi.stuivenberg.springboot.example.security.domain.Role;
import nl.novi.stuivenberg.springboot.example.security.domain.User;
import nl.novi.stuivenberg.springboot.example.security.payload.request.SignupRequest;
import nl.novi.stuivenberg.springboot.example.security.repository.RoleRepository;
import nl.novi.stuivenberg.springboot.example.security.repository.UserRepository;
import nl.novi.stuivenberg.springboot.example.security.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

/**
 * See nl.novi.stuivenberg.springboot.example.security.DatabaseFiller
 * Component annotation: see http://zetcode.com/springboot/component/
 */
@Component
public class TestDataConfig implements CommandLineRunner {
    private static final Logger logger =  LoggerFactory.getLogger(TestDataConfig.class);
    final static boolean VERBOSE=true;

    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void run(String... args)  {
        logger.info("Running "+ this.getClass().getName());
        User user = new User("admin","admin@novi.nl","password")
                .addRole(roleRepository.findByName(ERole.ROLE_ADMIN).get());
        user = userRepository.save(user);
        logger.info("Saved new user:");
        LogUtil.logMappedObject(user,logger);

    }
}

/*

        SignupRequest signupRequest =new SignupRequest();
        signupRequest.setUsername("admin");
        signupRequest.setEmail("admin@novi.nl");
        signupRequest.setPassword("password");
        signupRequest.setRole(new HashSet<>(Arrays.asList("admin")));
        authorizationService.registerUser(signupRequest);
        userRepository.findByUsername("admin")
                .ifPresentOrElse(
                        newUser
                            -> {logger.info("User created:");
                                logger.info(newUser.toString());},
                        ()
                            -> logger.info("New user was not saved: "+signupRequest.getUsername()));
 */