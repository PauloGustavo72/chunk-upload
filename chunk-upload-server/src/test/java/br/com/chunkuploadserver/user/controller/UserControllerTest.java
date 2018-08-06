package br.com.chunkuploadserver.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.chunkuploadserver.user.model.User;
import br.com.chunkuploadserver.user.service.UserService;

@RunWith(SpringRunner.class)
public class UserControllerTest {

	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserController userController;

	private MockMvc mvc;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		when(userService.save(any(User.class)))
			.thenReturn(any(User.class));
		
        this.mvc = MockMvcBuilders.standaloneSetup(userController).build();
	}
	
	@Test
	public void shouldReturnOk() throws Exception {
		User user = new User(1L, "matheus", "matheus");
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/newAccount")
				.accept(MediaType.APPLICATION_JSON).content(json)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(requestBuilder)
			.andExpect(status().isOk());
	}

}
