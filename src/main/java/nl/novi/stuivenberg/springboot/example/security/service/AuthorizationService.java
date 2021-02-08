package nl.novi.stuivenberg.springboot.example.security.service;

import nl.novi.stuivenberg.springboot.example.security.domain.ERole;
import nl.novi.stuivenberg.springboot.example.security.domain.Role;
import nl.novi.stuivenberg.springboot.example.security.domain.User;
import nl.novi.stuivenberg.springboot.example.security.payload.request.LoginRequest;
import nl.novi.stuivenberg.springboot.example.security.payload.request.SignupRequest;
import nl.novi.stuivenberg.springboot.example.security.payload.response.JwtResponse;
import nl.novi.stuivenberg.springboot.example.security.payload.response.MessageResponse;
import nl.novi.stuivenberg.springboot.example.security.repository.RoleRepository;
import nl.novi.stuivenberg.springboot.example.security.repository.UserRepository;
import nl.novi.stuivenberg.springboot.example.security.service.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class AuthorizationService {

    private static final String ROLE_NOT_FOUND_ERROR = "Error: Role is not found.";
    private static final String ADMIN_ROLE_ERROR = "Error: User with role admin exists already.";
    private static final String ROLE_ERROR = "Error: role %s cannot be set";

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder passwordEncoder) {
        this.encoder = passwordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     *
     * Deze methode verwerkt de gebruiker die wil registreren. De username en e-mail worden gecheckt. Eventuele rollen
     * worden toegevoegd en de gebruiker wordt opgeslagen in de database.
     *
     * @param signUpRequest de payload signup-request met gebruikersnaam en wachtwoord.
     * @return een HTTP response met daarin een succesbericht.
     */
    public ResponseEntity<MessageResponse> registerUser(@Valid SignupRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        //BvS Without passing a role a user is default enregistered with role COWORKER
        if (strRoles == null) {
            Role coworkerRole = roleRepository.findByName(ERole.ROLE_COWORKER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
            roles.add(coworkerRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                        if (userRepository.existsByRolesContains(new Role(ERole.ROLE_ADMIN))){
                            //Only one admin user can be created.
                            throw new RuntimeException(ADMIN_ROLE_ERROR);
                        }
                        roles.add(adminRole);
                        break;
                    case "emp":
                        Role empRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                        roles.add(empRole);
                        break;
                    case "man":
                        Role manRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                        roles.add(manRole);
                        break;
                    case "coworker":
                    case "cow":
                        Role coworkerRole = roleRepository.findByName(ERole.ROLE_COWORKER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                        roles.add(coworkerRole);
                        break;
                    default:
                        //BvS: Do not permit any other arbitrary role names in the SignupRequest.
                        throw new RuntimeException(ROLE_NOT_FOUND_ERROR);
                        //Debug
                        //BvS Disabled. This permits any arbitrary role name in the SignupRequest.
                        /*
                        Role cowRole = roleRepository.findByName(ERole.ROLE_COWORKER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                        roles.add(cowRole);
                         */
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<MessageResponse> registerWithSingleRole(@Valid SignupRequest signUpRequest,String abbrRole) {
        Set<String> roles = signUpRequest.getRole();
        if (String.join("",roles).equalsIgnoreCase(abbrRole)){
            return registerUser(signUpRequest);
        }
        throw new RuntimeException(ROLE_ERROR);
    }

    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MessageResponse> registerEmployee(@Valid SignupRequest signUpRequest) {
        return registerWithSingleRole( signUpRequest,  "emp");
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> registerManager(@Valid SignupRequest signUpRequest) {
        return registerWithSingleRole( signUpRequest,  "man");
    }

    public ResponseEntity<MessageResponse> registerCoworker(@Valid SignupRequest signUpRequest) {
        Set<String> roles = signUpRequest.getRole();
        if (roles==null || String.join("",roles).equalsIgnoreCase("coworker")){
            //Without passing a role a user is default enregistered with role COWORKER
            return registerUser( signUpRequest);
        }
        throw new RuntimeException(ROLE_ERROR);
    }

    public ResponseEntity<MessageResponse> registerAdmin(@Valid SignupRequest signUpRequest) {
        if (userRepository.existsByRole(ERole.ROLE_ADMIN)){
            //Only one admin user can be created.
            throw new RuntimeException(ADMIN_ROLE_ERROR);
        }
        //User with role ADMIN can have all roles added
        return registerUser(signUpRequest);
    }


    /**
     * Deze methode controleert de ontvangen username en wachtwoord. Het gebruikt hiervoor de
     * AuthenticationManager. I.a.w. Spring security doet die allemaal voor ons.
     *
     * Wanneer de gebruikersnaam/wachtwoord combinatie niet klopt, wordt er een Runtime exception gegooid:
     * 401 Unauthorized. Deze wordt gegooid door
     * {@link nl.novi.stuivenberg.springboot.example.security.service.security.jwt.AuthEntryPointJwt}
     *
     *
     * @param loginRequest De payload met username en password.
     * @return een HTTP-response met daarin de JWT-token.
     */
    public ResponseEntity<JwtResponse> authenticateUser(@Valid LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

}
