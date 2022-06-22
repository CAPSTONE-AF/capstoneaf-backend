package upn.proj.sowad.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "recurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Long idRecurso;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo")
    private String tipo;

    @Lob
    @Column(name = "contenido")
    private String contenido;

    @Column(name = "usu_crea")
    private String usuarioCreacion;

    @Column(name = "fec_crea")
    private Date fechaCreacion;

    @Column(name = "usu_modi")
    private String usuarioModificacion;

    @Column(name = "fec_modi")
    private Date fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "id_tema")
    @JsonIgnore
    private Tema tema;


}
