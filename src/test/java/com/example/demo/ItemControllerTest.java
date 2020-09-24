package com.example.demo;
import com.example.demo.controllers.ItemController;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);
    }



    @Test
    public void findById(){

        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);

        item = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals("dell laptop", item.getName());
        Assert.assertEquals(BigDecimal.valueOf(1200),item.getPrice());

    }

    @Test
    public void findByName(){

        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        when(itemRepository.findByName("Play Station 5")).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Play Station 5");
        List<Item> items = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(1, items.size());
        Assert.assertEquals("dell laptop", items.get(0).getName());

    }

    @Test
    public void findAll(){
        Item item = new Item();
        item.setId(1L);
        item.setName("dell laptop");
        item.setDescription("super mini");
        item.setPrice(BigDecimal.valueOf(1200));

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> items = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(1, items.size());
        Assert.assertEquals("dell laptop", items.get(0).getName());

    }

}
