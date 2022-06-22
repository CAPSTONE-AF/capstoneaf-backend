package upn.proj.sowad.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Entity
@Table(name = "result")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resul")
    private Long idResultado;

    @ManyToOne
    @JoinColumn(name="id_quiz")
    @JsonIgnore
    private Quiz quiz;

    @ManyToOne
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

    @OneToMany(mappedBy = "result",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Answer> answers;

}
