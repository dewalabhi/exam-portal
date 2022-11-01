package com.programmers.repository;

import com.programmers.model.db.Question;

import java.util.List;

/**
 * Question repository Custom.
 */
public interface QuestionRepositoryCustom {

    List<Question> findRandomQuestions(Long categoryId, Long sectionId, int questionLimit);

}
