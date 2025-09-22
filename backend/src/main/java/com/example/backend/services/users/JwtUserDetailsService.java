package com.example.backend.services.users;

import com.example.backend.entities.AuthUserDetail;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private static GrantedAuthority getAuthority(String role) {
        return new SimpleGrantedAuthority(role);
    }

    public static List<GrantedAuthority> getAuthorities( String rolesAscommaSeparated ) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Arrays.asList(rolesAscommaSeparated.split(",")).forEach (
                role -> authorities.add(getAuthority(role))
        );
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findUserByFullName(username);
        return new AuthUserDetail(user.getId(), user.getNickName(),
                user.getPassword() , getAuthorities(user.getUserType()));
    }

    public UserDetails loadUserByEmail(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        return new AuthUserDetail(user.getId(), user.getNickName(),
                user.getPassword() , getAuthorities(user.getUserType()));
    }

    public UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow (
                () -> new ResourceNotFoundException("User id" + id + "does not exist")
      );
        return new AuthUserDetail(user.getId(), user.getNickName(),
                user.getPassword() , getAuthorities(user.getUserType()));
    }
}
