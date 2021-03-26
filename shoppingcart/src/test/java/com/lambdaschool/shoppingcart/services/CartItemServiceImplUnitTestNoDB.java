package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplicationTest.class,
    properties = {"command.line.runner.enabled=false"})
public class CartItemServiceImplUnitTestNoDB
{

    @Autowired
    private CartItemService cartItemService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    private List<User> userList = new ArrayList<>();
    private List<Product> prodList = new ArrayList<>();
    @Before
    public void setUp() throws Exception
    {
        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u1 = new User("barnbarn",
            "LambdaLlama",
            "barnbarn@host.local",
            "added via seed data");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.setUserid(1);

        User u2 = new User("cinnamon",
            "LambdaLlama",
            "cinnamon@host.local",
            "added via seed data");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.setUserid(2);

        User u3 = new User("stumps",
            "LambdaLlama",
            "stumps@host.local",
            "added via seed data");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.setUserid(3);

        // Adding Products
        Product p1 = new Product();
        p1.setProductid(1);
        p1.setName("PEN");
        p1.setDescription("MAKES WORDS");
        p1.setPrice(2.50);
        p1.setComments("added via seed data");

        Product p2 = new Product();
        p1.setProductid(2);
        p2.setName("PENCIL");
        p2.setDescription("DOES MATH");
        p2.setPrice(1.50);
        p2.setComments("added via seed data");

        Product p3 = new Product();
        p1.setProductid(3);
        p3.setName("COFFEE");
        p3.setDescription("EVERYONE NEEDS COFFEE");
        p3.setPrice(4.00);
        p3.setComments("added via seed data");

        prodList.add(p1);
        prodList.add(p2);
        prodList.add(p3);

        CartItem cart1 = new CartItem(u1,p1,3,"sale");
        u1.getCarts().add(cart1);
        CartItem cart2 = new CartItem(u1,p2,2,"sale");
        u1.getCarts().add(cart2);
        CartItem cart3 = new CartItem(u1,p3,4,"sale");
        u1.getCarts().add(cart3);

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void addToCart()
    {
        CartItemId cartItemId = new CartItemId(1,1);
        CartItem cart3 = new CartItem(userList.get(0),
            prodList.get(0),3,"hello");


        Mockito.when(userRepository.findById(1L))
            .thenReturn(Optional.of(userList.get(0)));
        Mockito.when(productRepository.findById(1L))
            .thenReturn(Optional.of(prodList.get(0)));
        Mockito.when(cartItemRepository.findById(any(CartItemId.class)))
            .thenReturn(Optional.of(cart3));

        Mockito.when(cartItemRepository.save(any(CartItem.class)))
            .thenReturn(cart3);

        assertEquals(4,cartItemService.addToCart(1L,1L,"hi").getQuantity());
    }

    @Test
    public void removeFromCart()
    {
        CartItemId cartItemId = new CartItemId(1,1);
        CartItem cart3 = new CartItem(userList.get(0),
            prodList.get(0),3,"hello");

        Mockito.when(userRepository.findById(1L))
            .thenReturn(Optional.of(userList.get(0)));
        Mockito.when(productRepository.findById(1L))
            .thenReturn(Optional.of(prodList.get(0)));
        Mockito.when(cartItemRepository.findById(any(CartItemId.class)))
            .thenReturn(Optional.of(cart3));
        Mockito.when(cartItemRepository.save(any(CartItem.class)))
            .thenReturn(cart3);

        assertEquals(2,cartItemService.removeFromCart(1L,1L,"bye").getQuantity());
    }

    @Test
    public void emptyCart()
    {
        CartItemId cartItemId = new CartItemId(1,1);
        CartItem cart3 = new CartItem(userList.get(0),
            prodList.get(0),1,"hello");

        Mockito.when(userRepository.findById(1L))
            .thenReturn(Optional.of(userList.get(0)));
        Mockito.when(productRepository.findById(1L))
            .thenReturn(Optional.of(prodList.get(0)));
        Mockito.when(cartItemRepository.findById(any(CartItemId.class)))
            .thenReturn(Optional.of(cart3));
        Mockito.when(cartItemRepository.save(any(CartItem.class)))
            .thenReturn(null);

        assertNull(cartItemService.removeFromCart(1L,1L,"bye"));

    }
}