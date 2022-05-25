package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.proj.sowad.dto.AnswerDto;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.AnswerService;
import upn.proj.sowad.services.QuestionService;

import javax.annotation.Resource;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Resource(name = "answerServiceV1")
    private AnswerService answerService;

    private Logger log = LoggerFactory.getLogger(AvanceController.class.getName());

    @PostMapping("/register")
    public ResponseEntity<List<AnswerDto>> registerAnswer(@RequestBody List<AnswerDto> answersDto) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'registerAnswer' method");
        return new ResponseEntity<List<AnswerDto>>(this.answerService.registerAnswers(answersDto), OK);
    }



}
