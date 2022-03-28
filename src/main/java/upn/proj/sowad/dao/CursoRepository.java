package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import upn.proj.sowad.entities.Curso;

public interface CursoRepository extends JpaRepository<Curso,Long> {

	
	Curso findCursoByNombre(String nombre);
	
}
