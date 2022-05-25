package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upn.proj.sowad.dto.AnswerDto;
import upn.proj.sowad.dto.AvanceDto;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.HttpResponse;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.QuestionService;
import upn.proj.sowad.services.QuizService;

import javax.annotation.Resource;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    public static final String QUIZ_DELETED_SUCCESSFULLY = "Examen eliminado.";

    @Resource(name = "quizServiceV1")
    private QuizService quizService;

    private Logger log = LoggerFactory.getLogger(AvanceController.class.getName());

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/register")
    public ResponseEntity<QuizDto> registerNewQuiz(@RequestBody QuizDto quizDto) throws UtilityException {
        return new ResponseEntity<>(this.quizService.registerNewQuiz(quizDto), OK);
    }

    @GetMapping("/list/{idTema}")
    public ResponseEntity<List<QuizDto>> getAllQuizzesByTema(@PathVariable("idTema") String idTema) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'getAllQuizzesByTema' method");
        List<QuizDto> quizzesDto = this.quizService.getAllByTema(idTema);
        return new ResponseEntity<>(quizzesDto, OK);
    }

    @GetMapping("/find/{idQuiz}")
    public ResponseEntity<QuizDto> getByIdQuiz(@PathVariable("idQuiz") String idQuiz) throws UtilityException {
        if(log.isInfoEnabled())
            log.info("Entering 'getByIdQuiz' method");
        QuizDto quizDto = this.quizService.getQuizById(idQuiz);
        return new ResponseEntity<>(quizDto, OK);
    }

    @PutMapping("/update")
    public ResponseEntity<QuizDto> updateQuiz(@RequestBody QuizDto quizDto) throws UtilityException {
        QuizDto quizUpdated = this.quizService.updateQuiz(quizDto);
        return new ResponseEntity<>(quizUpdated, OK);
    }

    @DeleteMapping("/delete/{idQuiz}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("idQuiz") String idQuiz) throws UtilityException {
        this.quizService.deleteQuiz(idQuiz);
        return response(OK, QUIZ_DELETED_SUCCESSFULLY);
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }

}
