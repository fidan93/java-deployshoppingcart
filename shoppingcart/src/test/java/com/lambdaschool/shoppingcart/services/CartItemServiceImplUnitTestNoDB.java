package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplication.class,properties = {
    "command.line.runner.enabled=false"})
public class CartItemServiceImplUnitTestNoDB
{

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private CartItemService cartItemService;

    private List<User> userList = new ArrayList<>();
    private List<Product> prodList = new ArrayList<>();
    @Before
    public void setUp() throws Exception
    {
        //roles
        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        //user
        User u1 = new User("barnbarn", "LambdaLlama","barnbarn@host.local", " ");
        u1.setUserid(10);
        u1.getRoles().add(new UserRoles(u1,r1));

        User u2 = new User("cinnamon",
            "LambdaLlama",
            "cinnamon@host.local",
            "added via seed data");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.setUserid(2);

        //products
        Product p1 = new Product();
        p1.setProductid(1);
        p1.setName("Pen");
        p1.setDescription("Writes");
        p1.setComments("From Fidan");
        p1.setPrice(2.50);

        Product p2 = new Product();
        p2.setProductid(2);
        p2.setName("Pencil");
        p2.setDescription("Draws");
        p2.setComments("Seni cox sevirem");
        p2.setPrice(3.50);

        prodList.add(p1);
        prodList.add(p2);

        CartItem c1 = new CartItem(u1,p1,5,"Something");
        u1.getCarts().add(c1);

        userList.add(u1);
        userList.add(u2);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void addToCart()
    {
       CartItemId cartItemId= new CartItemId(1,1);
       CartItem cart3 = new CartItem();
       cart3.setUser(userList.get(0));
       cart3.setProduct(prodList.get(0));
       cart3.setComments("");
       cart3.setQuantity(2);

        Mockito.when(userRepository.findById(1L))
            .thenReturn(Optional.of(userList.get(0)));
        Mockito.when(productRepository.findById(1L))
            .thenReturn(Optional.of(prodList.get(0)));
        Mockito.when(cartItemRepository.findById(any(CartItemId.class))).thenReturn(Optional.of(cart3));

        assertEquals(3,cartItemService.addToCart(1L,"Hello").getQuantity());
    }

//    @Test
//    public void removeFromCart()
//    {
//        //User
//        User u1 = new User("barnbarn", "LambdaLlama","barnbarn@host.local", " ");
//        u1.setUserid(10);
//
//
//
//        CartItem c1 = new CartItem(u1,p1,5,"Something");
//
//        Mockito.when(userRepository.findByUsername("barnbarn")).thenReturn(u1);
//
//        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
//
//        Mockito.when(cartItemRepository.findById(new CartItemId(u1.getUserid(),
//            p1.getProductid()))).thenReturn(Optional.of(c1));
//
//        c1.setQuantity(5-1);
//        assertEquals(4,c1.getQuantity());
//    }
}