package br.com.chunkuploadserver.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.chunkuploadserver.user.model.User;
import br.com.chunkuploadserver.user.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User save(User user) {
		return userRepository.save(user);
	}

	public User findById(Long id) {
		return userRepository.getOne(id);
	}

}
