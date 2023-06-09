package com.cognixia.jump.controller;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.User;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
// import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.cognixia.jump.model.User.Role.ROLE_USER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class )
public class UserControllerTest {
    private static final String STARTING_URI="http://localhost:8080/api";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService service;
    @InjectMocks
    private UserController Controller;
   /* @MockBean
    private JwtRequestFilter jwtRequestFilter;*/
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private MyUserDetailsService userDetailsService;
    @MockBean
    private PasswordEncoder encoder;
    @Test
    @WithMockUser(username = "testUser",roles = {"USER_ADMIN"})
    @AutoConfigureMockMvc(addFilters = false)
    void testGetStudents() throws Exception{
        String uri= STARTING_URI+"/user";

        List<User> allUsers = new ArrayList<User>();
        allUsers.add(new User (true, ROLE_USER,1,"josephE","testPW","jpeg.link"));
        allUsers.add(new User (true, ROLE_USER,2,"cliffF","testPW","jpeg.link"));

        when(service.getUsers()).thenReturn(allUsers);

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(allUsers.size()))
                .andExpect(jsonPath("$[0].id").value(allUsers.get(0).getId()))
                .andExpect(jsonPath("$[0].username").value(allUsers.get(0).getUsername()))
                .andExpect(jsonPath("$[0].password").value(allUsers.get(0).getPassword()))
                .andExpect(jsonPath("$[0].role").value(allUsers.get(0).getRole().toString()))
                .andExpect(jsonPath("$[0].enabled").value(allUsers.get(0).isEnabled()))
                .andExpect(jsonPath("$[0].profilePhoto").value(allUsers.get(0).getProfilePhoto()))
                .andExpect(jsonPath("$[1].id").value(allUsers.get(1).getId()))
                .andExpect(jsonPath("$[1].username").value(allUsers.get(1).getUsername()))
                .andExpect(jsonPath("$[1].password").value(allUsers.get(1).getPassword()))
                .andExpect(jsonPath("$[1].role").value(allUsers.get(1).getRole().toString()))
                .andExpect(jsonPath("$[1].enabled").value(allUsers.get(1).isEnabled()))
                .andExpect(jsonPath("$[1].profilePhoto").value(allUsers.get(1).getProfilePhoto()));

        verify(service,times(1)).getUsers();
        //verifyNoInteractions(service);
    }
    //getUser(id)
    @Test
    @WithMockUser(username = "testUser",roles = {"USER_ADMIN"})
    void testGetUserById() throws Exception{

        String uri= STARTING_URI + "/user/{id}";
        int id=1;

        User testUser= new User (true, ROLE_USER,1,"josephE","testPW","jpeg.link");

        when (service.getUserById(id)).thenReturn(testUser);

        mvc.perform(get(uri,id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.role").value(testUser.getRole().toString()))
                .andExpect(jsonPath("$.enabled").value(testUser.isEnabled()))
                .andExpect(jsonPath("$.profilePhoto").value(testUser.getProfilePhoto()));

        verify (service, times(1)).getUserById(id);
        //verifyNoInteractions(service );

    }
    @Test
    @WithMockUser(username = "testUser",roles = {"USER_ADMIN"})
    void testUserByIdNotFound() throws Exception{
        int id=1;
        String uri=STARTING_URI+"/student/{id}";

        when (service.getUserById(id))
                .thenThrow(new ResourceNotFoundException("Student",id));

        mvc.perform(get(uri,id))
                .andDo(print())
                .andExpect(status().isNotFound());

        /*verify(service,times(1)).getUserById(id);
        verifyNoInteractions(service);*/
    }
    //createUser(user)
//    @Test
//    @WithMockUser(username = "testUser",roles = {"USER_ADMIN"})
//    @AutoConfigureMockMvc(addFilters = false)
//    void testCreateUser() throws Exception{
//        String uri= STARTING_URI +"/user";
//        User testUser= new User (true, ROLE_USER,1,"josephE","testPW","jpeg.link");
//
//        when(service.createUser(Mockito.any(User.class))).thenReturn(testUser);
//
//        mvc.perform( post(uri)
//                .content(asJsonString(testUser))
//                .contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andDo(print())
//            .andExpect(status().isCreated())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//    }

    //deleteUser(user)
    void testDeleteUser() throws Exception{
        String uri=STARTING_URI+ "/user/{id}";
        int id=1;

        User testUser=new User (true, ROLE_USER,1,"josephE","testPW","jpeg.link");

        when(service.deleteUser(id)).thenReturn(true);

        mvc.perform(delete(uri,id))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service, times(1)).deleteUser(id);
        verifyNoMoreInteractions(service);
    }

    public static String asJsonString(final Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
