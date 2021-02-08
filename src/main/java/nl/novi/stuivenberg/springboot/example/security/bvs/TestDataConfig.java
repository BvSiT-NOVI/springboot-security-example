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
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        logger.info("Running " + this.getClass().getName());
        if (true) {
            createUser(new User("admin", "admin@novi.nl", "password"), ERole.ROLE_ADMIN);
            createUser(new User("admin2", "admin2@novi.nl", "password"), ERole.ROLE_ADMIN, ERole.ROLE_MANAGER);
            createUser(new User("user3", "user3@novi.nl", "password"), ERole.ROLE_COWORKER, ERole.ROLE_ADMIN);
            createUser(new User("user4", "user4@novi.nl", "password"), ERole.ROLE_ADMIN);
            for (int i = 0; i < 5; i++) {
                createUser(createUser(new User("user", "user@novi.nl", "password"), i + 5, ERole.ROLE_COWORKER));
            }
        }
    }

    public User createUser(User user,ERole... roles){
        for(ERole role:roles ){
            if (roleRepository.existsByName(role)) {
                user.addRole(roleRepository.findByName(role).get());
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {user = userRepository.save(user);} catch (DataAccessException e) {e.printStackTrace();}
        userRepository.findByUsername(user.getUsername())
                .ifPresent(
                        newUser
                                -> {logger.info("User created:");
                                    //logger.info(newUser.toString());},
                                    LogUtil.logMappedObject(newUser,logger);});
        return user;
    }

    public User createUser(User user,int idx ,ERole... roles){
        user.setUsername(user.getUsername()+idx);
        user.setEmail( user.getEmail().replace("@",idx+"@"));
        return createUser(user,roles);
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