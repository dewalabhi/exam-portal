package com.programmers.service;

import com.programmers.model.beans.ExamBean;

public interface GetQuestionsService {
    ExamBean getQuestions(String examId, String userId);
}
