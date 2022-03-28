package upn.proj.sowad.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Tema;

public interface TemaRepository extends JpaRepository<Tema,Long>{

	public List<Tema> findAllByCurso(Curso curso);

	public Tema findTemaByTitulo(String titulo);
	
}
