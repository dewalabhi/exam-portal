package com.programmers.repository;

import com.programmers.model.db.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Question repository.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {

    @Query(value = "Select Count(*) from Question q where q.question_category_id = :id1 and q.section_id= :id2", nativeQuery = true)
    int getQuestionCount(@Param("id1") long id1, @Param("id2") long id2);

    @Query(value = "Select * from Question q where q.question_category_id = :id", nativeQuery = true)
    List<Question> findQuestions(@Param("id") long id);

    List<Question> findAll();

}
