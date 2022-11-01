package com.programmers.repository;

import com.programmers.model.db.ExamDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamDetailRepository extends CrudRepository<ExamDetail, Long> {

    Optional<ExamDetail> findById(Long id);
}
