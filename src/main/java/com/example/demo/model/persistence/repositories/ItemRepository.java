package com.example.demo.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Item;
import org.springframework.lang.Nullable;

public interface ItemRepository extends JpaRepository<Item, Long> {
	@Nullable
	public List<Item> findByName(String name);

}
