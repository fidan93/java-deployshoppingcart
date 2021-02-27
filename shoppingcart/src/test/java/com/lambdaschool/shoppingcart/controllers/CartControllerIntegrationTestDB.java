package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = ShoppingCartApplication.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "barnbarn")
public class CartControllerIntegrationTestDB
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        List<User> userList = userService.findAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listCartItemsByUserId()
    {
        given().when()
            .get("/carts/user/1")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("barnbarn"));
    }

    @Test
    public void addToCart()
    {
        given().when()
            .put("/carts/add/user/product/1")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("barnbarn"));

    }
}