package br.com.chunkuploadserver.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.chunkuploadserver.user.model.User;
import br.com.chunkuploadserver.user.repository.UserRepository;

@Service
public class CustomSpringSecurityUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()) 
            throw new UsernameNotFoundException("user name not found");
        
        return user.get();
    }
}