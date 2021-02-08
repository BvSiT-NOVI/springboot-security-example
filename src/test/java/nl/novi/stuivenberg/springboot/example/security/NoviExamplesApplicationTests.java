package nl.novi.stuivenberg.springboot.example.security;

import nl.novi.stuivenberg.springboot.example.security.domain.ERole;
import nl.novi.stuivenberg.springboot.example.security.domain.Role;
import nl.novi.stuivenberg.springboot.example.security.domain.User;
import nl.novi.stuivenberg.springboot.example.security.repository.RoleRepository;
import nl.novi.stuivenberg.springboot.example.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoviExamplesApplicationTests {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testSet(){
		//Arrange
		Set<String> roles = new HashSet<>();

		//Act
		//roles.add("emp");
		//roles.add("admin");

		String expected = "";
		String actual = String.join("",roles);
		//Assert
		assertTrue(expected.equalsIgnoreCase(actual));
	}

	@Test
	void testFindWithRole(){
		//Arrange
		Role role = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
		assertNotNull(role);

		//Act
		User actual = userRepository.findFirstByRolesContains(role).orElse(null);

		//Assert
		assertNotNull(actual);
	}

	@Test
	void testFindWithRole2(){
		//Arrange

		//Act
		List<User> users = userRepository.findByRole(ERole.ROLE_ADMIN);
		int actual = users.size();
		users.forEach(u-> System.out.println(u.getUsername()));
		//Assert
		assertTrue(actual==0);
	}

	@Test
	void testFindWithRole3(){
		//Arrange

		//Act
		boolean actual = userRepository.existsByRole(ERole.ROLE_ADMIN);
		boolean expected = true;
		//Assert
		assertEquals(actual,expected);
	}

}
