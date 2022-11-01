package com.programmers.service.impl;

import com.programmers.globalexception.EntityNotFoundException;
import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.DifficultyLevel;
import com.programmers.model.db.Options;
import com.programmers.model.db.Question;
import com.programmers.model.db.QuestionCategory;
import com.programmers.model.db.Section;
import com.programmers.repository.QuestionCategoryRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    private static <E extends Enum<E>> boolean contains(Class<E> enumClass, String value) {
        try {
            return EnumSet.allOf(enumClass).contains(Enum.valueOf(enumClass, value));
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void createQuestion(Question question) {
        validate(question);
        questionRepository.save(question);
    }

    @Override
    public void updateQuestion(Long questionId, Question question) {
        validate(question);
        final Optional<Question> findQuestion = questionRepository.findById(questionId);

        if (!findQuestion.isPresent()) {
            throw new EntityNotFoundException("Question Not found - " + questionId);
        }

        question.setId(questionId);
        questionRepository.save(question);
    }

    public void validate(Question question) {
        final List<String> errorMessage = new ArrayList<>();

        final String questionText = (question.getQuestionText() == null) ? "" : question.getQuestionText().trim();
        final String difficultyLevel = (question.getDifficultyLevel() == null) ? "" : question.getDifficultyLevel().trim();
        final Set<Options> getOptions = question.getOptions();
        final Optional<QuestionCategory> questionCategory = Optional.ofNullable(question.getQuestionCategory());
        final Optional<Section> section = Optional.ofNullable(question.getSection());

        if (difficultyLevel.equalsIgnoreCase("")) {
            errorMessage.add("Question difficulty level required");
        }

        if (!contains(DifficultyLevel.class, difficultyLevel)) {
            errorMessage.add("Question difficulty level must be either EASY, MEDIUM or HARD");
        }

        if (questionText.equalsIgnoreCase("")) {
            errorMessage.add("Question Text name required");
        }

        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid Question detail - " + errorMessage);
        }
        if (!questionCategory.isPresent()) {
            throw new InvalidEntityException("Question Category required");
        }
        if (!section.isPresent()) {
            throw new InvalidEntityException("Question Section required");
        }
        if (getOptions.isEmpty()) {
            throw new InvalidEntityException("Option can't be empty");
        }

        final Optional<QuestionCategory> findExamCategoryStatus = questionCategoryRepository.findById(questionCategory.get().getId());
        if (findExamCategoryStatus.isPresent() && !questionCategory.get().getIsActive()) {
            throw new EntityNotFoundException("Question Category with Id: " + questionCategory.get().getQuestionCategoryName()
                    + " is not present or not active");
        }

        final Set<String> uniqueOptions = new HashSet<>();
        boolean trueOption = false;
        for (Options options : getOptions) {
            final String optionText = (options.getOptionText() == null) ? "" : options.getOptionText().trim();
            if (optionText.equalsIgnoreCase("")) {
                throw new InvalidEntityException("Option can't be empty");
            }
            if (!uniqueOptions.contains(optionText)) {
                uniqueOptions.add(optionText);
            } else {
                throw new InvalidEntityException("Option can't be duplicate");
            }
            final boolean correctOption = options.getAnswer();

            if (trueOption && correctOption) {
                throw new InvalidEntityException("One option is allowed to be true");
            }
            if (correctOption) {
                trueOption = correctOption;
            }
        }

        if (!trueOption) {
            throw new InvalidEntityException("Correct option should be true");
        }
    }
}