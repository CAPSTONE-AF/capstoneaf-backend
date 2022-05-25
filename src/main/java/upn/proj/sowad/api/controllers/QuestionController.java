package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.entities.HttpResponse;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.AvanceService;
import upn.proj.sowad.services.QuestionService;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/question")
public class QuestionController {

    public static final String QUESTION_DELETED_SUCCESSFULLY = "Pregunta eliminada.";

    @Resource(name = "questionServiceV1")
    private QuestionService questionService;

    private Logger log = LoggerFactory.getLogger(AvanceController.class.getName());

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/register")
    public ResponseEntity<QuestionDto> registerQuestion(@RequestBody QuestionDto questionDto) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'registerQuestion' method");
        return new ResponseEntity<>(this.questionService.registerQuestion(questionDto), OK);
    }

    @GetMapping("/list/{idQuiz}")
    public ResponseEntity<List<QuestionDto>> getAllQuestionByQuiz(@PathVariable("idQuiz") String idQuiz) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'getAllQuestionByQuiz' method");
        List<QuestionDto> questionsDto = this.questionService.getAllByQuiz(idQuiz);
        return new ResponseEntity<>(questionsDto, OK);
    }

    @GetMapping("/find/{idQuestion}")
    public ResponseEntity<QuestionDto> getByIdQuestion(@PathVariable("idQuestion") String idQuestion) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'getByIdQuestion' method");
        QuestionDto questionDto = this.questionService.getQuestionById(idQuestion);
        return new ResponseEntity<>(questionDto, OK);
    }

    @PutMapping("/update")
    public ResponseEntity<QuestionDto> updateQuestion(@RequestBody QuestionDto questionDto) throws UtilityException {
        QuestionDto questionUpdated = this.questionService.updateQuestion(questionDto);
        return new ResponseEntity<>(questionUpdated, OK);
    }

    @DeleteMapping("/delete/{idQuestion}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteQuestion(@PathVariable("idQuestion") String idQuestion) throws UtilityException {
        this.questionService.deleteQuestion(idQuestion);
        return response(OK, QUESTION_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }

}
