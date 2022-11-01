package com.programmers.common;

public class Constant {

    //Password Constant
    public static final Long USER_PASSWORD = (long) 12345;
    public static final Long JWT_TOKEN_VALIDITY = (long) (5 * 60 * 60);
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String JWT_SECRET = "jwt.secret";
    //status messages
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error!!";
    public static final String INVALID_USER_ID_MESSAGE = "Invalid User Id";
    public static final String INCORRECT_PASSWORD_MESSAGE = "Invalid exam key";
    public static final String EXAM_EXPIRED_MESSAGE = "Exam has Expired";
    public static final String USER_ALREADY_TAKEN_EXAM = "User has already taken the exam";
    public static final String EXAM_NOT_STARTED = "Exam will start on ";
    public static final String PROGRAM_UPLOADED = "Programs submitted successfully";
    public static final String PROGRAM_NOT_UPLOADED = "Programs upload failed";
    public static final String MESSAGE_NOT_FOUND = "Record not found with Id : ";
    public static final String CODING_QUESTION_ID_FETCH = "Select coding_question_id from coding_exam_detail where coding_exam_id=:examId";
    public static final String CODING_EXAM_LIST = "codingExamList";
    public static final String INVALID_EMAIL = "Invalid Email - ";
    public static final String ALREADY_EXISTS = "already exists";
    // Admin service constants
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int CODE_VALIDATION_ERROR = 413;

    private Constant() {
        throw new IllegalStateException("Constant Class");
    }
}
