package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionBean {

    private String id;

    private String text;

    private List<OptionBean> options;

    private String answer;
}
