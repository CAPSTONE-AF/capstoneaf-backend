package upn.proj.sowad.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.constant.QuizConstant;
import upn.proj.sowad.constant.TemaImplConstant;
import upn.proj.sowad.dao.QuestionRepository;
import upn.proj.sowad.dao.QuizRepository;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.entities.Result;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.*;

import javax.annotation.Resource;
import javax.management.StringValueExp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static upn.proj.sowad.constant.TemaImplConstant.*;

@Service("quizServiceV1")
public class QuizServiceImpl implements QuizService {

    private Logger log = LoggerFactory.getLogger(getClass());
    public final static Integer NOTA_MAXIMA = 20;
    public final static Integer NUMERO_PREGUNTAS = 4;

    private QuizRepository quizRepository;

    @Resource(name = "utilityServiceV1")
    private UtilityService utilityService;

    private UserService userService;
    private TemaService temaService;

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository, UtilityService utilityService, UserService userService, TemaService temaService) {
        this.quizRepository = quizRepository;
        this.utilityService = utilityService;
        this.userService = userService;
        this.temaService = temaService;
    }

    @Override
    public QuizDto registerNewQuiz(QuizDto quizDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'registerNewQuiz' method");
        if (this.temaService.validateIDTema(quizDto.getIdTema()) && this.validateQuizDto(quizDto)) {
            Quiz savedQuiz = new Quiz();
            savedQuiz.setTitle(quizDto.getTitle());
            savedQuiz.setDescription(quizDto.getDescription());
            savedQuiz.setTema(this.temaService.getTemaById(String.valueOf(quizDto.getIdTema())));
            savedQuiz.setActive(true);
            savedQuiz.setMaxScore(NOTA_MAXIMA);
            savedQuiz.setNumberOfQuestions(NUMERO_PREGUNTAS);
            savedQuiz.setResults(new ArrayList<>());
            savedQuiz.setQuestions(new ArrayList<>());
            return this.convertQuizModelToQuizDto(this.quizRepository.save(savedQuiz));
        }
        return null;
    }

    private boolean validateQuizDto(QuizDto quizDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'validateQuizDto' method");
        if (quizDto.getTitle() == null)
            throw new UtilityException(QuizConstant.NOT_TITLE_FOUND);

        if (quizDto.getTitle() != null && quizDto.getTitle().isEmpty())
            throw new UtilityException(QuizConstant.NOT_TITLE_FOUND);

        if (quizDto.getDescription() == null)
            throw new UtilityException(QuizConstant.DESCRIPTION_NOT_FOUND);

        if (quizDto.getDescription() != null && quizDto.getDescription().isEmpty())
            throw new UtilityException(QuizConstant.DESCRIPTION_NOT_FOUND);

        return true;
    }


    @Override
    public List<QuizDto> getAllByTema(String idTema) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'getAllByTema' method");
        if (this.temaService.validateIDTema(idTema)) {
            List<Quiz> quizzesFound = this.quizRepository.findAllByTema(this.temaService.getTemaById(idTema));
            List<QuizDto> resp = new ArrayList<>();
            for (Quiz quizTmp : quizzesFound) {
                if (quizTmp.getActive())
                    resp.add(convertQuizModelToQuizDto(quizTmp));
            }
            return resp;
        }
        return Collections.EMPTY_LIST;
    }


    @Override
    public QuizDto getQuizById(String idQuiz) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'getQuizById' method");
        if (validateIDQuiz(idQuiz))
            return convertQuizModelToQuizDto(this.quizRepository.findByIdQuiz(Long.valueOf(idQuiz)));
        return null;
    }

    @Override
    public Quiz getQuizModelById(String idQuiz) throws UtilityException {
        return this.quizRepository.findByIdQuiz(Long.valueOf(idQuiz));
    }

    @Override
    public QuizDto updateQuiz(QuizDto quizDto) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'updateQuiz' method");
        if (validateIDQuiz(quizDto.getIdQuiz()) && this.validateQuizDto(quizDto)) {
            quizDto.setActive("true");
            quizDto.setMaxScore(String.valueOf(NOTA_MAXIMA));
            quizDto.setNumberOfQuestions(String.valueOf(NUMERO_PREGUNTAS));
            Quiz quizToUpdate = this.convertQuizDtoToQuizModel(quizDto);
            return this.convertQuizModelToQuizDto(this.quizRepository.save(quizToUpdate));
        }
        return null;
    }

    @Override
    public void deleteQuiz(String idQuiz) throws UtilityException {
        if (log.isInfoEnabled()) log.info("Entering 'deleteQuiz' method");
        if (validateIDQuiz(idQuiz)) {
            Quiz quizFound = this.quizRepository.findByIdQuiz(Long.valueOf(idQuiz));
            quizFound.setActive(false);
            this.quizRepository.save(quizFound);
        }
    }

    @Override
    public QuizDto convertQuizModelToQuizDto(Quiz quiz) {
        if (log.isInfoEnabled()) log.info("Entering 'convertQuizModelToQuizDto' method");
        QuizDto quizDto = new QuizDto();
        quizDto.setIdQuiz(quiz.getIdQuiz() != null ? String.valueOf(quiz.getIdQuiz()) : StringUtils.EMPTY);
        quizDto.setActive(quiz.getActive() != null ? String.valueOf(quiz.getActive()) : StringUtils.EMPTY);
        quizDto.setDescription(quiz.getDescription() != null ? quiz.getDescription() : StringUtils.EMPTY);
        quizDto.setTitle(quiz.getTitle() != null ? quiz.getTitle() : StringUtils.EMPTY);
        quizDto.setMaxScore(quiz.getMaxScore() != null ? String.valueOf(quiz.getMaxScore()) : StringUtils.EMPTY);
        quizDto.setIdTema(quiz.getTema() != null ? String.valueOf(quiz.getTema().getIdTema()) : StringUtils.EMPTY);
        quizDto.setNumberOfQuestions(quiz.getNumberOfQuestions() != null ? String.valueOf(quiz.getNumberOfQuestions()) : StringUtils.EMPTY);
        return quizDto;
    }

    @Override
    public Quiz convertQuizDtoToQuizModel(QuizDto quizDto) {
        if (log.isInfoEnabled()) log.info("Entering 'convertQuizDtoToQuizModel' method");
        Quiz quiz = new Quiz();
        if (quizDto.getIdTema() != null && !quizDto.getIdTema().isEmpty() && this.temaService.getTemaById(quizDto.getIdTema()) != null) {
            quiz.setTema(this.temaService.getTemaById(quizDto.getIdTema()));
        }
        quiz.setIdQuiz(quizDto.getIdQuiz() != null && !quizDto.getIdQuiz().isEmpty() ? Long.valueOf(quizDto.getIdQuiz()) : null);
        quiz.setActive(quizDto.getActive() != null && !quizDto.getActive().isEmpty() && (quizDto.getActive().equalsIgnoreCase("TRUE") || quizDto.getActive().equalsIgnoreCase("FALSE")) ? Boolean.valueOf(quizDto.getActive()) : null);
        quiz.setDescription(quizDto.getDescription() != null && !quizDto.getDescription().isEmpty() ? quizDto.getDescription() : StringUtils.EMPTY);
        quiz.setTitle(quizDto.getTitle() != null && !quizDto.getTitle().isEmpty() ? quizDto.getTitle() : StringUtils.EMPTY);
        quiz.setMaxScore(quizDto.getMaxScore() != null && !quizDto.getMaxScore().isEmpty() ? Integer.valueOf(quizDto.getMaxScore()) : null);
        quiz.setNumberOfQuestions(quizDto.getNumberOfQuestions() != null && !quizDto.getNumberOfQuestions().isEmpty() ? Integer.valueOf(quizDto.getNumberOfQuestions()) : null);
        return quiz;
    }



    @Override
    public boolean validateIDQuiz(String idQuiz) throws UtilityException {
        Long idQuizTmp = null;
        if (idQuiz == null)
            throw new UtilityException(QuizConstant.QUIZ_NOT_ENTERED);
        if (idQuiz != null && idQuiz.isEmpty())
            throw new UtilityException(QuizConstant.QUIZ_NOT_ENTERED);
        if (idQuiz != null && !idQuiz.isEmpty()) {
            try {
                idQuizTmp = Long.parseLong(idQuiz);
            } catch (NumberFormatException exception) {
                throw new UtilityException(QuizConstant.ID_QUIZ_IS_NOT_A_NUMBER);
            }
        }
        if (idQuizTmp != null && this.quizRepository.findByIdQuiz(idQuizTmp) == null)
            throw new UtilityException(QuizConstant.QUIZ_NOT_FOUND);
        return true;
    }

}
