package com.programmers.service.impl;

import com.programmers.model.beans.QuestionHistoryBean;
import com.programmers.model.db.QuestionHistory;
import com.programmers.repository.QuestionHistoryRepository;
import com.programmers.service.QuestionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionHistoryServiceImpl implements QuestionHistoryService {

    @Autowired
    private QuestionHistoryRepository questionHistoryRepository;

    public List<QuestionHistoryBean> getAllQuestionHistory(long id) {

        final List<QuestionHistoryBean> questionHistoryBeanList = new ArrayList<>();
        final List<QuestionHistory> questionHistoryList = questionHistoryRepository.questionHistoryByUser(id);

        for (QuestionHistory ques : questionHistoryList) {
            final QuestionHistoryBean questionHistoryBean = new QuestionHistoryBean();
            questionHistoryBean.setId(ques.getId());
            questionHistoryBean.setTimestamp(ques.getTimestamp());
            questionHistoryBeanList.add(questionHistoryBean);
        }
        return questionHistoryBeanList;
    }
}
