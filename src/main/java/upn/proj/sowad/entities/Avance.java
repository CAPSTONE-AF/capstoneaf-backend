package upn.proj.sowad.entities;

import java.util.Date;

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
@Table(name="avance")
public class Avance {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_avance")
	private long idAvance;
	
	@Column(name="fecha")
	private Date fecha;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY)
	@JoinColumn(name="id_user")
	@JsonIgnore
	private User user;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY)
	@JoinColumn(name="id_tema")
	@JsonIgnore
	private Tema tema;
	
	public Avance() {
		super();
	}

	public Avance(Date fecha, User user, Tema tema) {
		super();
		this.fecha = fecha;
		this.user = user;
		this.tema = tema;
	}

	public long getIdAvance() {
		return idAvance;
	}

	public void setIdAvance(long idAvance) {
		this.idAvance = idAvance;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	

}
