package upn.proj.sowad.services.impl;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static upn.proj.sowad.constant.CursoImplConstant.*;
import static upn.proj.sowad.constant.FileConstant.FORWARD_SLASH;
import static upn.proj.sowad.constant.FileConstant.TEMA_FOLDER;
import static upn.proj.sowad.constant.RecursoImplConstant.*;
import static upn.proj.sowad.constant.TemaImplConstant.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upn.proj.sowad.dao.CursoRepository;
import upn.proj.sowad.dao.RecursoRepository;
import upn.proj.sowad.dao.TemaRepository;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Recurso;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.RecursoExistsException;
import upn.proj.sowad.exception.domain.RecursoNotFoundException;
import upn.proj.sowad.exception.domain.TemaExistsException;
import upn.proj.sowad.exception.domain.TemaNotFoundException;
import upn.proj.sowad.services.RecursoService;

@Service
@Transactional
public class RecursoServiceImpl implements RecursoService{

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	private TemaRepository temaRepository;
	private CursoRepository cursoRepository;
	private RecursoRepository recursoRepository;
	
	@Autowired
	public RecursoServiceImpl(TemaRepository temaRepository,CursoRepository cursoRepository, 
			RecursoRepository recursoRepository) {
		super();
		this.temaRepository = temaRepository;
		this.cursoRepository = cursoRepository;
		this.recursoRepository = recursoRepository;
	}
	
	
	@Override
	public List<Recurso> getRecursos(String nombreCurso, String tituloTema) throws TemaNotFoundException, CursoNotFoundException {
		List<Recurso> recursosRequeridos = null;
		Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
		Tema tema = temaRepository.findTemaByTitulo(tituloTema);
		if(curso!=null) {
			if(tema!=null) {
				recursosRequeridos = recursoRepository.findAllByTema(tema);
			}
			else {
				throw new TemaNotFoundException(NO_TEMA_FOUND_BY_TITULO + tituloTema);
			}
		} else {
			throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
		}
		return recursosRequeridos;
	}

	@Override
	public Recurso findRecursoByNombre(String nombreCurso, String tituloTema, String nombre) throws TemaNotFoundException, CursoNotFoundException {
		Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
		Tema tema = temaRepository.findTemaByTitulo(tituloTema);
		Recurso recurso = recursoRepository.findRecursoByNombre(nombre);
		if(curso!=null) {
			if(tema!=null) {
				if(recurso!=null) {
					if(recurso.getTema().equals(tema)) return recurso;
				}
			}else {
				throw new TemaNotFoundException(NO_TEMA_FOUND_BY_TITULO + tituloTema);
			}
		}else {
			throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
		}
		return null;
	}

	@Override
	public Recurso addNewRecurso(String nombreCurso, String tituloTema, String nombre, String tipo, String contenido) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, RecursoNotFoundException, RecursoExistsException {
		validateNewNombre(nombreCurso,tituloTema,EMPTY,nombre);
		Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
		Tema tema = temaRepository.findTemaByTitulo(tituloTema);
		Recurso recurso = null;
		if(curso!=null) {
			if(tema!=null) {
				recurso = new Recurso();
				recurso.setTema(tema);
				recurso.setTipo(tipo);
				recurso.setContenido(contenido);
				recurso.setNombre(nombre);
		        recursoRepository.save(recurso);
			}else {
				throw new TemaNotFoundException(NO_TEMA_FOUND_BY_TITULO + tituloTema);
			}			
		} else {
			throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
		}
		return recurso;
	}

	@Override
	public Recurso updateRecurso(String nombreCurso, String tituloTema, String currentNombre, String newNombre,
			String tipo, String contenido) throws CursoNotFoundException, TemaNotFoundException, CursoExistsException, TemaExistsException, RecursoNotFoundException, RecursoExistsException {
		Recurso currentRecurso = validateNewNombre(nombreCurso,tituloTema,currentNombre, newNombre);
		Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
		Tema tema = temaRepository.findTemaByTitulo(tituloTema);
		if(curso!=null) {
			if(tema!=null) {
				currentRecurso.setTipo(tipo);
				currentRecurso.setContenido(contenido);
				currentRecurso.setNombre(newNombre);
				currentRecurso.setTema(tema);
				recursoRepository.save(currentRecurso);
			}else {
				throw new TemaNotFoundException(NO_TEMA_FOUND_BY_TITULO + tituloTema);
			}			
		}else {
			throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
		}
        return currentRecurso;
	}

	@Override
	public void deleteRecurso(String nombreCurso, String tituloTema, String nombre) throws TemaNotFoundException, CursoNotFoundException {
		Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
		Tema tema = temaRepository.findTemaByTitulo(tituloTema);
		Recurso recurso = findRecursoByNombre(nombreCurso,tituloTema,nombre);
        recursoRepository.deleteById(recurso.getIdRecurso());	
	}
	
	public Recurso validateNewNombre(String nombreCurso, String tituloTema, String currentNombre, String newNombre) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, RecursoNotFoundException, RecursoExistsException {
        Recurso recursoByNewNombre = findRecursoByNombre(nombreCurso,tituloTema, newNombre);
        if(StringUtils.isNotBlank(currentNombre)) {
            Recurso currentRecurso = findRecursoByNombre(nombreCurso,tituloTema,currentNombre);
            if(currentRecurso == null) {
                throw new RecursoNotFoundException(NO_RECURSO_FOUND_BY_NOMBRE + currentNombre);
            }
            if(recursoByNewNombre != null && !currentRecurso.getIdRecurso().equals(recursoByNewNombre.getIdRecurso())) {
                throw new RecursoExistsException(RECURSO_ALREADY_EXISTS);
            }
            return currentRecurso;
        } else {
            if(recursoByNewNombre != null) {
                throw new RecursoExistsException(RECURSO_ALREADY_EXISTS);
            }
            return null;
        }
    }

}
