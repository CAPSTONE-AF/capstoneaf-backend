package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.proj.sowad.entities.Avance;
import upn.proj.sowad.entities.Grado;

public interface GradoRepository extends JpaRepository<Grado, Long> {
    Grado findByIdGrado(Long idGrado);
}
