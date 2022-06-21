package upn.proj.sowad.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.UtilityException;


public interface CursoService {

    List<Curso> getCursos();
    
    Curso findCursoByNombre(String nombre) throws UtilityException;

    Curso addNewCurso(String nombre, String idUser) throws CursoNotFoundException, CursoExistsException, UtilityException;

    Curso updateCurso(String currentNombre, String newNombre, String idUser) throws CursoNotFoundException, CursoExistsException, UtilityException;

    void deleteCurso(String nombre) throws UtilityException;
    
    Curso register(String nombre) throws CursoNotFoundException, CursoExistsException, UtilityException;

    ByteArrayInputStream exportarPiechartPopCursos(BufferedImage bufferedImage);
}
