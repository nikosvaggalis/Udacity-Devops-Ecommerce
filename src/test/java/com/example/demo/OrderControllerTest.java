package com.example.demo;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private ApplicationUserRepository userRepository = mock(ApplicationUserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"applicationUserRepository", userRepository);
        TestUtils.injectObjects(orderController,"orderRepository", orderRepository);
    }

    @Test
    public void submit() {

        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));

        ApplicationUser user = new ApplicationUser();
        user.setUsername("user1");
        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);

        when(userRepository.findByUsername("user1")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("user1");
        UserOrder userOrder = response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(BigDecimal.valueOf(1200),item.getPrice());

    }

    @Test
    public void findOrdersbyUser() {

        ApplicationUser user = new ApplicationUser();
        user.setUsername("user1");

        Cart cart = new Cart();
        cart.setApplicationUser(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("lenovo laptop");
        item2.setDescription("black");
        item2.setPrice(BigDecimal.valueOf(1800));

        cart.addItem(item);
        cart.addItem(item2);
        user.setCart(cart);

        UserOrder userOrder = UserOrder.createFromCart(cart);
        List<UserOrder> usersOrders = new ArrayList<>();
        usersOrders.add(userOrder);

        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(orderRepository.findByApplicationUser(user)).thenReturn(usersOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user1");
        List<UserOrder> returnedOrders = response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(3000),returnedOrders.get(0).getTotal());

    }
}
