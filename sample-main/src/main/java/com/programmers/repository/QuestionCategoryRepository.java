package com.programmers.repository;

import com.programmers.model.db.QuestionCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Category repository.
 */
@Repository
public interface QuestionCategoryRepository extends CrudRepository<QuestionCategory, Long> {

    Optional<QuestionCategory> findById(Long id);

    List<QuestionCategory> findAll();

    Optional<QuestionCategory> findByQuestionCategoryName(String questionCategoryName);
}
