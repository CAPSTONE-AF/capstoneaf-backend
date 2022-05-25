package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.proj.sowad.dto.AnswerDto;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.entities.Result;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.QuizService;
import upn.proj.sowad.services.ResultService;

import javax.annotation.Resource;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/result")
public class ResultController {

    @Resource(name = "resultServiceV1")
    private ResultService resultService;

    private Logger log = LoggerFactory.getLogger(AvanceController.class.getName());

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResultDto> createNewResult(@RequestBody ResultDto resultDto) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'createNewResult' method");
        return new ResponseEntity<>(this.resultService.createNewResult(resultDto), OK);
    }

    @GetMapping("/find/{idUser}/{idQuiz}")
    public ResponseEntity<ResultDto> getResultByIdUserAndIdQuiz(@PathVariable("idUser") String idUser, @PathVariable("idQuiz") String idQuiz) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'getResultByIdUserAndIdQuiz' method");
        ResultDto resultDto = this.resultService.getResultByIdUserAndIdQuiz(idUser,idQuiz);
        return new ResponseEntity<>(resultDto, OK);
    }

    @PostMapping("/grade/{idUser}/{idQuiz}")
    public ResponseEntity<ResultDto> gradeQuiz(@PathVariable("idUser") String idUser, @PathVariable("idQuiz") String idQuiz) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'gradeQuiz' method");
        return new ResponseEntity<>(this.resultService.gradeQuiz(idUser,idQuiz), OK);
    }

}
