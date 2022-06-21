package upn.proj.sowad.services;

import java.util.List;

import upn.proj.sowad.entities.Recurso;
import upn.proj.sowad.exception.domain.*;

public interface RecursoService {

    List<Recurso> getRecursos(String nombreCurso, String tituloTema) throws TemaNotFoundException, CursoNotFoundException, UtilityException;

    Recurso findRecursoByNombre(String nombreCurso, String tituloTema, String nombre) throws TemaNotFoundException, CursoNotFoundException;
    
    Recurso addNewRecurso(String nombreCurso, String tituloTema, String nombre, String tipo, String contenido) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, RecursoNotFoundException, RecursoExistsException, UtilityException;

    Recurso updateRecurso(String nombreCurso, String tituloTema, String currentNombre, String newNombre, String tipo, String contenido) throws CursoNotFoundException, TemaNotFoundException, CursoExistsException, TemaExistsException, RecursoNotFoundException, RecursoExistsException;

    void deleteRecurso(String nombreCurso, String tituloTema, String nombre) throws TemaNotFoundException, CursoNotFoundException;

	
}
