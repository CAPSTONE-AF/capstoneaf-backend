package upn.proj.sowad.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.constant.QuestionConstant;
import upn.proj.sowad.constant.QuizConstant;
import upn.proj.sowad.constant.TemaImplConstant;
import upn.proj.sowad.dao.QuestionRepository;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.entities.Question;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.QuestionService;
import upn.proj.sowad.services.QuizService;
import upn.proj.sowad.services.UtilityService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("questionServiceV1")
public class QuestionServiceImpl implements QuestionService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private QuestionRepository questionRepository;

    @Resource(name = "utilityServiceV1")
    private UtilityService utilityService;

    @Resource(name = "quizServiceV1")
    private QuizService quizService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, UtilityService utilityService, QuizService quizService) {
        this.questionRepository = questionRepository;
        this.utilityService = utilityService;
        this.quizService = quizService;
    }

    @Override
    public QuestionDto registerQuestion(QuestionDto questionDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'registerQuestion' method");
        if (this.quizService.validateIDQuiz(questionDto.getIdQuiz()) && this.validateNumberMaxOfQuestions(questionDto.getIdQuiz()) && this.validateQuestionDto(questionDto)) {
            Question savedQuestion = new Question();
            savedQuestion.setAnswer(questionDto.getAnswer());
            savedQuestion.setContent(questionDto.getContent());
            savedQuestion.setOption1(questionDto.getOption1());
            savedQuestion.setOption2(questionDto.getOption2());
            savedQuestion.setOption3(questionDto.getOption3());
            savedQuestion.setOption4(questionDto.getOption4());
            savedQuestion.setQuiz(this.quizService.getQuizModelById(questionDto.getIdQuiz()));
            return this.convertQuestionModelToQuestionDto(questionRepository.save(savedQuestion));
        }
        return null;
    }

    private boolean validateNumberMaxOfQuestions(String idQuiz) throws UtilityException {
        Quiz quizFound = this.quizService.convertQuizDtoToQuizModel(this.quizService.getQuizById(idQuiz));
        List<Question> questionsOfQuizFound = this.questionRepository.findAllByQuiz(quizFound);
        if(questionsOfQuizFound.size() < 4)
            return true;
        return false;
    }

    private boolean validateQuestionDto(QuestionDto questionDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'validateQuizDto' method");
        if (questionDto.getAnswer() == null)
            throw new UtilityException(QuestionConstant.NOT_ANSWER_FOUND);

        if (questionDto.getAnswer() != null && questionDto.getAnswer().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_ANSWER_FOUND);

        if (questionDto.getAnswer() != null && !questionDto.getAnswer().isEmpty() && !this.validateAnswer(questionDto.getAnswer()))
            throw new UtilityException(QuestionConstant.ANSWER_NOT_VALID);

        if (questionDto.getContent() == null)
            throw new UtilityException(QuestionConstant.NOT_CONTENT_FOUND);

        if (questionDto.getContent() != null && questionDto.getContent().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_CONTENT_FOUND);

        if (questionDto.getOption1() == null)
            throw new UtilityException(QuestionConstant.NOT_OPTION1_FOUND);

        if (questionDto.getOption1() != null && questionDto.getOption1().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_OPTION1_FOUND);

        if (questionDto.getOption2() == null)
            throw new UtilityException(QuestionConstant.NOT_OPTION2_FOUND);

        if (questionDto.getOption2() != null && questionDto.getOption2().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_OPTION2_FOUND);

        if (questionDto.getOption3() == null)
            throw new UtilityException(QuestionConstant.NOT_OPTION3_FOUND);

        if (questionDto.getOption3() != null && questionDto.getOption3().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_OPTION3_FOUND);

        if (questionDto.getOption4() == null)
            throw new UtilityException(QuestionConstant.NOT_OPTION4_FOUND);

        if (questionDto.getOption4() != null && questionDto.getOption4().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_OPTION4_FOUND);

        return true;
    }

    private boolean validateAnswer(String answer) {
        if (!answer.equals("OP1") &&
                !answer.equals("OP2") &&
                !answer.equals("OP3") &&
                !answer.equals("OP4"))
            return false;
        return true;
    }

    @Override
    public List<QuestionDto> getAllByQuiz(String idQuiz) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'getAllByQuiz' method");
        if (this.quizService.validateIDQuiz(idQuiz)) {
            List<Question> questionsFound = this.questionRepository.findAllByQuiz(this.quizService.convertQuizDtoToQuizModel(this.quizService.getQuizById(idQuiz)));
            List<QuestionDto> resp = new ArrayList<>();
            for (Question questionTmp : questionsFound) {
                resp.add(convertQuestionModelToQuestionDto(questionTmp));
            }
            return resp;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public QuestionDto getQuestionById(String idQuestion) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'getQuestionById' method");
        if (this.validateIdQuestion(idQuestion))
            return convertQuestionModelToQuestionDto(this.questionRepository.findByIdQuestion(Long.valueOf(idQuestion)));
        return null;
    }

    @Override
    public Question getQuestionModelById(String idQuestion) throws UtilityException {
        return this.questionRepository.findByIdQuestion(Long.valueOf(idQuestion));
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'updateQuestion' method");
        if (validateIdQuestion(questionDto.getIdQuestion()) && this.validateQuestionDto(questionDto)) {
            Question questionToUpdate = this.convertQuestionDtoToQuestionModel(questionDto);
            return this.convertQuestionModelToQuestionDto(this.questionRepository.save(questionToUpdate));
        }
        return null;
    }

    @Override
    public void deleteQuestion(String idQuestion) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'deleteQuestion' method");
        if (validateIdQuestion(idQuestion)) {
            this.questionRepository.deleteById(Long.valueOf(idQuestion));
        }
    }

    @Override
    public QuestionDto convertQuestionModelToQuestionDto(Question question) {
        if (log.isInfoEnabled()) log.info("Entering 'convertQuestionModelToQuestionDto' method");
        QuestionDto questionDto = new QuestionDto();
        questionDto.setIdQuestion(question.getIdQuestion() != null ? String.valueOf(question.getIdQuestion()) : StringUtils.EMPTY);
        questionDto.setIdQuiz(question.getQuiz() != null ? String.valueOf(question.getQuiz().getIdQuiz()) : StringUtils.EMPTY);
        questionDto.setContent(question.getContent() != null ? question.getContent() : StringUtils.EMPTY);
        questionDto.setOption1(question.getOption1() != null ? question.getOption1() : StringUtils.EMPTY);
        questionDto.setOption2(question.getOption2() != null ? question.getOption2() : StringUtils.EMPTY);
        questionDto.setOption3(question.getOption3() != null ? question.getOption3() : StringUtils.EMPTY);
        questionDto.setOption4(question.getOption4() != null ? question.getOption4() : StringUtils.EMPTY);
        questionDto.setAnswer(question.getAnswer() != null ? question.getAnswer() : StringUtils.EMPTY);
        return questionDto;
    }

    @Override
    public Question convertQuestionDtoToQuestionModel(QuestionDto questionDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'convertQuestionDtoToQuestionModel' method");
        Question question = new Question();
        if (questionDto.getIdQuiz() != null && !questionDto.getIdQuiz().isEmpty() && this.quizService.getQuizById(questionDto.getIdQuiz()) != null) {
            question.setQuiz(this.quizService.getQuizModelById(questionDto.getIdQuiz()));
        }
        question.setIdQuestion(questionDto.getIdQuestion() != null && !questionDto.getIdQuestion().isEmpty() ? Long.valueOf(questionDto.getIdQuestion()) : null);
        question.setContent(questionDto.getContent() != null && !questionDto.getContent().isEmpty() ? questionDto.getContent() : StringUtils.EMPTY);
        question.setAnswer(questionDto.getAnswer() != null && !questionDto.getAnswer().isEmpty() ? questionDto.getAnswer() : StringUtils.EMPTY);
        question.setOption1(questionDto.getOption1() != null && !questionDto.getOption1().isEmpty() ? questionDto.getOption1() : StringUtils.EMPTY);
        question.setOption2(questionDto.getOption2() != null && !questionDto.getOption2().isEmpty() ? questionDto.getOption2() : StringUtils.EMPTY);
        question.setOption3(questionDto.getOption3() != null && !questionDto.getOption3().isEmpty() ? questionDto.getOption3() : StringUtils.EMPTY);
        question.setOption4(questionDto.getOption4() != null && !questionDto.getOption4().isEmpty() ? questionDto.getOption4() : StringUtils.EMPTY);
        return question;
    }

    @Override
    public boolean validateIdQuestion(String idQuestion) throws UtilityException {
        Long idQuestionTmp = null;
        if (idQuestion == null)
            throw new UtilityException(QuestionConstant.QUESTION_NOT_ENTERED);
        if (idQuestion != null && idQuestion.isEmpty())
            throw new UtilityException(QuestionConstant.QUESTION_NOT_ENTERED);
        if (idQuestion != null && !idQuestion.isEmpty()) {
            try {
                idQuestionTmp = Long.parseLong(idQuestion);
            } catch (NumberFormatException exception) {
                throw new UtilityException(QuestionConstant.ID_QUESTION_IS_NOT_A_NUMBER);
            }
        }
        if(idQuestionTmp!=null && this.questionRepository.findByIdQuestion(idQuestionTmp) == null)
            throw new UtilityException(QuestionConstant.QUESTION_NOT_FOUND);
        return true;
    }


}
