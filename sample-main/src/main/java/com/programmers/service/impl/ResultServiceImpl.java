package com.programmers.service.impl;

import com.programmers.common.CustomException;
import com.programmers.model.beans.ExamBean;
import com.programmers.model.beans.QuestionBean;
import com.programmers.model.beans.QuestionCategoryBean;
import com.programmers.model.beans.ResultPerCategoryBean;
import com.programmers.model.beans.SectionBean;
import com.programmers.model.beans.StatusBean;
import com.programmers.model.db.Exam;
import com.programmers.model.db.ExamDetail;
import com.programmers.model.db.Options;
import com.programmers.model.db.Result;
import com.programmers.model.db.ResultDetail;
import com.programmers.model.db.Section;
import com.programmers.model.db.User;
import com.programmers.repository.ExamDetailRepository;
import com.programmers.repository.ExamRepository;
import com.programmers.repository.OptionsRepository;
import com.programmers.repository.QuestionCategoryRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.repository.ResultDetailRepository;
import com.programmers.repository.ResultRepository;
import com.programmers.repository.SectionRepository;
import com.programmers.repository.UserRepository;
import com.programmers.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    public ExamRepository examRepository;
    @Autowired
    public ExamDetailRepository examDetailRepository;
    @Autowired
    public QuestionRepository questionRepository;
    @Autowired
    public SectionRepository sectionRepository;
    @Autowired
    public OptionsRepository optionsRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public QuestionCategoryRepository questionCategoryRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private ResultDetailRepository resultDetailRepository;

    @Override
    public StatusBean validateSubmitRequest(ExamBean examBean, String examId, String userId) {
        final StatusBean status = new StatusBean(HttpStatus.OK, "Valid Request");
        boolean error = false;
        String message = null;
        final List<QuestionCategoryBean> questionCategoryBeans = examBean.getQuestionCategories();
        final List<Result> resultList = resultRepository.findByUserIdAndExamId(Long.parseLong(userId),
                Long.parseLong(examId));
        if (!resultList.isEmpty()) {
            error = true;
            message = "Invalid exam to submit";
        }
        if (!error) {
            final Optional<Exam> examById = examRepository.findById(Long.parseLong(examId));
            Exam exam = null;
            if (examById.isPresent()) {
                exam = examById.get();
            }
            assert exam != null;
            final Set<ExamDetail> examDetailSet = exam.getExamDetailSet();

            if (questionCategoryBeans == null) {
                error = true;
                message = "Question Category not found";
            } else {
                for (QuestionCategoryBean questionCategoryBean : questionCategoryBeans) {
                    final List<SectionBean> sectionBeans = questionCategoryBean.getSections();
                    if (sectionBeans == null) {
                        error = true;
                        message = "Sections not found";
                    } else {
                        for (SectionBean sectionBean : sectionBeans) {
                            long examDetailId = 0;
                            if (sectionBean.getExamDetailId() != null) {
                                examDetailId = Long.parseLong(sectionBean.getExamDetailId());
                                final int noOfQuestions = Integer.parseInt(sectionBean.getNoOfQuestions());
                                ExamDetail examDetail = null;
                                final Optional<ExamDetail> examDetailById = examDetailRepository.findById(examDetailId);
                                if (examDetailById.isPresent()) {
                                    examDetail = examDetailById.get();
                                    if (!examDetailSet.contains(examDetail)) {
                                        error = true;
                                        message = "Invalid ExamDetail";
                                    } else if (sectionBean.getQuestions().size() != noOfQuestions) {
                                        error = true;
                                        message = "Invalid value for noOfQuestions";
                                    }
                                } else {
                                    error = true;
                                    message = "Invalid Exam Detail Id";
                                }
                            } else {
                                error = true;
                                message = "Exam Detail Id not found in sections";
                            }
                        }
                    }
                }
            }
        }
        if (error) {
            status.setCode(HttpStatus.BAD_REQUEST);
            status.setMessage(message);
        }
        return status;
    }

    @Override
    public List<ResultPerCategoryBean> calculateResult(ExamBean examBean, String examId, String userId) throws CustomException {
        final Set<ResultDetail> resultByCategorySet = new HashSet<>();

        Result result = new Result();
        final Optional<User> userById = userRepository.findById(Long.parseLong(userId));
        User user = null;
        if (userById.isPresent()) {
            user = userById.get();
        }

        result.setUser(user);
        final Optional<Exam> examById = examRepository.findById(Long.parseLong(examId));
        Exam exam = null;
        if (examById.isPresent()) {
            exam = examById.get();
        }
        result.setExam(exam);
        result = saveResult(result);

        final List<ResultPerCategoryBean> resultResponseList = new ArrayList<>();
        final List<QuestionCategoryBean> questionCategoryBeans = examBean.getQuestionCategories();
        int totalExamMarks = 0;
        int totalObtainedMarks = 0;
        for (QuestionCategoryBean questionCategoryBean : questionCategoryBeans) {

            int totalMarksPerCategory = 0;
            int obtainedMarksPerCategory = 0;

            final List<SectionBean> sectionBeans = questionCategoryBean.getSections();
            final ResultPerCategoryBean resultResponse = new ResultPerCategoryBean();
            resultResponse.setQuestionCategory(questionCategoryBean.getText());
            for (SectionBean sectionBean : sectionBeans) {
                final long examDetailId = Long.parseLong(sectionBean.getExamDetailId());
                final Optional<ExamDetail> examDetailById = examDetailRepository.findById(examDetailId);
                ExamDetail examDetail = new ExamDetail();
                if (examDetailById.isPresent()) {
                    examDetail = examDetailById.get();
                }

                final Section section = examDetail.getSection();
                ResultDetail resultDetail = new ResultDetail();
                resultDetail.setExamDetail(examDetail);
                int obtainedMarks = 0;
                final int totalMarks = examDetail.getNumberOfQuestions() * section.getMarksPerQuestion();
                totalMarksPerCategory = totalMarksPerCategory + totalMarks;
                resultDetail.setTotalMarks(totalMarks);

                final List<QuestionBean> questionbeans = sectionBean.getQuestions();
                for (QuestionBean questionBean : questionbeans) {
                    if (questionBean.getAnswer() != null) {
                        long answerId = 0;
                        try {
                            answerId = Long.parseLong(questionBean.getAnswer());
                        } catch (Exception ex) {
                            throw new CustomException("Answer should always be numeric (id of correct option)");
                        }
                        final Optional<Options> options = optionsRepository.findById(answerId);
                        if (options.isPresent() && questionBean.getId() != null) {
                            final boolean idMatch = options.get().getAnswer();
                            if (idMatch) {
                                obtainedMarks += section.getMarksPerQuestion();
                            }
                        }
                    }
                }
                obtainedMarksPerCategory += obtainedMarks;

                resultDetail.setObtainedMarks(obtainedMarks);
                resultDetail = resultDetailRepository.save(resultDetail);
                resultByCategorySet.add(resultDetail);
            }
            totalObtainedMarks += obtainedMarksPerCategory;
            totalExamMarks = totalExamMarks + totalMarksPerCategory;
            resultResponse.setObtainedMarks(obtainedMarksPerCategory);
            resultResponse.setTotalMarks(totalMarksPerCategory);
            resultResponseList.add(resultResponse);
        }
        result.setTotalMarks(totalExamMarks);
        result.setObtainedMarks(totalObtainedMarks);
        result.setResultDetailSet(resultByCategorySet);
        result = saveResult(result);
        return resultResponseList;
    }

    @Override
    public Result saveResult(Result result) {
        result = resultRepository.save(result);
        return result;
    }

    @Override
    public List<Result> getAllResult() {
        return resultRepository.findAll();
    }

    @Override
    public List<Result> getResult(long id) {
        return resultRepository.resultByUserId(id);
    }
}