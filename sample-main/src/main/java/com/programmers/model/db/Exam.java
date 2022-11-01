package com.programmers.model.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Exam {

    @JsonIgnore
    @UpdateTimestamp
    public Date timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String examName;

    @Column(unique = true)
    private String password;

    private int examDuration;

    @ManyToOne
    private ExamCategory examCategory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "exam_id")
    private Set<ExamDetail> examDetailSet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endDateTime;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", examName='" + examName + '\''
                + ", password='" + password + '\''
                + ", examDuration=" + examDuration
                + ", examCategory=" + examCategory
                + ", examDetailSet=" + examDetailSet
                + ", startDateTime=" + startDateTime
                + ", endDateTime=" + endDateTime
                + ", timestamp=" + timestamp
                + '}';
    }
}
