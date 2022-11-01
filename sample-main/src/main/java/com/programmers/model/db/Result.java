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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Result {

    @JsonIgnore
    @UpdateTimestamp
    public Date timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalMarks;
    private int obtainedMarks;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToOne(fetch = FetchType.EAGER)
    private Exam exam;
    @OneToMany
    @JoinColumn(name = "result_id")
    private Set<ResultDetail> resultDetailSet;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", totalMarks=" + totalMarks
                + ", obtainedMarks=" + obtainedMarks
                + ", user=" + user
                + ", exam=" + exam
                + ", resultDetailSet=" + resultDetailSet
                + ", timestamp=" + timestamp
                + '}';
    }
}
