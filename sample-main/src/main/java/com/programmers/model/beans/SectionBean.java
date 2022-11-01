package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SectionBean {

    private String examDetailId;

    private String marksPerQuestion;

    private String noOfQuestions;

    private List<QuestionBean> questions;

}
