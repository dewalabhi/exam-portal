package com.programmers.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResultDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private ExamDetail examDetail;

    private int obtainedMarks;

    private int totalMarks;

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", examDetail=" + examDetail
                + ", obtainedMarks=" + obtainedMarks
                + ", totalMarks=" + totalMarks
                + '}';
    }
}