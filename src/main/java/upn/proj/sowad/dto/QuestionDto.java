package upn.proj.sowad.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.proj.sowad.entities.Quiz;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String idQuestion;
    private String content;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer; // OP1 | OP2 | OP3 | OP4
    private String idQuiz;

}
