package upn.proj.sowad.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="recurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recurso {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_recurso")
	private Long idRecurso;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="tipo")
	private String tipo;
	
	@Column(name="contenido")
	private String contenido;

	@Column(name="usu_crea")
	private String usuarioCreacion;

	@Column(name="fec_crea")
	private Date fechaCreacion;

	@Column(name="usu_modi")
	private String usuarioModificacion;

	@Column(name="fec_modi")
	private Date fechaModificacion;

	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY)
	@JoinColumn(name="id_tema")
	@JsonIgnore
	private Tema tema;
	

	
}
