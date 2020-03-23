package com.patternpedia.auth.security;

import com.patternpedia.auth.user.UserEntity;
import com.patternpedia.auth.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByMail(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        log.info("{} has role of {}", username, authority);
        return new org.springframework.security.core.userdetails.User(user.getMail(), user.getPassword(), Arrays.asList(authority));
    }


}
