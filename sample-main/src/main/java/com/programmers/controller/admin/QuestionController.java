package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.Question;
import com.programmers.repository.QuestionRepository;
import com.programmers.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.programmers.common.RequestMappingConstant.CREATE_QUESTION;
import static com.programmers.common.RequestMappingConstant.DELETE_BY_QUESTION_CATEGORY;
import static com.programmers.common.RequestMappingConstant.DELETE_QUESTION;
import static com.programmers.common.RequestMappingConstant.GET_ALL_QUESTION;
import static com.programmers.common.RequestMappingConstant.GET_QUESTION;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;
import static com.programmers.common.RequestMappingConstant.UPDATE_QUESTION;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class QuestionController {

    private final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping(value = CREATE_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> createQuestion(@Valid @RequestBody Question question) {
        logger.debug("Create Question - {}", question);
        questionService.createQuestion(question);
        return new JsonRest<>(true, new HashMap<>(), "Question created successfully ", "", HttpStatus.CREATED);
    }

    @PutMapping(value = UPDATE_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> updateQuestion(@PathVariable(value = "questionId") Long questionId, @Valid @RequestBody Question question) {
        logger.debug("Update question with id - {} - {}", questionId, question);
        questionService.updateQuestion(questionId, question);
        return new JsonRest<>(true, new HashMap<>(), "Question updated successfully", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<Question>> getAllQuestions() {
        final List<Question> questionList = questionRepository.findAll();
        final HashMap<String, List<Question>> questionListMap = new HashMap<>();
        questionListMap.put("questionList", questionList);
        if (questionList.isEmpty()) {
            return new JsonRest<>(true, questionListMap, "Question list empty", "", HttpStatus.OK);
        }
        return new JsonRest<>(true, questionListMap, "", "", HttpStatus.OK);

    }

    @GetMapping(value = GET_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, Question> getQuestion(@RequestParam(value = "id") long id) {
        final HashMap<String, Question> questionMap = new HashMap<>();
        final Optional<Question> question = questionRepository.findById(id);
        question.ifPresent(adminUser -> questionMap.put("question", adminUser));
        return question.map(adminUser -> new JsonRest<>(true, questionMap, "", "", HttpStatus.OK))
                .orElseGet(() -> new JsonRest<>(true, new HashMap<>(), "Question not found with - " + id, "", HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = DELETE_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> deleteQuestion(@RequestParam(value = "id") long id) {
        final Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            questionRepository.delete(question.get());
            return new JsonRest<>(true, new HashMap<>(), "Question deleted successfully with id - " + id, "", HttpStatus.OK);
        }
        return new JsonRest<>(true, new HashMap<>(), "Question not found with id - " + id, "", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = DELETE_BY_QUESTION_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> deleteByQuestionCategoryId(@RequestParam(value = "id") long id) {
        final List<Question> questionList = questionRepository.findQuestions(id);
        if (questionList.isEmpty()) {
            return new JsonRest<>(true, new HashMap<>(), "Question category list empty", "", HttpStatus.OK);
        }
        questionRepository.deleteAll(questionList);
        return new JsonRest<>(true, new HashMap<>(), "Question deleted successfully with id - " + id, "", HttpStatus.OK);
    }
}