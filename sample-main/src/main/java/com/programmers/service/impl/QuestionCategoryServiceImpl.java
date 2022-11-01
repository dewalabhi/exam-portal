package com.programmers.service.impl;

import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.db.QuestionCategory;
import com.programmers.repository.QuestionCategoryRepository;
import com.programmers.service.QuestionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    @Override
    public QuestionCategory createQuestionCategory(QuestionCategory questionCategory) {
        validate(questionCategory);
        if (checkQuestionCategoryNameExistOrNot(questionCategory.getQuestionCategoryName())) {
            throw new InvalidEntityException("Question Category name already exists");
        }
        return questionCategoryRepository.save(questionCategory);
    }

    @Override
    public QuestionCategory updateQuestionCategory(Long questionCategoryId, QuestionCategory questionCategory) {
        validate(questionCategory);
        final Optional<QuestionCategory> findQuestionCategory = questionCategoryRepository.findById(questionCategoryId);

        if (!findQuestionCategory.isPresent()) {
            throw new EntityNotFoundException("Question Category not found with - " + questionCategoryId);
        }

        if (!findQuestionCategory.get().getQuestionCategoryName().equals(questionCategory.getQuestionCategoryName())
                && checkQuestionCategoryNameExistOrNot(questionCategory.getQuestionCategoryName())) {
            throw new InvalidEntityException("Question Category name already exists");
        }
        questionCategory.setId(questionCategoryId);
        return questionCategoryRepository.save(questionCategory);
    }

    private boolean checkQuestionCategoryNameExistOrNot(String questionCategoryName) {
        return questionCategoryRepository.findByQuestionCategoryName(questionCategoryName).isPresent();
    }

    public void validate(QuestionCategory questionCategory) {
        final List<String> errorMessage = new ArrayList<>();

        final String questionCategoryName = (questionCategory.getQuestionCategoryName() == null) ? ""
                : questionCategory.getQuestionCategoryName().trim();
        if (questionCategoryName.equalsIgnoreCase("") || !questionCategoryName.matches("^[a-zA-Z]*$")) {
            errorMessage.add("Question Category name required");
        }
        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid Question Category detail - " + errorMessage);
        }
    }
}
