package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.ExamCategory;
import com.programmers.repository.ExamCategoryRepository;
import com.programmers.service.ExamCategoryService;
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

import static com.programmers.common.RequestMappingConstant.CREATE_EXAM_CATEGORY;
import static com.programmers.common.RequestMappingConstant.GET_ALL_EXAM_CATEGORY;
import static com.programmers.common.RequestMappingConstant.GET_EXAM_CATEGORY;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;
import static com.programmers.common.RequestMappingConstant.UPDATE_EXAM_CATEGORY;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class ExamCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ExamCategoryController.class);
    @Autowired
    private ExamCategoryService examCategoryService;
    @Autowired
    private ExamCategoryRepository examCategoryRepository;

    @PostMapping(value = CREATE_EXAM_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> createExamCategory(@RequestBody ExamCategory examCategory) {
        logger.debug("create exam category - {}", examCategory.getExamCategoryName());
        examCategoryService.createExamCategory(examCategory);
        return new JsonRest<>(true, new HashMap<>(), "Exam Category created successfully", "", HttpStatus.CREATED);
    }

    @PutMapping(value = UPDATE_EXAM_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> updateExamCategory(@PathVariable(name = "examCategoryId") long examId, @RequestBody ExamCategory examCategory) {
        logger.debug("update exam category - {}", examCategory.getExamCategoryName());
        examCategoryService.updateExamCategory(examId, examCategory);
        return new JsonRest<>(true, new HashMap<>(), "Exam Category updated successfully", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_EXAM_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<ExamCategory>> getAllExamCategoryList() {
        logger.debug("get all exam category");
        final List<ExamCategory> examCategoryList = examCategoryService.getAllExamCategory();
        final HashMap<String, List<ExamCategory>> categoryList = new HashMap<>();
        if (examCategoryList.isEmpty()) {
            return new JsonRest<>(true, categoryList, "Exam Category list empty", "", HttpStatus.OK);
        }
        categoryList.put("examCategoryList", examCategoryList);
        return new JsonRest<>(true, categoryList, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_EXAM_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, ExamCategory> getExamCategoryById(@RequestParam(value = "id") long id) {
        logger.debug("find exam category - {}", id);
        final Optional<ExamCategory> getExamCategory = examCategoryRepository.findById(id);
        final HashMap<String, ExamCategory> examCategories = new HashMap<>();
        getExamCategory.ifPresent(examCategory -> examCategories.put("ExamCategory", examCategory));
        return getExamCategory.map(examCategory -> new JsonRest<>(true, examCategories, "", "", HttpStatus.OK))
                .orElseGet(() -> new JsonRest<>(true, new HashMap<>(), "ExamCategory not found with - " + id, "", HttpStatus.NOT_FOUND));
    }
}