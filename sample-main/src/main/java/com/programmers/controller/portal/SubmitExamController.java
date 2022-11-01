package com.programmers.controller.portal;

import com.programmers.common.AppUtils;
import com.programmers.common.Constant;
import com.programmers.model.beans.ExamBean;
import com.programmers.model.beans.ResultBean;
import com.programmers.model.beans.ResultPerCategoryBean;
import com.programmers.model.beans.StatusBean;
import com.programmers.service.ResultService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.programmers.common.RequestMappingConstant.PATH_PORTAL;
import static com.programmers.common.RequestMappingConstant.SUBMIT;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_PORTAL)
public class SubmitExamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitExamController.class);

    @Autowired
    private ResultService resultService;

    @PostMapping(value = SUBMIT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> submitExam(@RequestBody ExamBean examBean, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("API call: /submit for user and exam : {} and {}", examBean.getUserName(), examBean.getExamName());

        StatusBean status = new StatusBean();
        final ResultBean resultBean = new ResultBean();
        final List<ResultPerCategoryBean> resultResponseList;
        final ResponseEntity<Object> responseEntity;

        try {
            final Claims claims = AppUtils.fetchClaimsFromToken(request);
            final String examId = (String) claims.get("ExamId");
            final String userId = (String) claims.get("UserId");
            status = resultService.validateSubmitRequest(examBean, examId, userId);
            if (status.getCode() == HttpStatus.OK) {
                resultResponseList = resultService.calculateResult(examBean, examId, userId);
                resultBean.setResultResponseList(resultResponseList);
                status = new StatusBean(HttpStatus.OK, Constant.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            status.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            status.setMessage(ex.getMessage());
            LOGGER.error(ex.getMessage());
        }
        resultBean.setStatus(status);
        responseEntity = new ResponseEntity<>(resultBean, status.getCode());
        return responseEntity;
    }
}

