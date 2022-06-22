package upn.proj.sowad.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "quiz")
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quiz")
    private Long idQuiz;

    @Column(name = "tit_quiz")
    private String title;

    @Column(name = "desc_quiz")
    private String description;

    @Column(name = "ind_acti")
    private Boolean active;

    @Column(name = "max_score")
    private Integer maxScore;

    @Column(name = "nume_ques")
    private Integer numberOfQuestions;

    @ManyToOne
    @JsonIgnore
    private Tema tema;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Result> results = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();
}
