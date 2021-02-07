package nl.novi.stuivenberg.springboot.example.security.repository;

import nl.novi.stuivenberg.springboot.example.security.domain.Role;
import nl.novi.stuivenberg.springboot.example.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * De JpaRepositories die jullie al kennen. Hier wordt gebruik gemaakt van Query Creation. Op basis van de methode naam
 * weten Spring en JPA welke query op de database uitgevoerd moet worden.
 * Hier kan meer informatie over gevonden worden:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findFirstByRolesContains(Role role);
    boolean existsByRolesContains(Role role);

}
