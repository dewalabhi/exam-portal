package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionCategoryBean {

    private String text;

    private List<SectionBean> sections;

}
