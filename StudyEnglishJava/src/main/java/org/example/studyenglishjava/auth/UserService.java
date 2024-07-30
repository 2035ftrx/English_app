package org.example.studyenglishjava.auth;

import org.example.studyenglishjava.entity.User;
import org.example.studyenglishjava.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found."));
        return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), mapRoleToAuthority(user.getRole()));
    }

    private Collection<GrantedAuthority> mapRoleToAuthority(int role) {
        return Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(role)));
    }

}