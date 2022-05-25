package upn.proj.sowad.services;

import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.exception.domain.UtilityException;

import java.util.List;

public interface QuizService {

    QuizDto registerNewQuiz(QuizDto quizDto) throws UtilityException;

    List<QuizDto> getAllByTema(String idTema) throws UtilityException;

    QuizDto getQuizById(String idQuiz) throws UtilityException;

    Quiz getQuizModelById(String idQuiz) throws UtilityException;

    QuizDto updateQuiz(QuizDto quizDto) throws UtilityException;

    void deleteQuiz(String idQuiz) throws UtilityException;

    boolean validateIDQuiz(String idQuiz) throws UtilityException;

    public QuizDto convertQuizModelToQuizDto(Quiz quiz);

    public Quiz convertQuizDtoToQuizModel(QuizDto quizDto);

}
