package com.example.demo.model.persistence.repositories;

import java.util.List;

import com.example.demo.model.persistence.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.UserOrder;
import org.springframework.lang.Nullable;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	@Nullable
	List<UserOrder> findByApplicationUser(ApplicationUser applicationUser);
}
