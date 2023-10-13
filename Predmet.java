package models;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Predmet {
	
	private Long id;
	private String sifra_predmeta; 
	private String naziv;
	private int ects = 0;
	private String preduslovi=new String();
	
	 
 	//clanovi za definiranje veza
	private StudijskiProgram sp;//
	private Nastavnik nastavnik;//
	private Semestar semestar;//
	private List<AkademskiPredmet> akademskiPredmeti=new ArrayList<>();//
	private Usmjerenje usmjerenje;//
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	
	public String getSifra_predmeta() {
		return sifra_predmeta;
	}
	public void setSifra_predmeta(String sifra_predmeta) {
		this.sifra_predmeta = sifra_predmeta;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getEcts() {
		return ects;
	}
	public void setEcts(int ects) {
		this.ects = ects;
	}
	
	@ManyToOne()
	public StudijskiProgram getSp() {
		return sp;
	}
	public void setSp(StudijskiProgram sp) {
		this.sp = sp;
	}
	@ManyToOne(cascade=CascadeType.ALL)
	public Nastavnik getNastavnik() {
		return nastavnik;
	}
	public void setNastavnik(Nastavnik nastavnik) {
		this.nastavnik = nastavnik;
	 }
	
	

	    
	
	public String getPreduslovi() {
		return preduslovi;
	}
	public void setPreduslovi(String preduslovi) {
		this.preduslovi = preduslovi;
	}
	public String toString() {
		
		return this.sifra_predmeta+" "+this.naziv+" ECTS: "+this.ects+
				" Semestar: "+this.semestar+
				" Preduslovi: "+this.preduslovi+"Nastavnik: "+this.nastavnik; 
	}
	
	@OneToMany(mappedBy="predmet")
	public List<AkademskiPredmet> getAkademskiPredmeti() {
		return akademskiPredmeti;
	}
	public void setAkademskiPredmeti(List<AkademskiPredmet> akademskiPredmeti) {
		this.akademskiPredmeti = akademskiPredmeti;
	}
	@ManyToOne
	public Usmjerenje getUsmjerenje() {
		return usmjerenje; 
	}
	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	public Semestar getSemestar() {
		return semestar;
	}
	public void setSemestar(Semestar semestar) {
		this.semestar = semestar;
	}	
	
	
}
