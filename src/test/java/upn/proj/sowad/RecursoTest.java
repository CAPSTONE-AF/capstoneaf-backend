package upn.proj.sowad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import upn.proj.sowad.constant.RecursoImplConstant;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.CursoService;
import upn.proj.sowad.services.RecursoService;
import upn.proj.sowad.services.TemaService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static upn.proj.sowad.constant.CursoImplConstant.CURSO_HAS_NO_TITLE;
import static upn.proj.sowad.constant.CursoImplConstant.NO_CURSO_FOUND_BY_NOMBRE;
import static upn.proj.sowad.constant.TemaImplConstant.NO_TEMA_FOUND_BY_TITULO;

@SpringBootTest
public class RecursoTest {
    @Autowired
    private RecursoService recursoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private TemaService temaService;

    @Test
    public void listRecursos_whenHasNoCursoTitle() {
        String expectedMessage = CURSO_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.recursoService.getRecursos("", "");
        } catch (TemaNotFoundException | CursoNotFoundException | UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void registerRecurso_whenCursoTitleDoesNotExist() {
        String nombreCursoTest = "unknownCurso";
        String expectedMessage = NO_CURSO_FOUND_BY_NOMBRE + nombreCursoTest;
        String realMessage = "";
        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) != null)
                this.cursoService.deleteCurso(nombreCursoTest);

            this.recursoService.addNewRecurso(nombreCursoTest, "", "", "", "");
        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException e) {
            realMessage = e.getMessage();
        }

        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void registerRecurso_whenTemaTitleDoesNotExist() {
        String nombreCursoTest = "unknownCurso";
        String nombreTemaTest = "unknownTema";
        String expectedMessage = NO_TEMA_FOUND_BY_TITULO + nombreTemaTest;
        String realMessage = "";

        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");


            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) != null)
                this.temaService.deleteTema(nombreCursoTest, nombreTemaTest);

            this.recursoService.addNewRecurso(nombreCursoTest, nombreTemaTest, "", "", "");
        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException | IOException e) {
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
    public void registerRecurso_whenHasNoTipo() {
        String nombreCursoTest = "ciencia2";
        String nombreTemaTest = "fotosintesis2";
        String expectedMessage = RecursoImplConstant.RECURSO_HAS_NO_TIPO;
        String realMessage = "";

        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");


            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            this.recursoService.addNewRecurso(nombreCursoTest, nombreTemaTest, "plantas", "", "bien");
        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException | IOException | NotAnImageFileException e) {
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
    public void registerRecurso_whenHasNoContenido() {
        String nombreCursoTest = "ciencia3";
        String nombreTemaTest = "fotosintesis3";
        String expectedMessage = RecursoImplConstant.RECURSO_HAS_NO_CONTENIDO;
        String realMessage = "";

        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");


            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            this.recursoService.addNewRecurso(nombreCursoTest, nombreTemaTest, "tallos", "txt", "");
        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException | IOException | NotAnImageFileException e) {
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
    public void registerRecurso_whenHasNoTitle() {
        String nombreCursoTest = "ciencia4";
        String nombreTemaTest = "fotosintesis4";
        String expectedMessage = RecursoImplConstant.RECURSO_HAS_NO_TITLE;
        String realMessage = "";

        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");


            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) == null)
                this.temaService.addNewTema(nombreCursoTest, nombreTemaTest, "", "1");

            this.recursoService.addNewRecurso(nombreCursoTest, nombreTemaTest, "", "txt", "bien");
        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException | IOException | NotAnImageFileException e) {
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
    public void updateRecurso_whenCursoDoesNotExist() {
        String nombreCursoTest = "matematicasss";
        String nombreTemaTest = "sumas";
        String nombreRecursoTest = "mate";
        String expectedMessage = NO_CURSO_FOUND_BY_NOMBRE + nombreCursoTest;
        String realMessage = "";

        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) != null)
                this.cursoService.deleteCurso(nombreCursoTest);

            this.recursoService.addNewRecurso(nombreCursoTest, nombreTemaTest, nombreRecursoTest, "txt", "bien");

        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage, realMessage);
    }

    @Test
    public void updateRecurso_whenTemaDoesNotExist() {
        String nombreCursoTest = "matematicasss";
        String nombreTemaTest = "sumas";
        String nombreRecursoTest = "mate";
        String expectedMessage = NO_TEMA_FOUND_BY_TITULO + nombreTemaTest;
        String realMessage = "";

        try {

            if (this.cursoService.findCursoByNombre(nombreCursoTest) == null)
                this.cursoService.addNewCurso(nombreCursoTest, "1");

            if (this.temaService.findTemaByTitulo(nombreCursoTest, nombreTemaTest) != null)
                this.temaService.deleteTema(nombreCursoTest, nombreTemaTest);

            this.recursoService.addNewRecurso(nombreCursoTest, nombreTemaTest, nombreRecursoTest, "txt", "bien");
        } catch (TemaNotFoundException | CursoNotFoundException | CursoExistsException | TemaExistsException | RecursoNotFoundException | RecursoExistsException | UtilityException | IOException e) {
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
