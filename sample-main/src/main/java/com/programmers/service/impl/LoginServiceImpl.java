package com.programmers.service.impl;

import com.programmers.common.Constant;
import com.programmers.model.beans.LoginBean;
import com.programmers.model.beans.LoginStatusBean;
import com.programmers.model.db.Exam;
import com.programmers.model.db.User;
import com.programmers.repository.ExamDetailRepository;
import com.programmers.repository.ExamRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.repository.ResultRepository;
import com.programmers.repository.UserRepository;
import com.programmers.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ExamRepository examRepository;
    @Autowired
    public ExamDetailRepository examDetailRepository;
    @Autowired
    public QuestionRepository questionRepository;
    @Autowired
    public ResultRepository resultRepository;


    public LoginStatusBean validateLoginBean(String emailId, String password) throws ParseException {
        final LoginStatusBean status = new LoginStatusBean(null, null, HttpStatus.OK, "Valid Request", "");
        final String pass = password.substring(0, 3);

        final User user = userRepository.findByEmailId(emailId);
        if (pass.equals("APT")) {
            Exam exam = null;
            final Optional<Exam> examByPassword = examRepository.findByPassword(password);
            if (examByPassword.isPresent()) {
                exam = examByPassword.get();
            }
            if (exam == null) {
                status.setCode(HttpStatus.BAD_REQUEST);
                status.setMessage(Constant.INCORRECT_PASSWORD_MESSAGE);
            } else if (exam.getTimestamp() != null && exam.getStartDateTime() != null
                    && exam.getEndDateTime() != null) {
                logger.info(password);
                logger.info(emailId);
                final Date endDateTime = exam.getEndDateTime();
                final Date startDateTime = exam.getStartDateTime();
                final SimpleDateFormat formatter = new SimpleDateFormat(Constant.DATE_FORMAT);
                final Date endDate = formatter.parse(endDateTime.toString());
                final Date startDate = formatter.parse(startDateTime.toString());
                final Date currentDate = new Date();

                if (currentDate.after(endDate)) {
                    status.setCode(HttpStatus.BAD_REQUEST);
                    status.setMessage(Constant.EXAM_EXPIRED_MESSAGE);
                } else if (startDate.after(currentDate)) {

                    status.setCode(HttpStatus.BAD_REQUEST);
                    status.setMessage(Constant.EXAM_NOT_STARTED + exam.getEndDateTime());
                    status.setStartDate(startDateTime.toString());
                }

                if (user == null) {
                    status.setCode(HttpStatus.BAD_REQUEST);
                    status.setMessage(Constant.INVALID_USER_ID_MESSAGE);
                } else if (resultRepository.findByUserIdAndExamId(user.getId(), exam.getId()).isEmpty()) {
                    status.setCode(HttpStatus.BAD_REQUEST);
                    status.setMessage(Constant.USER_ALREADY_TAKEN_EXAM);
                } else {
                    status.setPath("/portal/instruction");
                }
            }
        }
        return status;
    }

    public Map<String, String> login(LoginBean loginBean) {

        final Map<String, String> claimsMap = new HashMap<>();
        // Exam
        final String password = loginBean.getPassword();
        final Optional<Exam> examByPassword = examRepository.findByPassword(password);
        if ((password).startsWith("APT") && examByPassword.isPresent()) {
            final Exam exam = examByPassword.get();
            claimsMap.put("ExamId", String.valueOf(exam.getId()));
            claimsMap.put("Type", "APT");
        }

        // user
        final String emailId = loginBean.getEmailId();
        final User user = userRepository.findByEmailId(emailId);
        claimsMap.put("UserId", String.valueOf(user.getId()));

        return claimsMap;
    }

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}