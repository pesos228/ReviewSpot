package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.repository.ClientRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;


public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    public UserDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var client = clientRepository.findByEmail(username);
        if (client == null){
            throw new UsernameNotFoundException("Client with email:" + username + " not found");
        }
        return new CustomUser(client.getEmail(), client.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_" + client.getRole().getName())),
                client.getPhotoUrl(), client.getId(), client.getName());
    }

    public static class CustomUser extends User{

        private final String photoUrl;
        private final int id;
        private final String name;

        public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String photoUrl, int id, String name) {
            super(username, password, authorities);
            this.photoUrl = photoUrl;
            this.id = id;
            this.name = name;
        }

        public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String photoUrl, int id, String name) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
            this.photoUrl = photoUrl;
            this.id = id;
            this.name = name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
