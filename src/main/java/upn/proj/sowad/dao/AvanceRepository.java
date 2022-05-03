package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.proj.sowad.entities.Avance;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.entities.User;

import java.util.List;

public interface AvanceRepository extends JpaRepository<Avance, Long> {

    Avance findByIdAvance(Long idAvance);

    List<Avance> findAllByUserId(Long idUser);

    Avance findByUserAndTema(User user, Tema tema);

}
