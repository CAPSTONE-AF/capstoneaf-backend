package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.proj.sowad.entities.Question;
import upn.proj.sowad.entities.Quiz;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByIdQuestion(Long idQuestion);

    List<Question> findAllByQuiz(Quiz quizFound);
}
