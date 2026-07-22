package com.sprint.SprintLite.security;

import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.UsersRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SprintLiteUserNamePwdAuthenticationProvider implements AuthenticationProvider {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = Objects.requireNonNull(authentication.getCredentials()).toString();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User details not found for the user : " + username
                ));
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(users.getRole().toString()));
        if (passwordEncoder.matches(pwd, users.getPasswordhash())){
            return new UsernamePasswordAuthenticationToken(users, null, authorities);
        }
        else{
            throw new BadCredentialsException("Invalid Password");
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
