package upn.proj.sowad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import upn.proj.sowad.constant.AnswerConstant;
import upn.proj.sowad.constant.QuestionConstant;
import upn.proj.sowad.dto.AnswerDto;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.dto.ResultDto;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AnswerTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private TemaService temaService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    @Test
    public void registerAnswer_whenHasNotEnoughAnswers() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = AnswerConstant.NOT_ENOUGH_RESPUESTAS;
        String realMessage = "";
        try {

            if(this.userService.findUserByUsername("johndoe")==null)
                this.userService.register("John", "Doe", "johndoe", "johndoe@gmail.com", "johndoe", null);

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe
            String idQuiz = this.quizService.registerNewQuiz(quizDto).getIdQuiz();

            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(idQuiz);
            questionDto.setContent("¿Cuánto es 2 + 2");
            questionDto.setOption1("4");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("OP1");
            String idQuestion1 = this.questionService.registerQuestion(questionDto).getIdQuestion();


            questionDto.setContent("¿Cuánto es 5 x 5");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("10");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP2");
            String idQuestion2 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            questionDto.setContent("¿Cuánto es 5 + 10");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("10");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP4");
            String idQuestion3 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            questionDto.setContent("¿Cuánto es 10 + 10");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("20");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP3");
            String idQuestion4 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            ResultDto resultDto = new ResultDto();
            resultDto.setIdUser(this.userService.findUserByUsername("johndoe").getId().toString());
            resultDto.setIdQuiz(idQuiz);

            String idResult = resultService.createNewResult(resultDto).getIdResultado();

            AnswerDto answerDto1 = new AnswerDto();
            answerDto1.setIdResult(idResult);
            answerDto1.setIdQuestion(idQuestion1);
            answerDto1.setAns("OP1"); // +5 puntos = 5 puntos

            AnswerDto answerDto2 = new AnswerDto();
            answerDto2.setIdResult(idResult);
            answerDto2.setIdQuestion(idQuestion2);
            answerDto2.setAns("OP2"); // +5 puntos = 10 puntos

            AnswerDto answerDto3 = new AnswerDto();
            answerDto3.setIdResult(idResult);
            answerDto3.setIdQuestion(idQuestion3);
            answerDto3.setAns("OP3"); // +0 puntos = 10 puntos

            AnswerDto answerDto4 = new AnswerDto();
            answerDto4.setIdResult(idResult);
            answerDto4.setIdQuestion(idQuestion4);
            answerDto4.setAns("OP4"); // +0 puntos = 10 puntos

            List<AnswerDto> answersDtoList = new ArrayList<>();
            answersDtoList.add(answerDto1);
            answersDtoList.add(answerDto2);
            answersDtoList.add(answerDto3);
            //answersDtoList.add(answerDto4);

            answerService.registerAnswers(answersDtoList);

        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException | UserNotFoundException | UsernameExistException | EmailExistException | MessagingException e) {
            realMessage = e.getMessage();
        }

        try {
            this.cursoService.deleteCurso(nombreCursoTest);
            this.userService.deleteUser("johndoe");
        } catch (UtilityException | IOException e) {
            e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void registerAnswer_whenHasInvalidQuestionID() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = QuestionConstant.QUESTION_NOT_FOUND;
        String realMessage = "";
        try {

            if(this.userService.findUserByUsername("johndoe")==null)
                this.userService.register("John", "Doe", "johndoe", "johndoe@gmail.com", "johndoe", null);

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe
            String idQuiz = this.quizService.registerNewQuiz(quizDto).getIdQuiz();

            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(idQuiz);
            questionDto.setContent("¿Cuánto es 2 + 2");
            questionDto.setOption1("4");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("OP1");
            String idQuestion1 = this.questionService.registerQuestion(questionDto).getIdQuestion();


            questionDto.setContent("¿Cuánto es 5 x 5");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("10");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP2");
            String idQuestion2 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            questionDto.setContent("¿Cuánto es 5 + 10");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("10");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP4");
            String idQuestion3 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            questionDto.setContent("¿Cuánto es 10 + 10");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("20");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP3");
            String idQuestion4 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            ResultDto resultDto = new ResultDto();
            resultDto.setIdUser(this.userService.findUserByUsername("johndoe").getId().toString());
            resultDto.setIdQuiz(idQuiz);

            String idResult = resultService.createNewResult(resultDto).getIdResultado();

            AnswerDto answerDto1 = new AnswerDto();
            answerDto1.setIdResult(idResult);
            answerDto1.setIdQuestion(idQuestion1);
            answerDto1.setAns("OP1"); // +5 puntos = 5 puntos

            AnswerDto answerDto2 = new AnswerDto();
            answerDto2.setIdResult(idResult);
            answerDto2.setIdQuestion(idQuestion2);
            answerDto2.setAns("OP2"); // +5 puntos = 10 puntos

            AnswerDto answerDto3 = new AnswerDto();
            answerDto3.setIdResult(idResult);
            answerDto3.setIdQuestion(idQuestion3);
            answerDto3.setAns("OP3"); // +0 puntos = 10 puntos

            AnswerDto answerDto4 = new AnswerDto();
            answerDto4.setIdResult(idResult);
            answerDto4.setIdQuestion("999999999");
            answerDto4.setAns("OP4"); // +0 puntos = 10 puntos

            List<AnswerDto> answersDtoList = new ArrayList<>();
            answersDtoList.add(answerDto1);
            answersDtoList.add(answerDto2);
            answersDtoList.add(answerDto3);
            answersDtoList.add(answerDto4);

            answerService.registerAnswers(answersDtoList);

        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException | UserNotFoundException | UsernameExistException | EmailExistException | MessagingException e) {
            realMessage = e.getMessage();
        }

        try {
            this.cursoService.deleteCurso(nombreCursoTest);
            this.userService.deleteUser("johndoe");
        } catch (UtilityException | IOException e) {
            e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void gradeQuiz_correctly() {
        String nombreCursoTest = "unknownCursos";
        String nombreTemaTest = "unknownTemas";
        String expectedMessage = "10.0";
        String realMessage = "";
        try {

            if(this.userService.findUserByUsername("johndoee")==null)
                this.userService.register("John", "Doe", "johndoee", "johndoee@gmail.com", "johndoe", null);

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe
            String idQuiz = this.quizService.registerNewQuiz(quizDto).getIdQuiz();

            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(idQuiz);
            questionDto.setContent("¿Cuánto es 2 + 2");
            questionDto.setOption1("4");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("OP1");
            String idQuestion1 = this.questionService.registerQuestion(questionDto).getIdQuestion();


            questionDto.setContent("¿Cuánto es 5 x 5");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("10");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP2");
            String idQuestion2 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            questionDto.setContent("¿Cuánto es 5 + 10");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("10");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP4");
            String idQuestion3 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            questionDto.setContent("¿Cuánto es 10 + 10");
            questionDto.setOption1("5");
            questionDto.setOption2("25");
            questionDto.setOption3("20");
            questionDto.setOption4("15");
            questionDto.setAnswer("OP3");
            String idQuestion4 = this.questionService.registerQuestion(questionDto).getIdQuestion();

            ResultDto resultDto = new ResultDto();
            resultDto.setIdUser(this.userService.findUserByUsername("johndoee").getId().toString());
            resultDto.setIdQuiz(idQuiz);

            String idResult = resultService.createNewResult(resultDto).getIdResultado();

            AnswerDto answerDto1 = new AnswerDto();
            answerDto1.setIdResult(idResult);
            answerDto1.setIdQuestion(idQuestion1);
            answerDto1.setAns("OP1"); // +5 puntos = 5 puntos

            AnswerDto answerDto2 = new AnswerDto();
            answerDto2.setIdResult(idResult);
            answerDto2.setIdQuestion(idQuestion2);
            answerDto2.setAns("OP2"); // +5 puntos = 10 puntos

            AnswerDto answerDto3 = new AnswerDto();
            answerDto3.setIdResult(idResult);
            answerDto3.setIdQuestion(idQuestion3);
            answerDto3.setAns("OP3"); // +0 puntos = 10 puntos

            AnswerDto answerDto4 = new AnswerDto();
            answerDto4.setIdResult(idResult);
            answerDto4.setIdQuestion(idQuestion4);
            answerDto4.setAns("OP4"); // +0 puntos = 10 puntos

            List<AnswerDto> answersDtoList = new ArrayList<>();
            answersDtoList.add(answerDto1);
            answersDtoList.add(answerDto2);
            answersDtoList.add(answerDto3);
            answersDtoList.add(answerDto4);

            answerService.registerAnswers(answersDtoList);

            realMessage = resultService.gradeQuiz(idResult).getResultScore();

        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException | UserNotFoundException | UsernameExistException | EmailExistException | MessagingException e) {
            realMessage = e.getMessage();
        }

        try {
            this.cursoService.deleteCurso(nombreCursoTest);
            this.userService.deleteUser("johndoee");
        } catch (UtilityException | IOException e) {
            e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }


}
