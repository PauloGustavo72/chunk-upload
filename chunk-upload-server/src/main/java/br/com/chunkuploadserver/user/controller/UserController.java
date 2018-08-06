package br.com.chunkuploadserver.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.chunkuploadserver.user.model.User;
import br.com.chunkuploadserver.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/api/newAccount")
	public ResponseEntity<User> newAccount(@RequestBody User user) {
		user = userService.save(user);
		return ResponseEntity.ok().body(user);
	}
}
