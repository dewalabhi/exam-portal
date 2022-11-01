package com.programmers.controller.portal;

import com.programmers.common.AppUtils;
import com.programmers.model.JsonRest;
import com.programmers.model.beans.ExamBean;
import com.programmers.service.GetQuestionsService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static com.programmers.common.RequestMappingConstant.GET_QUESTION;
import static com.programmers.common.RequestMappingConstant.PATH_PORTAL;

/**
 * @author Programmers.io
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_PORTAL)
public class GetQuestionsController {

    private static final Logger logger = LoggerFactory.getLogger(GetQuestionsController.class);

    @Autowired
    private GetQuestionsService getQuestionsService;

    @GetMapping(value = GET_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, ExamBean> getQuestions(HttpServletRequest request) {
        logger.info("API call: /portal/getCodingQuestions");
        final Claims claims = AppUtils.fetchClaimsFromToken(request);
        final String examId = (String) claims.get("ExamId");
        final String userId = (String) claims.get("UserId");
        final HashMap<String, ExamBean> examQuestion = new HashMap<>();
        examQuestion.put("ExamQuestion", getQuestionsService.getQuestions(examId, userId));
        return new JsonRest<>(true, examQuestion, "", "", HttpStatus.OK);
    }
}
