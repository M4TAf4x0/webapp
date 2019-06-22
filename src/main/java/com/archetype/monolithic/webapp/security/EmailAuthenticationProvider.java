package com.archetype.monolithic.webapp.security;

import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository  userRepository;

    public EmailAuthenticationProvider(PasswordEncoder passwordEncoder,
                                       UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository  = userRepository;
    }

    /**
     * Authenticate a user.
     *
     * @param auth the Authentication instance
     * @return the authentication token
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        final String username = auth.getName();
        final String password = auth.getCredentials().toString();

        return userRepository.findOneWithRolesByEmailIgnoreCase(username)
                .map(user -> generateTokenWithUsernameAndPassword(username, password, user))
                .orElseThrow(() -> new UsernameNotFoundException(
                                "User with email " + username + " was not found in the " + "database"));
    }

    /**
     * Generate a token for the authenticated user.
     *
     * @param username the username
     * @param password the password
     * @param user     the User entity
     * @return the authentication token
     */
    private UsernamePasswordAuthenticationToken generateTokenWithUsernameAndPassword(
            String username,
            String password,
            User user) {

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Authentication failed");
        }

        List<GrantedAuthority> grantedAuthorities = user.getRoles()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}