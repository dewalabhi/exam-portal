package com.programmers.repository;

import com.programmers.model.db.Result;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {

    List<Result> findByUserIdAndExamId(long userId, long examId);

    @Query(value = "SELECT * FROM Result r WHERE r.exam_id = :id", nativeQuery = true)
    List<Result> findByExamId(@Param("id") long id);

    @Query(value = "SELECT * FROM Result r WHERE r.user_id = :id", nativeQuery = true)
    List<Result> resultByUserId(@Param("id") long id);

    List<Result> findAll();
}

