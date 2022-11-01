package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.AdminUser;
import com.programmers.repository.AdminUserRepository;
import com.programmers.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.programmers.common.RequestMappingConstant.CREATE_ADMIN_USER;
import static com.programmers.common.RequestMappingConstant.GET_ADMIN_USER;
import static com.programmers.common.RequestMappingConstant.GET_ALL_ADMIN_USER;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;
import static com.programmers.common.RequestMappingConstant.UPDATE_ADMIN_USER;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class AdminUserController {

    private final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @PostMapping(value = CREATE_ADMIN_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> createAdminUser(@RequestBody AdminUser adminUser) {
        adminUserService.createAdminUser(adminUser);
        return new JsonRest<>(true, new HashMap<>(), "Admin created successfully", "", HttpStatus.CREATED);
    }

    @PutMapping(value = UPDATE_ADMIN_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> updateAdminUser(@PathVariable(value = "userID") String userId, @RequestBody AdminUser adminUser) {
        adminUserService.updateAdminUser(userId, adminUser);
        return new JsonRest<>(true, new HashMap<>(), "Admin updated successfully", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_ADMIN_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<AdminUser>> getAllAdminList() {
        logger.debug("get all admin list");
        final List<AdminUser> adminUserList = adminUserService.getAllAdminUser();

        if (adminUserList.isEmpty()) {
            logger.debug("get all admin list - " + true);
            final HashMap<String, List<AdminUser>> response = new HashMap<>();
            response.put("AdminList", adminUserList);
            return new JsonRest<>(true, response, "Admin user list empty", "", HttpStatus.OK);
        }

        final String loginUserName;
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            loginUserName = ((UserDetails) principal).getUsername();
        } else {
            loginUserName = principal.toString();
        }

        final List<AdminUser> finalAdminUserList = adminUserList.stream().filter(x -> !x.getUserName().equals(loginUserName))
                .collect(Collectors.toList());
        logger.debug("admin list - {}", finalAdminUserList);
        final HashMap<String, List<AdminUser>> response = new HashMap<>();
        response.put("AdminList", finalAdminUserList);
        return new JsonRest<>(true, response, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ADMIN_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, AdminUser> getAdminById(@PathVariable(value = "userName") String userName) {
        final Optional<AdminUser> getAdmin = adminUserRepository.findByUserName(userName);

        logger.debug("find admin with userName {} found - {}", userName, getAdmin.isPresent());
        final Map<String, AdminUser> adminMap = new HashMap<>();
        getAdmin.ifPresent(adminUser -> adminMap.put("Admin", adminUser));
        return getAdmin.map(adminUser -> new JsonRest<>(true, adminMap, "", "", HttpStatus.OK))
                .orElseGet(() -> new JsonRest<>(true, new HashMap<>(), "Admin not found with - " + userName, "",
                        HttpStatus.NOT_FOUND));
    }
}