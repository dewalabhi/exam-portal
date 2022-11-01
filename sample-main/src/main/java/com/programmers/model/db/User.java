package com.programmers.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Set;

@Entity(name = "userinfo")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @JsonIgnore
    @UpdateTimestamp
    public Date timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email_id", unique = true, nullable = false)
    private String emailId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    private String gender;

    private int age;

    private String college;

    private String skills;

    @Transient
    private Set<String> skillList;

    private String degree;

    private int graduationYear;

    @Column(unique = true)
    private String mobileNumber;

    private String country;

    private String state;

    private String city;

    private double graduationPercentage;

    private Boolean isActive = false;

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", emailId='" + emailId + '\''
                + ", firstName='" + firstName + '\''
                + ", middleName='" + middleName + '\''
                + ", lastName='" + lastName + '\''
                + ", gender='" + gender + '\''
                + ", age=" + age
                + ", college='" + college + '\''
                + ", skills='" + skills + '\''
                + ", skillList=" + skillList
                + ", degree='" + degree + '\''
                + ", graduationYear=" + graduationYear
                + ", mobileNumber='" + mobileNumber + '\''
                + ", country='" + country + '\''
                + ", state='" + state + '\''
                + ", city='" + city + '\''
                + ", graduationPercentage=" + graduationPercentage
                + ", isActive=" + isActive
                + '}';
    }
}