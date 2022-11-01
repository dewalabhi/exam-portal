package com.programmers.service.impl;

import com.programmers.globalexception.EntityAlreadyExistsException;
import com.programmers.globalexception.EntityNotFoundException;
import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.db.Exam;
import com.programmers.model.db.ExamCategory;
import com.programmers.model.db.ExamDetail;
import com.programmers.model.db.QuestionCategory;
import com.programmers.model.db.Section;
import com.programmers.repository.ExamRepository;
import com.programmers.repository.QuestionCategoryRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.repository.SectionRepository;
import com.programmers.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Exam createExam(Exam exam) {
        validate(exam);
        checkExamNameExistOrNot(exam.getExamName());
        checkExamKeyExistsOrNot(exam.getPassword());
        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(long examId, Exam exam) {
        validate(exam);
        final Optional<Exam> findExam = examRepository.findById(examId);
        if (!findExam.isPresent()) {
            throw new EntityNotFoundException("Exam not found with - " + examId);
        }
        if (!exam.getExamName().equals(findExam.get().getExamName())) {
            checkExamNameExistOrNot(exam.getExamName());
        }
        if (!exam.getPassword().equals(findExam.get().getPassword())) {
            checkExamKeyExistsOrNot(exam.getPassword());
        }
        exam.setId(examId);
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAllExam() {
        return examRepository.findAll();
    }

    @Override
    public int getTotalQuestionCategoryPerSection(long questionId, long sectionId) {
        return questionRepository.getQuestionCount(questionId, sectionId);
    }

    private void checkExamNameExistOrNot(String examName) {
        final Optional<Exam> findExam = examRepository.findByExamName(examName);
        if (findExam.isPresent()) {
            throw new EntityAlreadyExistsException("Exam name already exists - " + examName);
        }
    }

    private void checkExamKeyExistsOrNot(String password) {
        final Optional<Exam> findExam = examRepository.findByPassword(password);
        if (findExam.isPresent()) {
            throw new EntityAlreadyExistsException("Exam key already exists - " + password);
        }
    }

    public void validate(Exam exam) {
        final List<String> errorMessage = new ArrayList<>();

        final String examName = (exam.getExamName() == null) ? "" : exam.getExamName().trim();
        final String password = (exam.getPassword() == null) ? "" : exam.getPassword().trim();

        final Optional<ExamCategory> examCategory = Optional.ofNullable(exam.getExamCategory());
        final Set<ExamDetail> examDetail = exam.getExamDetailSet();

        if (examName.equalsIgnoreCase("") || !examName.matches("^[a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*$")) {
            errorMessage.add("Exam name required,only alphabet accepted");
        }

        if (!password.startsWith("APT")) {
            errorMessage.add("First 3 characters in Exam Key must be 'APT'");
        }

        if (!password.matches("^[A-Za-z0-9@$!%*?&]*$")) {
            errorMessage.add("Exam key required or Invalid Exam key");
        }

        if (!examCategory.isPresent()) {
            errorMessage.add("Exam Category required");
        }

        if (Objects.isNull(examDetail)) {
            errorMessage.add("Exam Detail required");
        }

        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid Exam detail - " + errorMessage);
        }

        for (ExamDetail examDetailObject : examDetail) {
            final QuestionCategory questionCategory = examDetailObject.getQuestionCategory();
            final Section section = examDetailObject.getSection();
            if (examDetailObject.getNumberOfQuestions() > 0
                    && examDetailObject.getNumberOfQuestions() <= getTotalQuestionCategoryPerSection(
                    questionCategory.getId(), section.getId())) {
                final Optional<QuestionCategory> findQuestionCategory = questionCategoryRepository
                        .findById(questionCategory.getId());
                final Optional<Section> findSection = sectionRepository.findById(section.getId());
                if (!findQuestionCategory.isPresent()) {
                    throw new EntityNotFoundException("Question Category not found - " + questionCategory.getId());
                }
                if (!findSection.isPresent()) {
                    throw new EntityNotFoundException("Section not found with - " + section.getId());
                }
            }
        }
    }
}