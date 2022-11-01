package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.beans.QuestionHistoryBean;
import com.programmers.model.db.Exam;
import com.programmers.model.db.Question;
import com.programmers.model.db.QuestionHistory;
import com.programmers.model.db.Result;
import com.programmers.model.db.User;
import com.programmers.model.response.StatusResponse;
import com.programmers.repository.ExamRepository;
import com.programmers.repository.QuestionHistoryRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.repository.ResultRepository;
import com.programmers.repository.UserRepository;
import com.programmers.service.DownloadExcelService;
import com.programmers.service.DownloadResultService;
import com.programmers.service.QuestionHistoryService;
import com.programmers.service.ReadExcelService;
import com.programmers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.programmers.common.Constant.CODE_VALIDATION_ERROR;
import static com.programmers.common.RequestMappingConstant.ADD_QUESTIONS;
import static com.programmers.common.RequestMappingConstant.DOWNLOAD_CANDIDATE_LIST;
import static com.programmers.common.RequestMappingConstant.DOWNLOAD_QUESTIONS;
import static com.programmers.common.RequestMappingConstant.DOWNLOAD_RESULT;
import static com.programmers.common.RequestMappingConstant.GET_ALL_QUESTION_HISTORY;
import static com.programmers.common.RequestMappingConstant.GET_QUESTIONS_HISTORY;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class ExcelController {

    @Autowired
    private ReadExcelService readExcelService;
    @Autowired
    private DownloadExcelService downloadExcelService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionHistoryRepository questionHistoryRepository;
    @Autowired
    private QuestionHistoryService questionHistoryService;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private DownloadResultService downloadResultService;

    @PostMapping(value = ADD_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<Question>> addQuestions(@RequestBody String fileBlob) {

        final StatusResponse statusResponse = readExcelService.processExcel(fileBlob);
        final List<Question> questionList = questionRepository.findAll();
        final HashMap<String, List<Question>> response = new HashMap<>();
        response.put("questionList", questionList);
        return new JsonRest<>(true, response, statusResponse.getMessage(), "", statusResponse.getStatus());
    }

    @GetMapping(value = DOWNLOAD_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> excelQuestionsReport(@RequestParam(value = "id") long id) throws IOException {
        ResponseEntity<Object> response = null;
        final StatusResponse statusResponse = new StatusResponse();
        final List<Question> questions = questionRepository.findQuestions(id);
        if (questions.isEmpty()) {
            statusResponse.setCode(CODE_VALIDATION_ERROR);
            statusResponse.setMessage("");
            statusResponse.setStatus(HttpStatus.FORBIDDEN);
            response = new ResponseEntity<>(statusResponse, statusResponse.getStatus());
            return response;
        } else {
            final ByteArrayInputStream in = downloadExcelService.questionsToExcel(questions);

            return ResponseEntity.ok().body(new InputStreamResource(in));
        }
    }

    @GetMapping(value = GET_QUESTIONS_HISTORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getQuestionHistoryById(@RequestParam(value = "id") String id) {

        QuestionHistory questionHistory = new QuestionHistory();
        final Optional<QuestionHistory> optionalQuestionHistory = questionHistoryRepository.findById(Long.parseLong(id));

        if (optionalQuestionHistory.isPresent()) {
            questionHistory = optionalQuestionHistory.get();
        }
        final byte[] b = questionHistory.getExcelData();

        final ByteArrayInputStream byt = new ByteArrayInputStream(b);

        return ResponseEntity.ok().body(new InputStreamResource(byt));

    }

    @GetMapping(value = DOWNLOAD_RESULT)
    public ResponseEntity<Object> excelResultReport(@RequestParam(value = "id") long id) throws IOException {
        ResponseEntity<Object> response = null;
        final StatusResponse statusResponse = new StatusResponse();
        final Optional<Exam> exams = examRepository.findById(id);
        Exam exam = new Exam();
        if (exams.isPresent()) {
            exam = exams.get();
        }
        final List<Result> result = resultRepository.findByExamId(id);
        if (result.isEmpty()) {
            statusResponse.setCode(CODE_VALIDATION_ERROR);
            statusResponse.setMessage("");
            statusResponse.setStatus(HttpStatus.FORBIDDEN);
            response = new ResponseEntity<>(statusResponse, statusResponse.getStatus());
            return response;
        } else {
            final ByteArrayInputStream in = downloadResultService.resultToExcel(result, exam);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(new InputStreamResource(in));
        }
    }

    @GetMapping(value = GET_ALL_QUESTION_HISTORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<QuestionHistoryBean>> getAllQuestionHistory(@RequestParam(value = "userId") long id) {
        final List<QuestionHistoryBean> questionHistoryBeanList = questionHistoryService.getAllQuestionHistory(id);
        final HashMap<String, List<QuestionHistoryBean>> questionHistory = new HashMap<>();
        questionHistory.put("questionHistoryBeanList", questionHistoryBeanList);
        if (questionHistoryBeanList.isEmpty()) {

            return new JsonRest<>(true, questionHistory, "Question history empty", "", HttpStatus.OK);
        }
        return new JsonRest<>(true, questionHistory, "", "", HttpStatus.OK);
    }

    @GetMapping(value = DOWNLOAD_CANDIDATE_LIST)
    public ResponseEntity<Object> excelCandidateReport() throws IOException {
        ResponseEntity<Object> response = null;
        final StatusResponse statusResponse = new StatusResponse();
        final List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            statusResponse.setCode(CODE_VALIDATION_ERROR);
            statusResponse.setMessage("");
            statusResponse.setStatus(HttpStatus.FORBIDDEN);
            response = new ResponseEntity<>(statusResponse, statusResponse.getStatus());
            return response;
        } else {
            final ByteArrayInputStream in = userService.downloadCandidate(userList);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Candidate.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(new InputStreamResource(in));
        }
    }

}