package upn.proj.sowad.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="grado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Grado {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_grado")
    private Long idGrado;

    @Column(name="usu_crea")
    private String usuarioCreacion;

    @Column(name="fec_crea")
    private Date fechaCreacion;

    @Column(name="usu_modi")
    private String usuarioModificacion;

    @Column(name="fec_modi")
    private Date fechaModificacion;

    @Column(name="val_nomb_grado")
    private String valorNombreGrado;
}
