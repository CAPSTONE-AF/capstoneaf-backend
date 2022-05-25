package upn.proj.sowad.services;

import upn.proj.sowad.dto.AnswerDto;
import upn.proj.sowad.entities.Answer;
import upn.proj.sowad.entities.Result;
import upn.proj.sowad.exception.domain.UtilityException;

import java.util.List;

public interface AnswerService {
    List<AnswerDto> registerAnswers(List<AnswerDto> answersDto) throws UtilityException;

    boolean validateIdAnswer(String idAnswer) throws UtilityException;

    List<Answer> findAllByResult(Result result);
}
