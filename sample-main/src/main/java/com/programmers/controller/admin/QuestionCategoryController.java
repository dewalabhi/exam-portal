package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.QuestionCategory;
import com.programmers.repository.QuestionCategoryRepository;
import com.programmers.service.QuestionCategoryService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.programmers.common.RequestMappingConstant.CREATE_QUESTION_CATEGORY;
import static com.programmers.common.RequestMappingConstant.GET_ALL_QUESTION_CATEGORY;
import static com.programmers.common.RequestMappingConstant.GET_QUESTION_CATEGORY;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;
import static com.programmers.common.RequestMappingConstant.UPDATE_QUESTION_CATEGORY;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class QuestionCategoryController {

    private final Logger logger = LoggerFactory.getLogger(QuestionCategoryController.class);

    @Autowired
    private QuestionCategoryService questionCategoryService;

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    @PostMapping(value = CREATE_QUESTION_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> createQuestionCategory(@RequestBody QuestionCategory questionCategory) {
        logger.debug("create question category - {}", questionCategory.getQuestionCategoryName());
        questionCategoryService.createQuestionCategory(questionCategory);
        return new JsonRest<>(true, new HashMap<>(), "Question Category created successfully", "", HttpStatus.CREATED);
    }

    @PutMapping(value = UPDATE_QUESTION_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> updateQuestionCategory(@PathVariable(value = "questionCategoryID") Long questionCategoryID,
                                                           @RequestBody QuestionCategory questionCategory) {
        logger.debug("update question category - {}", questionCategory.getQuestionCategoryName());
        questionCategoryService.updateQuestionCategory(questionCategoryID, questionCategory);
        return new JsonRest<>(true, new HashMap<>(), "Question Category updated successfully", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_QUESTION_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<QuestionCategory>> getAllQuestionCategory() {
        logger.debug("find all question category");
        final List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        final HashMap<String, List<QuestionCategory>> questionCategory = new HashMap<>();
        questionCategory.put("questionCategoryList", questionCategoryList);
        if (questionCategoryList.isEmpty()) {
            return new JsonRest<>(true, questionCategory, "Question Category list empty", "", HttpStatus.OK);
        }
        return new JsonRest<>(true, questionCategory, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_QUESTION_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, QuestionCategory> getCategory(@RequestParam(value = "id") long id) {
        logger.debug("find question category - {}", id);
        final Optional<QuestionCategory> questionCategory = questionCategoryRepository.findById(id);
        final HashMap<String, QuestionCategory> questionCategoryMap = new HashMap<>();
        questionCategory.ifPresent(adminUser -> questionCategoryMap.put("Category", adminUser));
        return questionCategory.map(adminUser -> new JsonRest<>(true, questionCategoryMap, "", "", HttpStatus.OK))
                .orElseGet(() -> new JsonRest<>(true, new HashMap<>(), "Question Category not found with - " + id, "", HttpStatus.NOT_FOUND));
    }
}