package com.programmers.model.db;

import com.programmers.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminUser extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID userId = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String userName;

    private String password;

    private String role;

    @Column(unique = true, nullable = false)
    private String emailId;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

}
