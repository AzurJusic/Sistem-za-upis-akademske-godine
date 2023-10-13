package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

@Entity
public class AkademskiPredmet {
    
	private long id;
	private Predmet predmet;//
	private String godina_odrzavanja;
	private AkademskaGodina akademskaGodina;//
	private List<Student> studenti=new ArrayList<>(); //
	
	private List<ZahtjevBodovi> zahtBod = new ArrayList<>();//
	private List<ZahtjevPreduslov> zahtPred = new ArrayList<>();//
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@ManyToOne()
	public Predmet getPredmet() {
		return predmet;
	}
	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}
	public String getGodina_odrzavanja() {
		return godina_odrzavanja;
	}
	public void setGodina_odrzavanja(String godina_odrzavanja) {
		this.godina_odrzavanja = godina_odrzavanja;
	}
	@ManyToOne
	public AkademskaGodina getAkademskaGodina() {
		return akademskaGodina;
	}
	public void setAkademskaGodina(AkademskaGodina akademskaGodina) {
		this.akademskaGodina = akademskaGodina;
	}
	public Nastavnik getNastavnik() {
		return predmet.getNastavnik();
	}
	
	public String toString() {
		
		return predmet.getSifra_predmeta()+" "+predmet.getNaziv()+", "+ predmet.getNastavnik().getZvanje()+" "+predmet.getNastavnik().getIme()
		+" "+predmet.getNastavnik().getPrezime();
	}
	
	@ManyToMany
	@JoinTable(name="predmet_student",
	joinColumns=@JoinColumn(name="predmet_id"),
	inverseJoinColumns=@JoinColumn(name="student_id"))
	public List<Student> getStudenti() {
		return studenti;
	}
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	
	@OneToMany
	public List<ZahtjevBodovi> getZahtBod() {
		return zahtBod;
	}
	public void setZahtBod(List<ZahtjevBodovi> zahtBod) {
		this.zahtBod = zahtBod;
	}
	
	@OneToMany
	public List<ZahtjevPreduslov> getZahtPred() {
		return zahtPred;
	}
	public void setZahtPred(List<ZahtjevPreduslov> zahtPred) {
		this.zahtPred = zahtPred;
	}
	
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		
		if(obj==null || getClass()!=obj.getClass())
			return false;
		AkademskiPredmet p=(AkademskiPredmet)obj;
		
		return (this.id==p.getId());
	}
	

	
}
