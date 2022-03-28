package upn.proj.sowad.entities;

import java.util.ArrayList;
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
		this.temas = new ArrayList<>();
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
