package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.proj.sowad.entities.Answer;
import upn.proj.sowad.entities.Question;
import upn.proj.sowad.entities.Result;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

    Answer findByIdAnswer(Long idAnswer);

    Answer findByResultAndQuestion(Result resultFound, Question questionFound);

    List<Answer> findAllByResult(Result result);
}
