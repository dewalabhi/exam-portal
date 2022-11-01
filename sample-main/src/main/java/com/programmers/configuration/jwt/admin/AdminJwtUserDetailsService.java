package com.programmers.configuration.jwt.admin;

import com.programmers.model.db.AdminUser;
import com.programmers.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AdminJwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        final Optional<AdminUser> byUserName = adminUserRepository.findByUserName(userName);
        if (byUserName.isPresent()) {
            final AdminUser adminUser = byUserName.get();
            return new User(adminUser.getUserName(), adminUser.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
