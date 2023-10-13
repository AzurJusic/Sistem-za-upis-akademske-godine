package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Semestar {
     
	private Long id;
	private String broj=null;
	private String ljetniZimski=null;
	private List<Predmet> predmeti=new ArrayList<>();//
	private StudijskiProgram studijskiProgram;//
	
	public String getBroj() {
		return broj;
	}
	public void setBroj(String broj) {
		this.broj = broj;
	}
	public String getLjetniZimski() {
		return ljetniZimski;
	}
	public void setLjetniZimski(String ljetniZimski) {
		this.ljetniZimski = ljetniZimski;
	}
	@OneToMany(mappedBy="semestar")
	public List<Predmet> getPredmeti() {
		return predmeti;
	}
	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}
	@ManyToOne
	public StudijskiProgram getStudijskiProgram() {
		return studijskiProgram;
	}
	public void setStudijskiProgram(StudijskiProgram studijskiProgram) {
		this.studijskiProgram = studijskiProgram;
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String toString() {
//		return this.broj+" "+this.ljetniZimski+" "+this.studijskiProgram;
		return this.ljetniZimski + " " + this.broj;
	}
	
	
}
