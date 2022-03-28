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

@Entity
@Table(name="recurso")
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
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY)
	@JoinColumn(name="id_tema")
	@JsonIgnore
	private Tema tema;
	
	public Recurso() {
		super();
	}

	public Recurso(String tipo, String contenido, Tema tema) {
		super();
		this.tipo = tipo;
		this.contenido = contenido;
		this.tema = tema;
	}

	public Long getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(Long idRecurso) {
		this.idRecurso = idRecurso;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
	
	
}
