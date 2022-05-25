package upn.proj.sowad.services;

import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.entities.Question;
import upn.proj.sowad.exception.domain.UtilityException;

import java.util.List;

public interface QuestionService {
    QuestionDto registerQuestion(QuestionDto questionDto) throws UtilityException;

    List<QuestionDto> getAllByQuiz(String idQuiz) throws UtilityException;

    QuestionDto getQuestionById(String idQuestion) throws UtilityException;

    Question getQuestionModelById(String idQuestion) throws UtilityException;

    QuestionDto updateQuestion(QuestionDto questionDto) throws UtilityException;

    void deleteQuestion(String idQuestion) throws UtilityException;

    QuestionDto convertQuestionModelToQuestionDto(Question question);

    Question convertQuestionDtoToQuestionModel(QuestionDto questionDto) throws UtilityException;

    boolean validateIdQuestion(String idQuestion) throws UtilityException;
}
