package upn.proj.sowad.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import upn.proj.sowad.entities.Recurso;
import upn.proj.sowad.entities.Tema;

public interface RecursoRepository extends JpaRepository<Recurso,Long>{

	public List<Recurso> findAllByTema(Tema tema);

	public Recurso findRecursoByNombre(String nombre);
	
}
