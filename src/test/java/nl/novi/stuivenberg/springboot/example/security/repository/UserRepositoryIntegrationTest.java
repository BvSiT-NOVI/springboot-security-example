package nl.novi.stuivenberg.springboot.example.security.repository;

import nl.novi.stuivenberg.springboot.example.security.domain.ERole;
import nl.novi.stuivenberg.springboot.example.security.domain.Role;
import nl.novi.stuivenberg.springboot.example.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional //Important! Reverts all changes to the database
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //use Postgres DB
class UserRepositoryIntegrationTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	@BeforeEach
	void init(){
		userRepository.deleteAll(); //TODO Is needed both here and in individual tests??
	}

	@Test
	void testSet(){
		//Arrange
		Set<String> roles = new HashSet<>();
		//Act
		String expected = "";
		String actual = String.join("",roles);
		//Assert
		assertTrue(expected.equalsIgnoreCase(actual));
	}

	@Test
	void test_findFirstByRolesContains(){
		//Arrange
		userRepository.deleteAll(); //TODO Is needed both here and in individual tests??
		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
		assertNotNull(adminRole);
		User user = new User("admin","admin@novi.nl","password");
		user.addRole(adminRole);
		userRepository.save(user);

		//Act
		User actual = userRepository.findFirstByRolesContains(adminRole).orElse(null);

		//Assert
		assertNotNull(actual);
	}

	@Test
	void test_findByRole(){
		//Arrange
		userRepository.deleteAll(); //TODO Is needed both here and in individual tests??
		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
		assertNotNull(adminRole);
		User user = new User("admin","admin@novi.nl","password");
		user.addRole(adminRole);
		userRepository.save(user);

		//Act
		List<User> users = userRepository.findByRole(ERole.ROLE_ADMIN);
		int actual = users.size();
		users.forEach(u-> System.out.println(u.getUsername()));
		//Assert
		assertTrue(actual==1);
	}

	@Test
	void test_existsByRole(){
		//Arrange
		userRepository.deleteAll(); //TODO Is needed both here and in individual tests??
		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
		assertNotNull(adminRole);
		User user = new User("admin","admin@novi.nl","password");
		user.addRole(adminRole);
		userRepository.save(user);
		//Act
		boolean actual = userRepository.existsByRole(ERole.ROLE_ADMIN);
		boolean expected = true;
		//Assert
		assertEquals(actual,expected);
	}

}
