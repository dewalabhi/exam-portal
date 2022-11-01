package com.programmers.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuestionHistory {

    @JsonIgnore
    @UpdateTimestamp
    public Date timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private AdminUser adminUser;
    private byte[] excelData;
    private String createdBy;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", adminUser=" + adminUser
                + ", excelData=" + Arrays.toString(excelData)
                + ", createdBy='" + createdBy + '\''
                + ", timestamp=" + timestamp
                + '}';
    }
}
