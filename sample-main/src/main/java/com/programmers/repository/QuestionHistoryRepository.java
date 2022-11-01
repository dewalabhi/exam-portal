package com.programmers.repository;

import com.programmers.model.db.QuestionHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface QuestionHistoryRepository extends CrudRepository<QuestionHistory, Long> {

    Optional<QuestionHistory> findById(Long id);

    List<QuestionHistory> findAll();

    @Query(value = "SELECT * FROM question_history q WHERE q.admin_user_id = :id and q.timestamp = :time", nativeQuery = true)
    QuestionHistory questionHistoryByUserIdAndTime(@Param("id") long id, @Param("time") Date time);

    @Query(value = "SELECT * FROM question_history q WHERE q.admin_user_id = :id", nativeQuery = true)
    List<QuestionHistory> questionHistoryByUser(@Param("id") long id);
}
