package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class Usmjerenje {
	
	private int id;
	private String naziv;
	
	
	
	private List<Nastavnik> nastavnici=new ArrayList<>();//
	private List<Student> studenti=new ArrayList<>();//
	private StudijskiProgram studijskiProgram;//
	private List<Predmet> predmeti=new ArrayList<>();//
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	@OneToMany(mappedBy="usmjerenje",cascade=CascadeType.ALL)
	public List<Nastavnik> getNastavnici() {
		return nastavnici;
	}
	public void setNastavnici(List<Nastavnik> nastavnici) {
		this.nastavnici = nastavnici;
	}
	@OneToMany(mappedBy="usmjerenje",cascade=CascadeType.ALL)
	public List<Student> getStudenti() {
		return studenti;
	}
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	
	@ManyToOne
	public StudijskiProgram getStudijskiProgram() {
		return studijskiProgram;
	}
	public void setStudijskiProgram(StudijskiProgram studijskiProgram) {
		this.studijskiProgram = studijskiProgram;
	}
	@OneToMany(cascade=CascadeType.ALL,mappedBy="usmjerenje")
	public List<Predmet> getPredmeti(){
		return predmeti;
	}
	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}
	
	
	public String toString() {
		return this.naziv;
	}
	
	
	
	

}
