package com.programmers.service;

import com.programmers.model.db.QuestionCategory;

public interface QuestionCategoryService {

    QuestionCategory createQuestionCategory(QuestionCategory questionCategory);

    QuestionCategory updateQuestionCategory(Long questionCategoryId, QuestionCategory questionCategory);

}
