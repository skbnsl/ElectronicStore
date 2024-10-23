package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if(roleAdmin==null){
			Role role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_ADMIN");
			roleRepository.save(role1);
		}

		Role roleUser = roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if(roleUser==null){
			Role role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setName("ROLE_NORMAL");
			roleRepository.save(role2);
		}

		//creating a admin user
		User user1 = userRepository.findByEmail("sagar@gmail.com").orElse(null);
		if(user1==null){
			User user = new User();
			user.setEmail("sagar@gmail.com");
			user.setName("sagar");
			user.setPassword(passwordEncoder.encode("sagar"));
			user.setRoles(List.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());

			userRepository.save(user);
		}
	}
}
