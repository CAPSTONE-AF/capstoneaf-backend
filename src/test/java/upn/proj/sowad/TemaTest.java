package upn.proj.sowad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import upn.proj.sowad.constant.TemaImplConstant;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.CursoService;
import upn.proj.sowad.services.TemaService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static upn.proj.sowad.constant.CursoImplConstant.CURSO_HAS_NO_TITLE;

@SpringBootTest
public class TemaTest {
    @Autowired
    private TemaService temaService;

    @Test
    public void registerTema_whenHasNoTitle() {
        String expectedMessage = TemaImplConstant.TEMA_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.temaService.addNewTema("","","","1");
        } catch (UtilityException | IOException | NotAnImageFileException | CursoNotFoundException | CursoExistsException | TemaNotFoundException | TemaExistsException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage,realMessage);
    }

    @Test
    public void updateTema_whenHasNoTitle() {
        String expectedMessage = TemaImplConstant.TEMA_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.temaService.updateTema("","","","","1");
        } catch (CursoExistsException | CursoNotFoundException | UtilityException | TemaNotFoundException | TemaExistsException | IOException | NotAnImageFileException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage,realMessage);
    }


    @Test
    public void deleteTema_whenHasNoTitle() {
        String expectedMessage = TemaImplConstant.TEMA_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.temaService.deleteTema("","");
        } catch (UtilityException | IOException | CursoNotFoundException | CursoExistsException | TemaNotFoundException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage,realMessage);
    }

}
