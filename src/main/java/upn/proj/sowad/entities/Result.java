package upn.proj.sowad.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "result")
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resul")
    private Long idResultado;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name="id_quiz")
    @JsonIgnore
    private Quiz quiz;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    @JsonIgnore
    private User user;

    @Column(name = "num_corr_ans")
    private Integer numCorrectAns;

    @Column(name = "num_inco_ans")
    private Integer numIncorrectAns;

    @Column(name = "resu_sco")
    private Double resultScore;

    @Column(name = "subm_date")
    private Date submitDate;

    @OneToMany(mappedBy = "result",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Answer> answers;

}
