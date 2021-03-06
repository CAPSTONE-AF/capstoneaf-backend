package upn.proj.sowad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.entities.Result;
import upn.proj.sowad.entities.User;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Result findByIdResultado(Long idResult);

    Result findByUserAndQuiz(User userFound, Quiz quizFound);

    List<Result> findAllByUserOrderBySubmitDateDesc(User user);
}
