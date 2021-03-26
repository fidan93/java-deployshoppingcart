package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.models.UserMinimum;
import com.lambdaschool.shoppingcart.models.UserRoles;
import com.lambdaschool.shoppingcart.services.RoleService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class OpenController
{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/register",consumes = "application/json",produces="application/json")
    public ResponseEntity<?> createNewUser(HttpServletRequest httpServletRequest, @RequestBody
                                           UserMinimum newuser)
    {
        User user = new User();
        user.setUsername(newuser.getUsername());
        user.setPassword(newuser.getPassword());
        user.setPrimaryemail(newuser.getPrimaryemail());

        Set<UserRoles> newRoles = new HashSet<>();
        newRoles.add(new UserRoles(user,roleService.findByName("USER")));

        user = userService.save(user);

        HttpHeaders responceHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString(httpServletRequest.getServerName()+":"+httpServletRequest.getLocalPort()+"/users/user/{userid}")
            .buildAndExpand(user.getUserid()).toUri();
        responceHeaders.setLocation(newUserURI);

        //login
        RestTemplate restTemplate = new RestTemplate();
        String requestURI = "http://"+httpServletRequest.getServerName()+":"+httpServletRequest.getLocalPort()+"/login";

        List<MediaType> acceptableMediaType = new ArrayList<>();
        acceptableMediaType.add(MediaType.APPLICATION_JSON);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(acceptableMediaType);
        headers.setBasicAuth(System.getenv("OAUTHCLIENTID"),
            System.getenv("OAUTHCLIENTSECRET"));

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","password");
        map.add("scope","read write trust");
        map.add("username",newuser.getUsername());
        map.add("password",newuser.getPassword());

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map,headers);

        String theToken = restTemplate.postForObject(requestURI,request,String.class);

        return new ResponseEntity<>(theToken,responceHeaders,
            HttpStatus.CREATED);
    }
}
