package com.programmers.repository.impl;

import com.programmers.model.db.Question;
import com.programmers.repository.QuestionRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Question repository.
 */

@Repository
@Transactional(readOnly = true)
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Find random question list by category id and limit.
     *
     * @param categoryId category of Question
     * @return List of Question
     */
    @Override
    public List<Question> findRandomQuestions(Long categoryId, Long sectionId, int questionLimit) {
        final String query = "SELECT q FROM Question q WHERE q.questionCategory.id = " + categoryId
                + " and q.section.id = " + sectionId + " ORDER BY function('RAND')";
        return entityManager.createQuery(query, Question.class).setMaxResults(questionLimit).getResultList();
    }
}
