package upn.proj.sowad.services;

import java.util.List;

import javax.mail.MessagingException;

import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.EmailExistException;
import upn.proj.sowad.exception.domain.UserNotFoundException;
import upn.proj.sowad.exception.domain.UsernameExistException;


public interface CursoService {

    List<Curso> getCursos();
    
    Curso findCursoByNombre(String nombre);

    Curso addNewCurso(String nombre) throws CursoNotFoundException, CursoExistsException;

    Curso updateCurso(String currentNombre, String newNombre) throws CursoNotFoundException, CursoExistsException;

    void deleteCurso(String nombre);
    
    Curso register(String nombre) throws CursoNotFoundException, CursoExistsException;
}
