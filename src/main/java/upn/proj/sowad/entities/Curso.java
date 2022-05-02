package upn.proj.sowad.entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="curso")
public class Curso {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_curso")
	private Long idCurso;
	
	@Column(name="nombre")
	private String nombre;

	@Column(name="usu_curso_modi")
	private String usu_curso_modi;

	@Column(name="usu_crear_curso")
	private String usu_crear_curso;
	
	@Column(name="fec_curso_crear")
	private Date fec_curso_crear;

	@Column(name="fec_curso_modi")
	private Date fec_curso_modi;

	@Column(name="cant_temas")
	private int cantTemas;
	
	@OneToMany(cascade = {CascadeType.ALL},mappedBy="curso")
	@JsonIgnore
	private List<Tema> temas;

	
	public Curso() {
		this.cantTemas = 0;
		this.temas = new ArrayList<>();
	}

	public Curso(String nombre) {
		this.nombre = nombre;
		this.cantTemas = 0;
		this.usu_crear_curso=usu_crear_curso;
		this.fec_curso_crear=fec_curso_crear;
		this.fec_curso_modi=fec_curso_modi;
		this.usu_curso_modi=usu_curso_modi;
		this.temas = new ArrayList<>();
	}


	public String getUsu_curso_modi() {
		return usu_curso_modi;
	}

	public void setUsu_curso_modi(String usu_curso_modi) {
		this.usu_curso_modi = usu_curso_modi;
	}

	public String getUsu_crear_curso() {
		return usu_crear_curso;
	}

	public void setUsu_crear_curso(String usu_crear_curso) {
		this.usu_crear_curso = usu_crear_curso;
	}

	public Date getFec_curso_crear() {
		return fec_curso_crear;
	}

	public void setFec_curso_crear(Date fec_curso_crear) {
		this.fec_curso_crear = fec_curso_crear;
	}

	public Date getFec_curso_modi() {
		return fec_curso_modi;
	}

	public void setFec_curso_modi(Date fec_curso_modi) {
		this.fec_curso_modi = fec_curso_modi;
	}

	public void setCantTemas(int cantTemas) {
		this.cantTemas = cantTemas;
	}

	public Long getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getCantTemas() {
		return cantTemas;
	}

	public void setCantTemas(boolean resp) {
		if(resp)	this.cantTemas++;
		else this.cantTemas--;
	}

	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}

	public List<Tema> getTemas() {
		return temas;
	}

	public void setTemas(Tema tema) {
		temas.add(tema);
		cantTemas++;
	}

	
	
	
	
	
	
}
