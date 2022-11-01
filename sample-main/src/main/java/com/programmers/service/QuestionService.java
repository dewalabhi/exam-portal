package com.programmers.service;

import com.programmers.model.db.Question;

public interface QuestionService {

    void createQuestion(Question question);

    void updateQuestion(Long questionId, Question question);
}
