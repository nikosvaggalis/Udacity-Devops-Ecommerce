package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import com.example.demo.model.persistence.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	public static final Logger logger = LoggerFactory.getLogger("commons-log");

	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(request.getUsername());

		if(applicationUser==null) {
			logger.error("CART:User {} not found.Cannot add to Cart", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			logger.error("CART:Item {} not found.Cannot add to Cart",request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = applicationUser.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> {
						cart.addItem(item.get());
						logger.info("CART:Attempting to add item with id {} to Cart", i);
					});
		cartRepository.save(cart);
		logger.info("CART:All items successfully added from Cart");

		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(request.getUsername());

		if(applicationUser==null) {
			logger.error("CART:User {} not found.Cannot remove Cart", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			logger.error("CART:Item {} not found.Cannot remove from Cart",request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = applicationUser.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> { cart.removeItem(item.get());
						logger.info("CART:Attempting to remove item with id {} from cart", i);
		});
		cartRepository.save(cart);
		logger.info("CART:All items successfully removed from Cart");

		return ResponseEntity.ok(cart);
	}
		
}
