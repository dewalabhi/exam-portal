package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.Exam;
import com.programmers.repository.ExamRepository;
import com.programmers.service.ExamService;
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

import static com.programmers.common.RequestMappingConstant.CREATE_EXAM;
import static com.programmers.common.RequestMappingConstant.GET_ALL_EXAM;
import static com.programmers.common.RequestMappingConstant.GET_EXAM;
import static com.programmers.common.RequestMappingConstant.GET_TOTAL_QUESTION_CATEGORY_PER_SECTION;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;
import static com.programmers.common.RequestMappingConstant.UPDATE_EXAM;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class ExamController {

    private final Logger logger = LoggerFactory.getLogger(ExamController.class);
    @Autowired
    private ExamService examService;
    @Autowired
    private ExamRepository examRepository;

    @PostMapping(value = CREATE_EXAM, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> createExam(@RequestBody Exam exam) {
        logger.debug("create exam - {}", exam);
        examService.createExam(exam);
        return new JsonRest<>(true, new HashMap<>(),
                "Exam Created Successfully", "", HttpStatus.CREATED);
    }

    @PutMapping(value = UPDATE_EXAM, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> updateExam(@PathVariable(value = "examID") long examID, @RequestBody Exam exam) {
        logger.debug("update exam id - {} - {}", examID, exam);
        examService.updateExam(examID, exam);
        return new JsonRest<>(true, new HashMap<>(),
                "Exam Updated Successfully", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_EXAM, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, Exam> getExam(@RequestParam(value = "id") long id) {
        final Optional<Exam> getAdmin = examRepository.findById(id);
        final HashMap<String, Exam> exam = new HashMap<>();
        getAdmin.ifPresent(adminUser -> exam.put("Exam", adminUser));
        return getAdmin.map(adminUser -> new JsonRest<>(true, exam, "", "", HttpStatus.OK))
                .orElseGet(() -> new JsonRest<>(true, new HashMap<>(), "Exam not found with - " + id, "", HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = GET_ALL_EXAM, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<Exam>> getAllExam() {
        final List<Exam> examList = examService.getAllExam();
        final HashMap<String, List<Exam>> exam = new HashMap<>();
        exam.put("examList", examList);
        return new JsonRest<>(true, exam, examList.isEmpty() ? "Exam list empty" : "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_TOTAL_QUESTION_CATEGORY_PER_SECTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, Integer> getTotalQuestionCategoryPerSection(@PathVariable(value = "questionID") long questionId,
                                                                        @PathVariable(value = "sectionID") long sectionId) {
        final HashMap<String, Integer> numberOfQuestion = new HashMap<>();
        numberOfQuestion.put("numberOfQuestion", examService.getTotalQuestionCategoryPerSection(questionId, sectionId));
        return new JsonRest<>(true, numberOfQuestion, "", "", HttpStatus.OK);
    }
}