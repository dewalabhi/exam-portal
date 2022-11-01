package com.programmers.globalexception;

import com.programmers.model.JsonRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestControllerAdvice(basePackages = {"com.programmers"})
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(value = InvalidTokenException.class)
    public JsonRest<Object, Object> invalidToken(InvalidTokenException ex, HttpServletRequest request) {
        log.error("InvalidTokenException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public JsonRest<Object, Object> entityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("EntityNotFoundException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExpiredTokenException.class)
    public JsonRest<Object, Object> expiredToken(ExpiredTokenException ex, HttpServletRequest request) {
        log.error("ExpiredTokenException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidEmailException.class)
    public JsonRest<Object, Object> invalidEmail(InvalidEmailException ex, HttpServletRequest request) {
        log.error("InvalidEmailException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidEntityException.class)
    public JsonRest<Object, Object> invalidEntityException(InvalidEntityException ex, HttpServletRequest request) {
        log.error("InvalidEntityException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public JsonRest<Object, Object> numberFormatException(NumberFormatException ex, HttpServletRequest request) {
        log.error("NumberFormatException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public JsonRest<Object, Object> exception(Exception ex, HttpServletRequest request) {
        log.error("Exception with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), "Internal Server Error", request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UserSecurityException.class)
    public JsonRest<Object, Object> userSecurityException(UserSecurityException ex, HttpServletRequest request) {
        log.error("UserSecurityException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(false, new HashMap<>(), ex.getMessage(), "", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    public JsonRest<Object, Object> entityAlreadyExistsException(EntityAlreadyExistsException ex,
                                                                 HttpServletRequest request) {
        log.error("EntityAlreadyExistsException with request - {} {}", request.getRequestURI(), ex);
        return new JsonRest<>(true, new HashMap<>(), ex.getMessage(), "", HttpStatus.BAD_REQUEST);
    }
}
