package upn.proj.sowad.services;

import java.util.List;

import upn.proj.sowad.entities.Recurso;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.RecursoExistsException;
import upn.proj.sowad.exception.domain.RecursoNotFoundException;
import upn.proj.sowad.exception.domain.TemaExistsException;
import upn.proj.sowad.exception.domain.TemaNotFoundException;	

public interface RecursoService {

    List<Recurso> getRecursos(String nombreCurso, String tituloTema) throws TemaNotFoundException, CursoNotFoundException;

    Recurso findRecursoByNombre(String nombreCurso, String tituloTema, String nombre) throws TemaNotFoundException, CursoNotFoundException;
    
    Recurso addNewRecurso(String nombreCurso, String tituloTema, String nombre, String tipo, String contenido) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, RecursoNotFoundException, RecursoExistsException;

    Recurso updateRecurso(String nombreCurso, String tituloTema, String currentNombre, String newNombre, String tipo, String contenido) throws CursoNotFoundException, TemaNotFoundException, CursoExistsException, TemaExistsException, RecursoNotFoundException, RecursoExistsException;

    void deleteRecurso(String nombreCurso, String tituloTema, String nombre) throws TemaNotFoundException, CursoNotFoundException;

	
}
