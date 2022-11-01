package com.programmers.repository;

import com.programmers.model.db.Exam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {

    Optional<Exam> findById(Long id);

    List<Exam> findAll();

    Optional<Exam> findByPassword(String password);

    Optional<Exam> findByExamName(String examName);

}
