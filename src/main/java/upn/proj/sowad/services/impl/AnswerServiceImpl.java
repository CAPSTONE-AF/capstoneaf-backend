package upn.proj.sowad.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.constant.AnswerConstant;
import upn.proj.sowad.constant.QuestionConstant;
import upn.proj.sowad.dao.AnswerRepository;
import upn.proj.sowad.dto.AnswerDto;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.entities.Answer;
import upn.proj.sowad.entities.Question;
import upn.proj.sowad.entities.Result;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.AnswerService;
import upn.proj.sowad.services.QuestionService;
import upn.proj.sowad.services.ResultService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("answerServiceV1")
@Transactional
public class AnswerServiceImpl implements AnswerService {

    public final static Integer NUMERO_PREGUNTAS = 4;

    private Logger log = LoggerFactory.getLogger(getClass());

    private AnswerRepository answerRepository;

    @Resource(name = "resultServiceV1")
    private ResultService resultService;

    @Resource(name = "questionServiceV1")
    private QuestionService questionService;

    @Autowired
    public AnswerServiceImpl(ResultService resultService, QuestionService questionService, AnswerRepository answerRepository) {
        this.resultService = resultService;
        this.questionService = questionService;
        this.answerRepository = answerRepository;
    }

    @Override
    public List<AnswerDto> registerAnswers(List<AnswerDto> answersDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'registerAnswer' method");
        List<AnswerDto> savedAnswersDto = new ArrayList<>();
        if (answersDto.size() == NUMERO_PREGUNTAS) {
            for (AnswerDto answerDto : answersDto) {
                if (this.resultService.validateIdResult(answerDto.getIdResult()) &&
                        this.questionService.validateIdQuestion(answerDto.getIdQuestion()) &&
                        this.validateAnswerDto(answerDto)) {
                    Answer savedAnswer = new Answer();
                    savedAnswer.setAns(answerDto.getAns());
                    savedAnswer.setQuestion(this.questionService.getQuestionModelById(answerDto.getIdQuestion()));
                    savedAnswer.setResult(this.resultService.findByIdResult(answerDto.getIdResult()));
                    savedAnswersDto.add(this.convertAnswerModelToAnswerDto(this.answerRepository.save(savedAnswer)));
                }
            }
        }else
            throw new UtilityException(AnswerConstant.NOT_ENOUGH_RESPUESTAS);


        return savedAnswersDto;
    }

    private AnswerDto convertAnswerModelToAnswerDto(Answer answer) {
        if (log.isInfoEnabled()) log.info("Entering 'convertAnswerModelToAnswerDto' method");
        AnswerDto answerDto = new AnswerDto();
        answerDto.setIdAnswer(answer.getIdAnswer() != null ? String.valueOf(answer.getIdAnswer()) : StringUtils.EMPTY);
        answerDto.setIdQuestion(answer.getQuestion() != null ? String.valueOf(answer.getQuestion().getIdQuestion()) : StringUtils.EMPTY);
        answerDto.setIdResult(answer.getResult() != null ? String.valueOf(answer.getResult().getIdResultado()) : StringUtils.EMPTY);
        answerDto.setAns(answer.getAns() != null ? answer.getAns() : StringUtils.EMPTY);
        return answerDto;
    }

    private boolean validateAnswerDto(AnswerDto answerDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'validateAnswerDto' method");
        if (answerDto.getAns() == null)
            throw new UtilityException(QuestionConstant.NOT_ANSWER_FOUND);

        if (answerDto.getAns() != null && answerDto.getAns().isEmpty())
            throw new UtilityException(QuestionConstant.NOT_ANSWER_FOUND);

        if (answerDto.getAns() != null && !answerDto.getAns().isEmpty() && !this.validateAnswer(answerDto.getAns()))
            throw new UtilityException(QuestionConstant.ANSWER_NOT_VALID);

        Result resultFound = this.resultService.findByIdResult(answerDto.getIdResult());
        Question questionFound = this.questionService.getQuestionModelById(answerDto.getIdQuestion());

        if (!resultFound.getQuiz().equals(questionFound.getQuiz())) {
            throw new UtilityException(AnswerConstant.QUIZ_NOT_EQUAL);
        }

        if (this.answerRepository.findByResultAndQuestion(resultFound, questionFound) != null)
            throw new UtilityException(AnswerConstant.DUPLICATE_ANSWER);

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
    public boolean validateIdAnswer(String idAnswer) throws UtilityException {
        Long idAnswerTemp = null;
        if (idAnswer == null)
            throw new UtilityException(AnswerConstant.ANSWER_NOT_ENTERED);
        if (idAnswer != null && idAnswer.isEmpty())
            throw new UtilityException(AnswerConstant.ANSWER_NOT_ENTERED);
        if (idAnswer != null && !idAnswer.isEmpty()) {
            try {
                idAnswerTemp = Long.parseLong(idAnswer);
            } catch (NumberFormatException exception) {
                throw new UtilityException(AnswerConstant.ID_ANSWER_IS_NOT_A_NUMBER);
            }
        }
        if (idAnswerTemp != null && this.answerRepository.findByIdAnswer(idAnswerTemp) == null)
            throw new UtilityException(AnswerConstant.ANSWER_NOT_FOUND);
        return true;
    }

    @Override
    public List<Answer> findAllByResult(Result result) {
        return this.answerRepository.findAllByResult(result);
    }

}
