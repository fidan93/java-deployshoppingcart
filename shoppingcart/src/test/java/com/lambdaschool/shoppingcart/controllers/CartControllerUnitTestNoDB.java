package com.lambdaschool.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@WithMockUser(username = "admin",roles={"USER","ADMIN"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = ShoppingCartApplication.class)
@AutoConfigureMockMvc
public class CartControllerUnitTestNoDB
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private UserService userService;
    User u1;
    Product p1;
    CartItem c1;
    @Before
    public void setUp() throws Exception
    {
      u1 = new User("barnbarn", "LambdaLlama","barnbarn@host.local", " ");
        u1.setUserid(10);
         p1 = new Product();
        p1.setProductid(1);
        c1 = new CartItem(u1,p1,5,"Something");

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listCartItemsByUserId() throws Exception
    {
        String apiUrl = "/carts/user/10";

        Mockito.when(userService.findUserById(10))
            .thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
            .andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(u1);

        assertEquals(er,tr);
    }

    @Test
    public void addToCart() throws Exception
    {
        String url = "/carts/add/user/product/1";

        ObjectMapper mapper= new ObjectMapper();
        String tr = mapper.writeValueAsString(c1);

        Mockito.when(cartItemService.addToCart(1,"Hello"))
            .thenReturn(c1);

        RequestBuilder rb = MockMvcRequestBuilders.put(url)
            .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(rb)
            .andExpect(status().isOk());

    }
}