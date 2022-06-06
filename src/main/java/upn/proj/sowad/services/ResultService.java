package upn.proj.sowad.services;

import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.entities.Result;
import upn.proj.sowad.exception.domain.UtilityException;

import java.util.List;

public interface ResultService {

    Result findByIdResult (String idResult);

    ResultDto createNewResult(ResultDto resultDto) throws UtilityException;

    boolean validateIdResult(String idResult) throws UtilityException;

    ResultDto getResultByIdUserAndIdQuiz(String idUser, String idQuiz) throws UtilityException;

    Result getResultModelByIdUserAndIdQuiz(String idUser, String idQuiz) throws UtilityException;

    ResultDto gradeQuiz(String idResult) throws UtilityException;

    List<ResultDto> findaAllByUserId(String idUser) throws UtilityException;
}
