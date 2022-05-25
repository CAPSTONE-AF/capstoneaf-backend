package upn.proj.sowad.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.proj.sowad.entities.Question;
import upn.proj.sowad.entities.Result;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private String idAnswer;
    private String ans; // OP1 | OP2 | OP3 | OP4
    private String idResult;
    private String idQuestion;
}
