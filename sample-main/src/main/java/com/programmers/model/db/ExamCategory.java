package com.programmers.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExamCategory {

    @JsonIgnore
    public Date timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String examCategoryName;
    private String createdBy;
    private Boolean isActive = true;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", examCategoryName='" + examCategoryName + '\''
                + ", isActive=" + isActive
                + ", timestamp=" + timestamp
                + '}';
    }
}
