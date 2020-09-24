package com.example.demo;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private ApplicationUserRepository userRepository = mock(ApplicationUserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "applicationUserRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }


    @Test
    public void add(){
        ModifyCartRequest m = new ModifyCartRequest();
        m.setUsername("user1");
        m.setItemId(1);
        m.setQuantity(1);

        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.setUsername("user1");
        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addToCart(m);
        cart = response.getBody();
        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertEquals("dell laptop",cart.getItems().get(0).getName());
        Assert.assertEquals(BigDecimal.valueOf(1200),cart.getTotal());
    }


    @Test
    public void remove(){
        ModifyCartRequest m = new ModifyCartRequest();
        m.setUsername("user1");
        m.setItemId(1);
        m.setQuantity(1);

        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.setUsername("user1");
        Cart cartObj = new Cart();
        user.setCart(cartObj);

        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));


        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<Cart> response = cartController.removeFromCart(m);
        Cart cart = response.getBody();
        Assert.assertEquals(200,response.getStatusCodeValue());

    }
}
