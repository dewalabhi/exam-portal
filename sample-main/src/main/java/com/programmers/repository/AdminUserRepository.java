package com.programmers.repository;

import com.programmers.model.db.AdminUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminUserRepository extends CrudRepository<AdminUser, UUID> {
    Optional<AdminUser> findByPassword(String password);

    List<AdminUser> findAll();

    Optional<AdminUser> findByUserName(String userName);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<AdminUser> findByEmailId(String email);
}
