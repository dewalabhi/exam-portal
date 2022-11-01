package com.programmers.service;

import com.programmers.model.beans.QuestionHistoryBean;

import java.util.List;

public interface QuestionHistoryService {
    List<QuestionHistoryBean> getAllQuestionHistory(long id);
}
