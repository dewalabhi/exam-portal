package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultPerCategoryBean {

    private String questionCategory;

    private int obtainedMarks;

    private int totalMarks;

}
