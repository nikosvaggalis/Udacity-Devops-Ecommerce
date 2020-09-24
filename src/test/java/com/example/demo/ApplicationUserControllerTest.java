package com.example.demo;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationUserControllerTest {
    private UserController userController;

    private ApplicationUserRepository userRepository = mock(ApplicationUserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "applicationUserRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);


    }

    @Test
    public void createUser() throws Exception {

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user1");
        r.setPassword("mypass");
        r.setConfirmPassword("mypass");

        when(encoder.encode("mypass")).thenReturn("mypasshashed");

        ResponseEntity<ApplicationUser> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        ApplicationUser user = response.getBody();
        assertEquals(0, user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("mypasshashed", user.getPassword());


    }

    @Test
    public void findUserByUsername() {

        ApplicationUser user = new ApplicationUser();
        long id = 1L;
        user.setUsername("user1");
        user.setPassword("mypass");
        user.setId(id);

        when(userRepository.findByUsername("user1")).thenReturn(user);
        ResponseEntity<ApplicationUser> responseEntity = userController.findByUserName("user1");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        user = responseEntity.getBody();
        assertEquals(id, user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("mypass", user.getPassword());
    }

    @Test
    public void findUserById() {

        ApplicationUser user = new ApplicationUser();
        long id = 1L;
        user.setUsername("user1");
        user.setPassword("mypass");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        ResponseEntity<ApplicationUser> responseEntity = userController.findById(id);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        user = responseEntity.getBody();
        assertEquals(id, user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("mypass", user.getPassword());

    }



}
