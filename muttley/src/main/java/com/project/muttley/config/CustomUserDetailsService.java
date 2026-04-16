package com.project.muttley.config;

import com.project.muttley.model.User;
import com.project.muttley.model.enums.UserRole;
import com.project.muttley.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com email: " + email);
        }

        String authority = normalizeRole(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(authority)
        );
    }

    private String normalizeRole(UserRole role) {
        if (role == null) {
            return "ROLE_STAFF";
        }

        if (role == UserRole.ADMIN) {
            return "ROLE_ADMIN";
        }

        return "ROLE_STAFF";
    }
}