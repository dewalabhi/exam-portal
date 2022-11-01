package com.programmers.configuration.jwt.portal;

import com.programmers.model.db.User;
import com.programmers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service class of Users.
 */
@Service
public class PortalJwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        if (userRepository.findByEmailId(userName) != null) {
            final User user = userRepository.findByEmailId(userName);
            return new org.springframework.security.core.userdetails.User(user.getEmailId(), "", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
