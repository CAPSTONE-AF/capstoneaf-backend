package upn.proj.sowad.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="tema")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tema {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tema")
	private Long idTema;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="portada_url")
	private String portadaUrl;

	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY)
	@JoinColumn(name="id_curso")
	@JsonIgnore
	private Curso curso;
	
	@OneToMany(cascade = {CascadeType.ALL},mappedBy="tema")
	@JsonIgnore
	private List<Recurso> recursos;
	
	@OneToMany(cascade = {CascadeType.ALL},mappedBy="tema")
	private List<Avance> avances;
	

	public Tema() {
		this.recursos = new ArrayList<>();
		this.avances = new ArrayList<>();
	}

	public Tema(String titulo, String portadaUrl, Curso curso) {
		super();
		this.titulo = titulo;
		this.portadaUrl = portadaUrl;
		this.curso = curso;
		this.recursos = new ArrayList<>();
		this.avances = new ArrayList<>();
	}

	public Long getIdTema() {
		return idTema;
	}

	public void setIdTema(Long idTema) {
		this.idTema = idTema;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getPortadaUrl() {
		return portadaUrl;
	}

	public void setPortadaUrl(String portadaUrl) {
		this.portadaUrl = portadaUrl;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	
	}

	public List<Recurso> getRecursos() {
		return recursos;
	}

	public void setRecursos(Recurso recurso) {
		recursos.add(recurso);
	}

	public List<Avance> getAvances() {
		return avances;
	}

	public void setAvances(Avance avance) {
		this.avances.add(avance);
	}
	
	
}