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
        String nombreCursoTest = "matematicasss";
        String nombreTemaTest = "sumas";
        String expectedMessage = TEMA_NOT_FOUND;
        String realMessage = "";
        try {
            QuizDto quizDto = new QuizDto();

            Tema temaFound = new Tema();

            quizDto.setTitle("Examen 1");
            quizDto.setDescription("Unit Test");

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                temaFound = this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");
            else {
                this.temaService.deleteTema(nombreCursoTest, nombreTemaTest);
                temaFound = this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");
            }

            quizDto.setIdTema(temaFound.getIdTema().toString());

            this.quizService.registerNewQuiz(quizDto);

        } catch (CursoExistsException | CursoNotFoundException | UtilityException | TemaNotFoundException | IOException | NotAnImageFileException | TemaExistsException e) {
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
