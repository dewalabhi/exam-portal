package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExamBean {

    private String examName;

    private String examDuration;

    private String userName;

    private List<QuestionCategoryBean> questionCategories;

    private StatusBean status;
}
