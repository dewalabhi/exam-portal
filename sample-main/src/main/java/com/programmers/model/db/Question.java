package com.programmers.model.db;

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
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @JsonIgnore
    @UpdateTimestamp
    public Date timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false)
    private QuestionCategory questionCategory;

    @ManyToOne(optional = false)
    private Section section;

    @Column(length = 1000)
    private String questionText;

    private String difficultyLevel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private Set<Options> options;

    private Boolean isActive = false;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", questionCategory=" + questionCategory
                + ", section=" + section
                + ", questionText='" + questionText + '\''
                + ", difficultyLevel=" + difficultyLevel
                + ", options=" + options
                + ", isActive=" + isActive
                + ", timestamp=" + timestamp
                + '}';
    }
}
