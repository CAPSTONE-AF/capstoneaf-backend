package upn.proj.sowad.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ques")
    private Long idQuestion;

    @Lob
    @Column(name = "cont_ques")
    private String content;

    @Lob
    @Column(name = "opti_1")
    private String option1;

    @Lob
    @Column(name = "opti_2")
    private String option2;

    @Lob
    @Column(name = "opti_3")
    private String option3;

    @Lob
    @Column(name = "opti_4")
    private String option4;

    @Column(name = "ans_ques")
    private String answer; // OP1 | OP2 | OP3 | OP4


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name="id_quiz")
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Answer> answers;
}