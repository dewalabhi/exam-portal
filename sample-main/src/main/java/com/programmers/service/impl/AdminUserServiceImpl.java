package com.programmers.service.impl;

import com.programmers.common.EncryptDecrypt;
import com.programmers.globalexception.EntityAlreadyExistsException;
import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.db.AdminUser;
import com.programmers.repository.AdminUserRepository;
import com.programmers.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.programmers.common.Constant.ALREADY_EXISTS;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminUser createAdminUser(AdminUser adminUser) {
        validateUser(adminUser, false);
        if (findUserByName(adminUser.getUserName())) {
            throw new EntityAlreadyExistsException("Username " + adminUser.getUserName().trim() + ALREADY_EXISTS);
        }

        if (adminUserRepository.existsByMobileNumber(adminUser.getMobileNumber())) {
            throw new InvalidEntityException("Mobile Number " + adminUser.getMobileNumber().trim() + ALREADY_EXISTS);
        }

        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            adminUser.setCreatedBy(((UserDetails) principal).getUsername());
        } else {
            adminUser.setCreatedBy(principal.toString());
        }
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        return adminUserRepository.save(adminUser);
    }

    @Override
    public AdminUser updateAdminUser(String uid, AdminUser adminUser) {

        final Optional<AdminUser> findAdminUser = adminUserRepository.findById(UUID.fromString(uid));
        if (!findAdminUser.isPresent()) {
            throw new EntityNotFoundException("User name not found - " + uid);
        } else {
            final String password = findAdminUser.get().getPassword();
            final boolean isPasswordUpdate = password.equals(adminUser.getPassword());

            adminUser.setUserId(UUID.fromString(uid));
            validateUser(adminUser, isPasswordUpdate);

            final String userName = adminUser.getUserName();
            final String existingUserName = findAdminUser.get().getUserName();

            if (!userName.equals(existingUserName) && findUserByName(userName)) {
                throw new EntityAlreadyExistsException(
                        "There is an user name with " + adminUser.getUserName().trim() + ALREADY_EXISTS);
            }

            if (!adminUser.getMobileNumber().equals(findAdminUser.get().getMobileNumber())
                    && adminUserRepository.existsByMobileNumber(adminUser.getMobileNumber())) {
                throw new InvalidEntityException("Mobile Number already exists");
            }

            adminUser.setUserId(UUID.fromString(uid));
            adminUser.setPassword(EncryptDecrypt.encrypt(adminUser.getPassword()));
            final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                adminUser.setUpdatedBy(((UserDetails) principal).getUsername());
            } else {
                adminUser.setUpdatedBy(principal.toString());
            }
            adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
            return adminUserRepository.save(adminUser);
        }
    }

    @Override
    public List<AdminUser> getAllAdminUser() {
        return adminUserRepository.findAll();
    }

    @Override
    public boolean findUserByName(String userName) {
        final Optional<AdminUser> findAdminUser = adminUserRepository.findByUserName(userName);
        return findAdminUser.isPresent();
    }

    public void validateUser(AdminUser adminUser, boolean isPasswordUpdate) {
        final List<String> errorMessage = new ArrayList<>();

        final String lastName = (adminUser.getLastName() == null) ? "" : adminUser.getLastName().trim();
        final String firstName = (adminUser.getFirstName() == null) ? "" : adminUser.getFirstName().trim();
        final String password = (adminUser.getPassword() == null) ? "" : adminUser.getPassword().trim();
        final String role = (adminUser.getRole() == null) ? "" : adminUser.getRole().trim();
        final String email = (adminUser.getEmailId() == null) ? "" : adminUser.getEmailId().trim();
        final String mobileNumber = (adminUser.getMobileNumber() == null) ? "" : adminUser.getMobileNumber().trim();

        if (firstName.equalsIgnoreCase("") || !firstName.matches("^[a-zA-Z]*$")) {
            errorMessage.add("First name required");
        }
        if (!lastName.matches("^[a-zA-Z]*$")) {
            errorMessage.add("Last name required");
        }
        if (!mobileNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
            errorMessage.add("Mobile number must be 10 digit [XXX-XXX-XXXX]");
        }
        if (!role.matches("^[a-zA-Z0-9]+$")) {
            errorMessage.add("User role required");
        }
        if (!isPasswordUpdate && (password.equalsIgnoreCase("") || !isValidPassword(password))) {
            errorMessage.add("Password length must be at least 6 characters and at most 8 characters");
        }
        final boolean validateEmail = userService.validateEmail(email);
        if (!validateEmail) {
            errorMessage.add("Email required or invalid");
        }
        final Optional<AdminUser> userByEmailId = adminUserRepository.findByEmailId(email);

        if (userByEmailId.isPresent()) {
            final boolean userMatch = userByEmailId.get().getUserId().equals(adminUser.getUserId());
            if (!userMatch) {
                errorMessage.add("Email already exists");
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid User detail - " + errorMessage);
        }
    }

    private boolean isValidPassword(String password) {
        /*
         * ^ represents starting character of the string. (?=.*[0-9]) represents a digit
         * must occur at least once. (?=.*[a-z]) represents a lower case alphabet must
         * occur at least once. (?=.*[A-Z]) represents an upper case alphabet that must
         * occur at least once. (?=.*[@#$%^&-+=()] represents a special character that
         * must occur at least once. (?=\\S+$) white spaces don’t allowed in the entire
         * string. .{6, 8} represents at least 6 characters and at most 8 characters. $
         * represents the end of the string.
         */

        /*
         * It contains at least 6 characters and at most 8 characters. It contains at
         * least one digit. It contains at least one upper case alphabet. It contains at
         * least one lower case alphabet. It contains at least one special character
         * which includes !@#$%&*()-+=^. It doesn't contain any white space. -
         * Hitesh@portal20 - true - HiteshSoni - false
         */
        final String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+).{6,8}$";

        final Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        final Matcher m = p.matcher(password);
        return m.matches();
    }
}
