package upn.proj.sowad.services.impl;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static upn.proj.sowad.constant.CursoImplConstant.NOMBRE_ALREADY_EXISTS;
import static upn.proj.sowad.constant.CursoImplConstant.NO_CURSO_FOUND_BY_NOMBRE;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upn.proj.sowad.dao.CursoRepository;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.services.CursoService;

@Service
@Transactional
public class CursoServiceImpl implements CursoService{

	private CursoRepository cursoRepository;

	@Autowired
	public CursoServiceImpl(CursoRepository cursoRepository) {
		super();
		this.cursoRepository = cursoRepository;
	}

	@Override
	public List<Curso> getCursos() {
		return cursoRepository.findAll();
	}

	@Override
	public Curso findCursoByNombre(String nombre) {
		return cursoRepository.findCursoByNombre(nombre);
	}

	@Override
	public Curso addNewCurso(String nombre) throws CursoNotFoundException, CursoExistsException {
		validateNewNombre(EMPTY,nombre);
		Curso curso = new Curso();
		curso.setNombre(nombre);
		cursoRepository.save(curso);
		return curso;
	}


	@Override
	public void deleteCurso(String nombre) {
		 Curso curso = cursoRepository.findCursoByNombre(nombre);
	     cursoRepository.deleteById(curso.getIdCurso());
	}

	@Override
    public Curso updateCurso(String currentNombre, String newNombre) throws CursoNotFoundException, CursoExistsException {
        Curso currentCurso = validateNewNombre(currentNombre, newNombre);
        currentCurso.setNombre(newNombre);
        cursoRepository.save(currentCurso);
        return currentCurso;
    }
	
	public Curso validateNewNombre(String currentNombre, String newNombre) throws CursoNotFoundException, CursoExistsException {
        Curso cursoByNewNombre = findCursoByNombre(newNombre);
        if(StringUtils.isNotBlank(currentNombre)) {
            Curso currentCurso = findCursoByNombre(currentNombre);
            if(currentCurso == null) {
                throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + currentNombre);
            }
            if(cursoByNewNombre != null && !currentCurso.getIdCurso().equals(cursoByNewNombre.getIdCurso())) {
                throw new CursoExistsException(NOMBRE_ALREADY_EXISTS);
            }
            return currentCurso;
        } else {
            if(cursoByNewNombre != null) {
                throw new CursoExistsException(NOMBRE_ALREADY_EXISTS);
            }
            return null;
        }
    }

	@Override
	public Curso register(String nombre) throws CursoNotFoundException, CursoExistsException {
		validateNewNombre(EMPTY,nombre);
		Curso curso = new Curso();
		curso.setNombre(nombre);
		cursoRepository.save(curso);
		return curso;
		
	}
	
	
	
	
	
	
	
	
}
