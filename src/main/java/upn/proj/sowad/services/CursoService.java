package upn.proj.sowad.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;


public interface CursoService {

    List<Curso> getCursos();
    
    Curso findCursoByNombre(String nombre);

    Curso addNewCurso(String nombre, String idUser) throws CursoNotFoundException, CursoExistsException;

    Curso updateCurso(String currentNombre, String newNombre, String idUser) throws CursoNotFoundException, CursoExistsException;

    void deleteCurso(String nombre);
    
    Curso register(String nombre) throws CursoNotFoundException, CursoExistsException;

    ByteArrayInputStream exportarPiechartPopCursos(BufferedImage bufferedImage);
}
