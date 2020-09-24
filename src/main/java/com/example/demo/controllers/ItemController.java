package com.example.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

import javax.swing.text.html.Option;

import static java.nio.charset.StandardCharsets.UTF_8;


@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	public static final Logger logger = LoggerFactory.getLogger("commons-log");


	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		List<Item> items = itemRepository.findAll();
		if (items == null || items.isEmpty()) {
			logger.error("ITEM:Cannot find items ");
			return ResponseEntity.notFound().build();
		}

		logger.info("ITEM:All items were retrieved successfully");
		return ResponseEntity.ok(items);
	}


	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Optional<Item> optionalItem = itemRepository.findById(id);
		if (!optionalItem.isPresent()) {
			logger.error("ITEM:Cannot find item by id {} ", id);
			return ResponseEntity.notFound().build();
		}

		logger.info("ITEM:Item with id {} was retrieved successfully",id);

		return ResponseEntity.ok(optionalItem.get());
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		String result;

		try {
			result = java.net.URLDecoder.decode(name, "UTF8");
		}
		catch (UnsupportedEncodingException ex) {
			return ResponseEntity.badRequest().build();
		}

		List<Item> items = itemRepository.findByName(result);
		if (items == null || items.isEmpty()) {
			logger.error("ITEM:Cannot find item by name {} ", result);
			return ResponseEntity.notFound().build();
		}
		logger.info("ITEM:Items with name {} were retrieved successfully",result);

		return ResponseEntity.ok(items);
	}
}
