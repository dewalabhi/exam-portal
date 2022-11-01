package com.programmers.service;

import com.programmers.model.db.AdminUser;
import java.util.List;

public interface AdminUserService {

    AdminUser createAdminUser(AdminUser adminUser);

    AdminUser updateAdminUser(String uid, AdminUser adminUser);

    List<AdminUser> getAllAdminUser();

    boolean findUserByName(String userName);
}
