package upn.proj.sowad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.CursoService;
import upn.proj.sowad.services.QuizService;
import upn.proj.sowad.services.TemaService;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static upn.proj.sowad.constant.CursoImplConstant.CURSO_HAS_NO_TITLE;
import static upn.proj.sowad.constant.TemaImplConstant.TEMA_NOT_FOUND;

@SpringBootTest
public class QuizTest {
    @Autowired
    private QuizService quizService;

    @Autowired
    private TemaService temaService;

    @Autowired
    private CursoService cursoService;

    @Test
    public void registerExamen_whenTemaDoesNotExist() {
        String expectedMessage = TEMA_NOT_FOUND;
        String realMessage = "";
        try {
            QuizDto quizDto = new QuizDto();

            quizDto.setTitle("nuevo");
            quizDto.setDescription("examen1");

            quizDto.setIdTema("999999999"); //este idTema no existe

            this.quizService.registerNewQuiz(quizDto);

        } catch (UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage, realMessage);
    }
        @Test
        public void registerExamen_whenTitleDoesNotExist() throws UtilityException, CursoExistsException, CursoNotFoundException, TemaNotFoundException, TemaExistsException, IOException, NotAnImageFileException {
            String nombreCursoTest = "unknownCurso";
            String nombreTemaTest = "unknownTema";
            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");
            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");
            QuizDto quizDto = new QuizDto();
            quizDto.setTitle("");
            quizDto.setDescription("examen1");
            quizDto.setIdTema(this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest).getIdTema().toString());

            //String expectedMessage = CURSO_HAS_NO_TITLE;
           //String realMessage = "";
            //try {
              //  QuizDto quizDto = new QuizDto();
                //quizDto.setTitle("");
                //this.quizService.registerNewQuiz(quizDto);

            //} catch ( UtilityException e) {
              //  realMessage = e.getMessage();
           // }
          //  assertEquals(expectedMessage, realMessage);

        }
    }





