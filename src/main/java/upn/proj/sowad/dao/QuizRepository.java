package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.entities.Tema;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    Quiz findByIdQuiz(Long idQuiz);

    List<Quiz> findAllByTema(Tema tema);
}
