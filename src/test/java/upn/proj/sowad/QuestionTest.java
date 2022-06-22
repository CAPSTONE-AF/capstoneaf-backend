package upn.proj.sowad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import upn.proj.sowad.constant.QuestionConstant;
import upn.proj.sowad.constant.QuizConstant;
import upn.proj.sowad.dto.QuestionDto;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.CursoService;
import upn.proj.sowad.services.QuestionService;
import upn.proj.sowad.services.QuizService;
import upn.proj.sowad.services.TemaService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class QuestionTest {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private TemaService temaService;

    @Autowired
    private QuizService quizService;

    @Test
    public void registerQuestion_whenHasInvalidQuizID() {

        String expectedMessage = QuizConstant.QUIZ_NOT_FOUND;
        String realMessage = "";
        try {

            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz("999999999");
            questionDto.setContent("¿Cuánto es 2 - 2?");
            questionDto.setOption1("1");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("OP3");

            this.questionService.registerQuestion(questionDto);
        } catch (UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage, realMessage);
    }


    @Test
    public void registerQuestion_whenHasNoEnunciado() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = QuestionConstant.NOT_CONTENT_FOUND;
        String realMessage = "";
        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe


            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(this.quizService.registerNewQuiz(quizDto).getIdQuiz());
            questionDto.setContent("");
            questionDto.setOption1("1");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("OP3");

            this.questionService.registerQuestion(questionDto);
        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException e) {
            realMessage = e.getMessage();
        }

        try {
            this.cursoService.deleteCurso(nombreCursoTest);
        } catch (UtilityException e) {
            e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void registerQuestion_whenHasEmptyOptions() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = QuestionConstant.NOT_OPTION1_FOUND;
        String realMessage = "";
        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe


            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(this.quizService.registerNewQuiz(quizDto).getIdQuiz());
            questionDto.setContent("¿Cuánto es 2 - 2?");
            questionDto.setOption1("");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("OP3");

            this.questionService.registerQuestion(questionDto);
        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException e) {
            realMessage = e.getMessage();
        }

        try {
            this.cursoService.deleteCurso(nombreCursoTest);
        } catch (UtilityException e) {
            e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void registerQuestion_whenHasEmptyAnswer() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = QuestionConstant.NOT_ANSWER_FOUND;
        String realMessage = "";
        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe


            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(this.quizService.registerNewQuiz(quizDto).getIdQuiz());
            questionDto.setContent("¿Cuánto es 2 - 2?");
            questionDto.setOption1("1");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("");

            this.questionService.registerQuestion(questionDto);
        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException e) {
            realMessage = e.getMessage();
        }

        try {
            this.cursoService.deleteCurso(nombreCursoTest);
        } catch (UtilityException e) {
            e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void registerQuestion_whenHasInvalidQuiz() {
        String expectedMessage = QuizConstant.QUIZ_NOT_FOUND;
        String realMessage = "";
        try {

            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz("999999999");
            questionDto.setContent("¿Cuánto es 2 - 2?");
            questionDto.setOption1("1");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("");

            this.questionService.registerQuestion(questionDto);
        } catch (UtilityException e) {
            realMessage = e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void getAllQuestions_whenPassInvalidQuizId() {
        String expectedMessage = QuizConstant.QUIZ_NOT_FOUND;
        String realMessage = "";
        try {
            this.questionService.getAllByQuiz("999999999");
        } catch (UtilityException e) {
            realMessage = e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void updateQuestion_whenPassInvalidQuizId() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = QuestionConstant.QUESTION_NOT_FOUND;
        String realMessage = "";
        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString()); //este idTema no existe


            QuestionDto questionDto = new QuestionDto();
            questionDto.setIdQuiz(this.quizService.registerNewQuiz(quizDto).getIdQuiz());
            questionDto.setContent("¿Cuánto es 2 - 2?");
            questionDto.setOption1("1");
            questionDto.setOption2("3");
            questionDto.setOption3("0");
            questionDto.setOption4("1");
            questionDto.setAnswer("");
            questionDto.setIdQuestion("999999999");
            this.questionService.updateQuestion(questionDto);
        } catch (UtilityException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException e) {
            realMessage = e.getMessage();
        }
        try {
            this.cursoService.deleteCurso(nombreCursoTest);
        } catch (UtilityException e) {
            e.getMessage();
        }
        assertEquals(expectedMessage, realMessage);
    }




}
