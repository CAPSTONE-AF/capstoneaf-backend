package upn.proj.sowad.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.proj.sowad.entities.Answer;
import upn.proj.sowad.entities.Quiz;
import upn.proj.sowad.entities.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private String idResultado;
    private String idQuiz;
    private String idUser;
    private String numCorrectAns;
    private String numIncorrectAns;
    private String resultScore;
    private String submitDate;
}
