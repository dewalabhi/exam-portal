package com.programmers.service;

import com.programmers.model.db.ExamCategory;

import java.util.List;

public interface ExamCategoryService {

    ExamCategory createExamCategory(ExamCategory examCategory);

    ExamCategory updateExamCategory(long id, ExamCategory examCategory);

    void validate(ExamCategory examCategory);

    List<ExamCategory> getAllExamCategory();
}
