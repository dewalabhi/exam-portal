package com.programmers.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Options {
    @JsonIgnore
    @UpdateTimestamp
    private Date timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String optionText;
    private Boolean answer;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", optionText='" + optionText + '\''
                + ", answer=" + answer
                + ", timestamp=" + timestamp
                + '}';
    }
}
