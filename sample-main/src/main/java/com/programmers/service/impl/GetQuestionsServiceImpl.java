package com.programmers.service.impl;

import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.beans.ExamBean;
import com.programmers.model.beans.OptionBean;
import com.programmers.model.beans.QuestionBean;
import com.programmers.model.beans.QuestionCategoryBean;
import com.programmers.model.beans.SectionBean;
import com.programmers.model.db.Exam;
import com.programmers.model.db.ExamDetail;
import com.programmers.model.db.Options;
import com.programmers.model.db.Question;
import com.programmers.model.db.QuestionCategory;
import com.programmers.model.db.Section;
import com.programmers.model.db.User;
import com.programmers.repository.ExamRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.repository.UserRepository;
import com.programmers.service.GetQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GetQuestionsServiceImpl implements GetQuestionsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ExamRepository examRepository;

    @Autowired
    public QuestionRepository questionRepository;

    @Override
    public ExamBean getQuestions(String examId, String userId) {
        return getExamBean(examId, userId);
    }

    private ExamBean getExamBean(String examId, String userId) {
        final ExamBean examBean = new ExamBean();
        Exam exam = new Exam();
        final Optional<Exam> examById = examRepository.findById(Long.parseLong(examId));
        // Exam
        if (examById.isPresent()) {
            exam = examById.get();
        }

        examBean.setExamName(exam.getExamName());
        examBean.setExamDuration(String.valueOf(exam.getExamDuration()));

        User user = new User();
        final Optional<User> userById = userRepository.findById(Long.parseLong(userId));
        if (userById.isPresent()) {
            user = userById.get();
        }
        // user
        final String name = user.getFirstName() + " " + user.getLastName();
        examBean.setUserName(name);
        // Exam Detail
        final Set<ExamDetail> examDetailList = exam.getExamDetailSet();
        final Map<Long, QuestionCategoryBean> questionCategoryMap = new HashMap<>(0);

        for (ExamDetail examDetail : examDetailList) {
            final QuestionCategory questionCategory = examDetail.getQuestionCategory();
            final Long questionCategoryId = questionCategory.getId();
            final Section section = examDetail.getSection();
            final Long sectionId = section.getId();
            final int questionLimit = examDetail.getNumberOfQuestions();
            // get questions for section and QuestionCategory
            final List<Question> questions = questionRepository.findRandomQuestions(questionCategoryId, sectionId,
                    questionLimit);
            if (questions.size() != questionLimit) {
                throw new InvalidEntityException("Insert more Questions of " + section.getMarksPerQuestion()
                        + " marks in Database for Question Category - " + questionCategory.getQuestionCategoryName());
            }

            final List<QuestionBean> questionBeanList = new ArrayList<>();
            for (Question question : questions) {
                final QuestionBean questionBean = new QuestionBean();
                final Set<Options> options = question.getOptions();
                final List<OptionBean> optionBeanList = new ArrayList<>();
                for (Options option : options) {
                    final OptionBean optionBean = new OptionBean();
                    optionBean.setId(String.valueOf(option.getId()));
                    optionBean.setText(option.getOptionText());
                    optionBeanList.add(optionBean);
                }
                questionBean.setId(String.valueOf(question.getId()));
                questionBean.setText(question.getQuestionText());
                questionBean.setOptions(optionBeanList);
                questionBeanList.add(questionBean);
            }

            // sectionBean
            final SectionBean sectionBean = new SectionBean();
            sectionBean.setExamDetailId(String.valueOf(examDetail.getId()));
            sectionBean.setMarksPerQuestion(String.valueOf(section.getMarksPerQuestion()));
            sectionBean.setNoOfQuestions(String.valueOf(questionLimit));
            sectionBean.setQuestions(questionBeanList);

            QuestionCategoryBean questionCategoryBean = null;

            // If QuestionCategoryBean exist already then fetch that otherwise
            // create one
            if (questionCategoryMap.get(questionCategoryId) != null) {
                questionCategoryBean = questionCategoryMap.get(questionCategoryId);
                questionCategoryBean.getSections().add(sectionBean);
            } else {
                questionCategoryBean = new QuestionCategoryBean();
                final List<SectionBean> sbList = new ArrayList<>();
                sbList.add(sectionBean);
                questionCategoryBean.setSections(sbList);
                questionCategoryBean.setText(questionCategory.getQuestionCategoryName());
                questionCategoryMap.put(questionCategoryId, questionCategoryBean);
            }
        }

        final List<QuestionCategoryBean> questionCategoryList = new ArrayList<>(questionCategoryMap.values());
        examBean.setQuestionCategories(questionCategoryList);
        return examBean;
    }
}
