package br.com.chunkuploadserver.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.chunkuploadserver.user.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void shoulCreateNewUser() {
		User user = new User(1L, "matheus", "matheus");
		User savedUser = userService.save(user);
		
		assertNotNull(savedUser);
		assertEquals(new Long(1), savedUser.getId());
		assertEquals("matheus", savedUser.getUsername());
	}

}
