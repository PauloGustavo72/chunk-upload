package br.com.chunkuploadserver.config;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthentication implements Authentication {

	private static final long serialVersionUID = -295250221436044510L;
	private final UserDetails user;
    private boolean authenticated = true;
    private String token;

    public UserAuthentication(UserDetails user, String token) {
        this.user = user;
		this.token = token;
    }

    public UserAuthentication(UserDetails user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}
    
    public void setToken(String token) {
		this.token = token;
	}
    
    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public UserDetails getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}