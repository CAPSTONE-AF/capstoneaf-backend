package upn.proj.sowad.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.constant.QuestionConstant;
import upn.proj.sowad.constant.QuizConstant;
import upn.proj.sowad.constant.ResultConstant;
import upn.proj.sowad.dao.QuizRepository;
import upn.proj.sowad.dao.ResultRepository;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.entities.*;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.QuizService;
import upn.proj.sowad.services.ResultService;
import upn.proj.sowad.services.UserService;
import upn.proj.sowad.services.UtilityService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("resultServiceV1")
public class ResultServiceImpl implements ResultService {

    public static final double PUNTAJE_POR_PREGUNTA = 5;

    private Logger log = LoggerFactory.getLogger(getClass());

    private ResultRepository resultRepository;

    @Resource(name = "utilityServiceV1")
    private UtilityService utilityService;

    private UserService userService;

    @Resource(name = "quizServiceV1")
    private QuizService quizService;



    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository, UtilityService utilityService, UserService userService, QuizService quizService) {
        this.resultRepository = resultRepository;
        this.utilityService = utilityService;
        this.userService = userService;
        this.quizService = quizService;
    }

    @Override
    public Result findByIdResult(String idResult) {
        return this.resultRepository.findByIdResultado(Long.valueOf(idResult));
    }

    @Override
    public ResultDto createNewResult(ResultDto resultDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'createNewResult' method");
        if (this.userService.validateIdUser(resultDto.getIdUser()) &&
                this.quizService.validateIDQuiz(resultDto.getIdQuiz())) {
            Result result = new Result();
            result.setUser(this.userService.findByID(resultDto.getIdUser()));
            result.setQuiz(this.quizService.getQuizModelById(resultDto.getIdQuiz()));
            result.setAnswers(new ArrayList<>());
            return this.convertResultModelToResultDto(this.resultRepository.save(result));
        }
        return null;
    }

    private ResultDto convertResultModelToResultDto(Result result) {
        if (log.isInfoEnabled()) log.info("Entering 'convertResultModelToResultDto' method");
        if (result != null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setIdResultado(result.getIdResultado() != null ? String.valueOf(result.getIdResultado()) : StringUtils.EMPTY);
            resultDto.setIdUser(result.getUser() != null ? String.valueOf(result.getUser().getId()) : StringUtils.EMPTY);
            resultDto.setIdQuiz(result.getQuiz() != null ? String.valueOf(result.getQuiz().getIdQuiz()) : StringUtils.EMPTY);
            resultDto.setResultScore(result.getResultScore() != null ? String.valueOf(result.getResultScore()) : StringUtils.EMPTY);
            resultDto.setNumCorrectAns(result.getNumCorrectAns() != null ? String.valueOf(result.getNumCorrectAns()) : StringUtils.EMPTY);
            resultDto.setNumIncorrectAns(result.getNumIncorrectAns() != null ? String.valueOf(result.getNumIncorrectAns()) : StringUtils.EMPTY);
            resultDto.setSubmitDate(result.getSubmitDate() != null ? String.valueOf(result.getSubmitDate()) : StringUtils.EMPTY);
            return resultDto;
        }
        return null;
    }


    @Override
    public boolean validateIdResult(String idResult) throws UtilityException {
        Long idResultTemp = null;
        if (idResult == null)
            throw new UtilityException(ResultConstant.RESULT_NOT_ENTERED);
        if (idResult != null && idResult.isEmpty())
            throw new UtilityException(ResultConstant.RESULT_NOT_ENTERED);
        if (idResult != null && !idResult.isEmpty()) {
            try {
                idResultTemp = Long.parseLong(idResult);
            } catch (NumberFormatException exception) {
                throw new UtilityException(ResultConstant.ID_RESULT_IS_NOT_A_NUMBER);
            }
        }
        if (idResultTemp != null && this.resultRepository.findByIdResultado(idResultTemp) == null)
            throw new UtilityException(ResultConstant.RESULT_NOT_FOUND);
        return true;
    }

    @Override
    public ResultDto getResultByIdUserAndIdQuiz(String idUser, String idQuiz) throws UtilityException {
        if (this.userService.validateIdUser(idUser) &&
                this.quizService.validateIDQuiz(idQuiz)) {
            User userFound = this.userService.findByID(idUser);
            Quiz quizFound = this.quizService.getQuizModelById(idQuiz);
            return this.convertResultModelToResultDto(this.resultRepository.findByUserAndQuiz(userFound, quizFound));
        }
        return null;
    }

    @Override
    public Result getResultModelByIdUserAndIdQuiz(String idUser, String idQuiz) throws UtilityException {
        if (this.userService.validateIdUser(idUser) &&
                this.quizService.validateIDQuiz(idQuiz)) {
            User userFound = this.userService.findByID(idUser);
            Quiz quizFound = this.quizService.getQuizModelById(idQuiz);
            return this.resultRepository.findByUserAndQuiz(userFound, quizFound);
        }
        return null;
    }

    @Override
    public ResultDto gradeQuiz(String idResult) throws UtilityException {
        Result result = this.findByIdResult(idResult);
        if (result != null) {
            List<Answer> answers = result.getAnswers();
            Double scoreObtenido = 0.0;
            Integer numCorrectAns = 0;
            Integer numIncorrectAns = 0;
            if (answers.size() == 4) {
                for (Answer answer : answers){
                    Question question = answer.getQuestion();
                    if(question.getAnswer().equals(answer.getAns())){
                        scoreObtenido += PUNTAJE_POR_PREGUNTA;
                        numCorrectAns += 1;
                    }else
                        numIncorrectAns += 1;
                }
            }
            result.setResultScore(scoreObtenido);
            result.setNumCorrectAns(numCorrectAns);
            result.setNumIncorrectAns(numIncorrectAns);
            result.setSubmitDate(new Date());
            return this.convertResultModelToResultDto(this.resultRepository.save(result));
        } else
            throw new UtilityException(QuizConstant.QUIZ_NOT_SUBMITED_YET);
    }

    @Override
    public List<ResultDto> findaAllByUserId(String idUser) throws UtilityException {

        if(this.userService.validateIdUser(idUser)){

            List<ResultDto> response = new ArrayList<>();

            User userFound = this.userService.findByID(idUser);
            List<Result> resultsByUserId = this.resultRepository.findAllByUserOrderBySubmitDateDesc(userFound);

            for(Result resultTemp : resultsByUserId){
                if(resultTemp.getResultScore()!=null)
                    response.add(this.convertResultModelToResultDto(resultTemp));
            }

            return response;

        }

        return null;

    }

}
