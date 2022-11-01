package com.programmers.controller.portal;

import com.programmers.common.CustomException;
import com.programmers.configuration.jwt.portal.PortalJwtTokenUtil;
import com.programmers.globalexception.InvalidEmailException;
import com.programmers.globalexception.UserSecurityException;
import com.programmers.model.beans.LoginBean;
import com.programmers.model.beans.LoginStatusBean;
import com.programmers.model.db.User;
import com.programmers.repository.UserRepository;
import com.programmers.service.LoginService;
import com.programmers.service.UserService;
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
import java.util.Map;
import java.util.Optional;

import static com.programmers.common.RequestMappingConstant.LOGIN;
import static com.programmers.common.RequestMappingConstant.PATH_PORTAL;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_PORTAL)
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private PortalJwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping(value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody LoginBean loginBean) {
        final boolean validateEmail = userService.validateEmail(loginBean.getEmailId());
        if (!validateEmail) {
            throw new InvalidEmailException("Invalid Email - " + loginBean.getEmailId());
        }
        final boolean detailSaveOrNot = userService.checkUserDetailSaveOrNot(loginBean.getEmailId());
        if (!detailSaveOrNot) {
            throw new UserSecurityException("Email address not verified");
        }
        logger.info("API call: /login for request : {}", loginBean);

        LoginStatusBean status = new LoginStatusBean();
        ResponseEntity<Object> responseEntity = null;
        logger.info("Login : {}", loginBean);

        final String emailId = loginBean.getEmailId();
        final String password = loginBean.getPassword();


        try {
            status = loginService.validateLoginBean(emailId, password);
            if (status.getCode() == HttpStatus.OK) {
                final Map<String, String> claimsMap = loginService.login(loginBean);
                final String examId = claimsMap.get("ExamId");
                final String userId = claimsMap.get("UserId");
                final String type = claimsMap.get("Type");
                final long id = Long.parseLong(userId);
                final Optional<User> userNameFromId = userRepository.findById(id);
                String firstName = "";
                String middleName = "";
                String lastName = "";
                if (userNameFromId.isPresent()) {
                    firstName = userNameFromId.get().getFirstName();
                    middleName = userNameFromId.get().getMiddleName();
                    lastName = userNameFromId.get().getLastName();

                }
                final String userName = firstName + " " + (middleName == null ? "" : middleName + " ") + lastName;
            } else {
                throw new IllegalStateException("Unexpected value: " + status.getCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            status.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            status.setMessage(ex.getMessage());
            logger.error(ex.getMessage());
        }
        responseEntity = new ResponseEntity<>(status, status.getCode());
        return responseEntity;
    }
}
