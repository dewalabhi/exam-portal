package com.programmers.service;

import com.programmers.common.CustomException;
import com.programmers.model.beans.ExamBean;
import com.programmers.model.beans.ResultPerCategoryBean;
import com.programmers.model.beans.StatusBean;
import com.programmers.model.db.Result;

import java.util.List;

public interface ResultService {

    StatusBean validateSubmitRequest(ExamBean examBean, String examId, String userId);

    List<ResultPerCategoryBean> calculateResult(ExamBean examBean, String examId, String userId) throws CustomException;

    Result saveResult(Result result);

    List<Result> getAllResult();

    List<Result> getResult(long id);

}
