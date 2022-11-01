package com.programmers.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

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
public class Section {

    @UpdateTimestamp
    public Date timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private int marksPerQuestion;
    private Boolean isActive;

    @Override
    public String toString() {
        return "Section{"
                + "id=" + id
                + ", marksPerQuestion=" + marksPerQuestion
                + ", isActive=" + isActive
                + ", timestamp=" + timestamp
                + '}';
    }
}
