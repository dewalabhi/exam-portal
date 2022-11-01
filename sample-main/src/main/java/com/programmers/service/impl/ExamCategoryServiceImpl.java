package com.programmers.service.impl;

import com.programmers.globalexception.EntityAlreadyExistsException;
import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.db.ExamCategory;
import com.programmers.repository.ExamCategoryRepository;
import com.programmers.service.ExamCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamCategoryServiceImpl implements ExamCategoryService {

    @Autowired
    private ExamCategoryRepository examCategoryRepository;

    @Override
    public ExamCategory createExamCategory(ExamCategory examCategory) {
        validate(examCategory);
        final String examCategoryName = examCategory.getExamCategoryName();
        if (findExamByName(examCategory) && !examCategoryName.isEmpty()) {
            throw new EntityAlreadyExistsException("Exam category" + examCategoryName + " already exists");
        }
        return examCategoryRepository.save(examCategory);
    }

    @Override
    public ExamCategory updateExamCategory(long id, ExamCategory examCategory) {
        validate(examCategory);
        final Optional<ExamCategory> findExamCategory = examCategoryRepository.findById(id);
        final String examCategoryName = examCategory.getExamCategoryName();
        if (findExamCategory.isPresent() && findExamByName(examCategory) && !examCategoryName.isEmpty()) {
            throw new EntityAlreadyExistsException(
                    "Exam category " + examCategory.getExamCategoryName().trim() + " already exists");
        }
        examCategory.setId(id);
        return examCategoryRepository.save(examCategory);
    }

    @Override
    public void validate(ExamCategory examCategory) {
        final List<String> errorMessage = new ArrayList<>();

        final String examCategoryName = (examCategory.getExamCategoryName() == null) ? ""
                : examCategory.getExamCategoryName().trim();

        if (examCategoryName.equalsIgnoreCase("") || !examCategoryName.matches("^[A-Za-z]*$")) {
            errorMessage.add("Exam Category required");
        }

        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid Exam Category detail - " + errorMessage);
        }
    }

    @Override
    public List<ExamCategory> getAllExamCategory() {
        return examCategoryRepository.findAll();
    }

    private boolean findExamByName(ExamCategory examCategory) {
        final String examCategoryName = examCategory.getExamCategoryName();
        if (!examCategoryName.isEmpty()) {
            return examCategoryRepository.existsByExamCategoryName(examCategoryName.trim());
        } else {
            return false;
        }
    }
}