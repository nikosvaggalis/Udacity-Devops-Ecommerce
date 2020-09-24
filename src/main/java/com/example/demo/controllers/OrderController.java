package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.persistence.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	public static final Logger logger = LoggerFactory.getLogger("commons-log");

	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);

		if(applicationUser==null) {
			logger.error("ORDER:User {} not found.Cannot submit Order", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(applicationUser.getCart());
		orderRepository.save(order);
		logger.info("ORDER:Order for user {} successfully entered", username);

		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);

		if(applicationUser==null) {
			logger.error("ORDER:User {} not found. Cannot retrieve Order history", username);
			return ResponseEntity.notFound().build();
		}

		List<UserOrder> userOrders = orderRepository.findByApplicationUser(applicationUser);
		if (userOrders==null) {
			logger.error("ORDER:User {} found but has no orders", username);
			return ResponseEntity.notFound().build();
		}

		logger.info("ORDER:Order history for user {} successfully retrieved", username);
		return ResponseEntity.ok(userOrders);
	}
}
