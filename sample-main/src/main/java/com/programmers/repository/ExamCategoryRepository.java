package com.programmers.repository;

import com.programmers.model.db.ExamCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamCategoryRepository extends CrudRepository<ExamCategory, Long> {
    List<ExamCategory> findAll();

    boolean existsByExamCategoryName(String examCategoryName);
}