package upn.proj.sowad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.CursoService;
import upn.proj.sowad.services.UserService;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static upn.proj.sowad.constant.CursoImplConstant.CURSO_HAS_NO_TITLE;
import static upn.proj.sowad.constant.UserImplConstant.USERNAME_ALREADY_EXISTS;

@SpringBootTest
public class CursoTest {
    @Autowired
    private CursoService cursoService;

    @Test
    public void registerCurso_whenHasNoTitle() {
        String expectedMessage = CURSO_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.cursoService.addNewCurso("","1");
        } catch (CursoExistsException | CursoNotFoundException | UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage,realMessage);
    }

    @Test
    public void updateCurso_whenHasNoTitle() {
        String expectedMessage = CURSO_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.cursoService.updateCurso("","","1");
        } catch (CursoExistsException | CursoNotFoundException | UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage,realMessage);
    }

    @Test
    public void deleteCurso_whenHasNoTitle() {
        String expectedMessage = CURSO_HAS_NO_TITLE;
        String realMessage = "";
        try {
            this.cursoService.deleteCurso("");
        } catch (UtilityException e) {
            realMessage = e.getMessage();
        }
        assertEquals(expectedMessage,realMessage);
    }

}
