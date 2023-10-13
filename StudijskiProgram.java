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
public class StudijskiProgram {
	//clanovi
	private int id;
	private String naziv; 
	private String akademska_godina;
	
	
	
	//clanovi za definiranje veza
	private Prodekan prodekan;//
	private List<Predmet> predmeti=new ArrayList<>();//
	private List<Usmjerenje> usmjerenja=new ArrayList<>();//
	private List<Nastavnik> nastavnici=new ArrayList<>();//
	private List<Student> studenti=new ArrayList<>();//
	private List<Semestar> semestri=new ArrayList<>();//
	private Administrator administrator;//

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


	public String getAkademska_godina() {
		return akademska_godina;
	}


	public void setAkademska_godina(String akademska_godina) {
		this.akademska_godina = akademska_godina;
	}

    @OneToMany(mappedBy="sp",cascade=CascadeType.ALL)
	public List<Predmet> getPredmeti() {
		return predmeti;
	}


	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}

    @ManyToOne
	public Administrator getAdministrator() {
		return administrator;
	}


	public void setAdministrator(Administrator administrator) {
		this.administrator = administrator;
	}

    @ManyToOne(cascade=CascadeType.ALL)
	public Prodekan getProdekan() {
		return prodekan;
	}


	public void setProdekan(Prodekan prodekan) {
		this.prodekan = prodekan;
	}

    @OneToMany(mappedBy="studijskiProgram")
	public List<Usmjerenje> getUsmjerenja() {
		return usmjerenja;
	}


	public void setUsmjerenja(List<Usmjerenje> usmjerenja) {
		this.usmjerenja = usmjerenja;
	}

    @OneToMany(cascade=CascadeType.ALL,mappedBy="studijskiProgram")
	public List<Nastavnik> getNastavnici() {
		return nastavnici;
	}


	public void setNastavnici(List<Nastavnik> nastavnici) {
		this.nastavnici = nastavnici;
	}

    @OneToMany(cascade=CascadeType.ALL,mappedBy="studijskiProgram")
	public List<Student> getStudenti() {
		return studenti;
	}


	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="studijskiProgram")
	public List<Semestar> getSemestri() {
		return semestri;
	}


	public void setSemestri(List<Semestar> semestri) {
		this.semestri = semestri;
	}


	public String toString() {
		return this.naziv+" "+this.akademska_godina;
	}
	
	

     
}
