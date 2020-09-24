package com.example.demo.controllers;

import com.example.demo.model.persistence.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public static final Logger logger = LoggerFactory.getLogger("commons-log");



	@GetMapping("/id/{id}")
	public ResponseEntity<ApplicationUser> findById(@PathVariable Long id) {

		Optional<ApplicationUser> optionalApplicationUser = applicationUserRepository.findById(id);
		if ( !optionalApplicationUser.isPresent()) {
			logger.error("USER:Cannot find user by id {} ", id);
			return ResponseEntity.badRequest().build();
		}
		logger.info("USER:User {} found by id " , id);

		return ResponseEntity.ok(optionalApplicationUser.get());
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<ApplicationUser> findByUserName(@PathVariable String username) {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
		if ( applicationUser==null) {
			logger.error("USER:Cannot find user by username {} ", username);
			return ResponseEntity.badRequest().build();
		}
		logger.info("USER:User {} found by name " , username);

		return ResponseEntity.ok(applicationUser);

	}
	
	@PostMapping("/create")
	public ResponseEntity<ApplicationUser> createUser(@RequestBody CreateUserRequest createUserRequest) {
		ApplicationUser applicationUser = new ApplicationUser();
		applicationUser.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		applicationUser.setCart(cart);


		if (createUserRequest.getPassword().length()<6 ||!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			logger.error("USER:Cannot create user " + createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}

		applicationUser.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		applicationUserRepository.save(applicationUser);

		logger.info("USER:User {} created successfully " , createUserRequest.getUsername());

		return ResponseEntity.ok(applicationUser);


	}
	
}
