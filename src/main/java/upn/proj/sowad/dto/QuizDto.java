package upn.proj.sowad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String idQuiz;
    private String title;
    private String description;
    private String active;
    private String maxScore;
    private String numberOfQuestions;
    private String idTema;
}
