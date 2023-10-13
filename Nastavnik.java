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
public class Nastavnik  {
    //clanovi
  
    private Long id;
    private String ime;
    private String prezime;
    private String username;
    private String password;
	private String zvanje;
	private boolean prodekan=false;
	
	
	//clanovi za kreiranje veza
	private List<Predmet> predmeti=new ArrayList<>();//
   
    
    private StudijskiProgram studijskiProgram;//
    private Usmjerenje usmjerenje;//
    
//    private List<ZahtjevBodovi> prenosZahtjevi = new ArrayList<>();
//    private List<ZahtjevPreduslov> preduslovZahtjevi = new ArrayList<>();
    
    //konstruktori
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
    @OneToMany(cascade=CascadeType.ALL,mappedBy="nastavnik")  
	public List<Predmet> getPredmeti() {
		return predmeti;
	}
	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}
	
	public String getZvanje() {
		return zvanje;
	}
	public void setZvanje(String zvanje) {
		this.zvanje = zvanje;
	}

	
	
	public boolean isProdekan() {
		return prodekan;
	}
	public void setProdekan(boolean prodekan) {
		this.prodekan = prodekan; 
	}

	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String toString() {
		return this.getIme()+" "+this.getPrezime()+","+this.getZvanje();
	}
	
	@ManyToOne
	public StudijskiProgram getStudijskiProgram() {
		return studijskiProgram;
	}
	public void setStudijskiProgram(StudijskiProgram studijskiProgram) {
		this.studijskiProgram = studijskiProgram;
	}
	@ManyToOne 
	public Usmjerenje getUsmjerenje() {
		return usmjerenje;
	}
	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}
//	public List<ZahtjevBodovi> getPrenosZahtjevi() {
//		return prenosZahtjevi;
//	}
//	public void setPrenosZahtjevi(List<ZahtjevBodovi> prenosZahtjevi) {
//		this.prenosZahtjevi = prenosZahtjevi;
//	}
//	public List<ZahtjevPreduslov> getPreduslovZahtjevi() {
//		return preduslovZahtjevi;
//	}
//	public void setPreduslovZahtjevi(List<ZahtjevPreduslov> preduslovZahtjevi) {
//		this.preduslovZahtjevi = preduslovZahtjevi;
//	}
	

	
	
}

	



