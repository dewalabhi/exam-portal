package com.programmers.service;

import com.programmers.model.db.Exam;

import java.util.List;

public interface ExamService {

    Exam createExam(Exam exam);

    Exam updateExam(long examId, Exam exam);

    List<Exam> getAllExam();

    int getTotalQuestionCategoryPerSection(long questionId, long sectionId);
}
