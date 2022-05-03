package upn.proj.sowad.entities;

import java.util.ArrayList;
import java.util.Date;
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
	
	@Column(name="usu_tema_modi")
	private String usu_tema_modi;

	@Column(name="usu_crear_tema")
	private String usu_crear_tema;
	
	@Column(name="fec_tema_crear")
	private Date fec_tema_crear;

	@Column(name="fec_tema_modi")
	private Date fec_tema_modi;

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
		this.usu_crear_tema=usu_crear_tema;
		this.fec_tema_crear=fec_tema_crear;
		this.fec_tema_modi=fec_tema_modi;
		this.usu_tema_modi=usu_tema_modi;
	}

	public String getUsu_tema_modi() {
		return usu_tema_modi;
	}

	public void setUsu_tema_modi(String usu_tema_modi) {
		this.usu_tema_modi = usu_tema_modi;
	}

	public String getUsu_crear_tema() {
		return usu_crear_tema;
	}

	public void setUsu_crear_tema(String usu_crear_tema) {
		this.usu_crear_tema = usu_crear_tema;
	}

	public Date getFec_tema_crear() {
		return fec_tema_crear;
	}

	public void setFec_tema_crear(Date fec_tema_crear) {
		this.fec_tema_crear = fec_tema_crear;
	}

	public Date getFec_tema_modi() {
		return fec_tema_modi;
	}

	public void setFec_tema_modi(Date fec_tema_modi) {
		this.fec_tema_modi = fec_tema_modi;
	}

	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}

	public void setAvances(List<Avance> avances) {
		this.avances = avances;
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